package dynamic;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class HelloWorld extends HttpServlet {
  private static final Object UPLOAD_DIRECTORY = "";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    resp.getWriter().print("Hello from Java!\n");

  }

  @Override
  protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
  {
    //process only if its multipart content
    if( ServletFileUpload.isMultipartContent( req )){
      try {
        List<FileItem> multiparts = new ServletFileUpload(
                new DiskFileItemFactory()).parseRequest(req);

        for(FileItem item : multiparts){
          if(!item.isFormField()){
            String name = new File(item.getName()).getName();
            item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
          }
        }

        //File uploaded successfully
        req.setAttribute("message", "File Uploaded Successfully");
      } catch (Exception ex) {
        req.setAttribute("message", "File Upload Failed due to " + ex);
      }

    }else{
      req.setAttribute("message",
                           "Sorry this Servlet only handles file upload request");
    }
  }
}