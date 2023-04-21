package sharedPhoto.server.services;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sharedPhoto.server.ThumbnailCache;

/**
 * Servlet implementation class for Servlet: Image
 * 
 * @web.servlet name="Image" display-name="Image"
 *              description="Loads photos"
 * 
 * @web.servlet-mapping url-pattern="/SrvImage"
 * 
 */
public class SrvImage extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public SrvImage() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    private void doService(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> params = request.getParameterMap();
        String albumName = params.get("a")[0];
        String photoName = params.get("n")[0];
        System.out.println("a=" + albumName);
        System.out.println("n=" + photoName);
        System.out.println("----");
        response.setHeader("Cache-Control", "cache");
        response.setDateHeader("Expires", System.currentTimeMillis() + 15 * 1000);
        try {
            response.setContentType("images/jpeg");
            response.setHeader("Content-Disposition", "inline");
            response.setHeader("Accept-Ranges", "bytes");
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(ThumbnailCache.getImagePath(albumName, photoName)));
            ServletOutputStream os = response.getOutputStream();
            byte[] buffer = new byte[40 * 1024];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) >= 0) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (Exception e) {
        }
    }
}
