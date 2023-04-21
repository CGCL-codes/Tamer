package so.n_3.musicbox.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import so.n_3.musicbox.model.MusicBox;
import so.n_3.musicbox.model.Playlist;

/**
 *
 * @author oasynnoum
 */
public class UploadMusic extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        } finally {
            out.close();
        }
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean result = false;
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Vector<FileItem> fileItems = new Vector<FileItem>();
        String playlistId = "";
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        String fileSeparator = System.getProperty("file.separator");
        String patternString = "";
        Pattern pattern = null;
        if (fileSeparator.equals("\\")) {
            patternString = "([^\\" + fileSeparator + "]*\\.mp3)$";
        } else {
            patternString = "([^" + fileSeparator + "]*\\.mp3)$";
        }
        pattern = Pattern.compile(patternString);
        try {
            List items = upload.parseRequest(request);
            Iterator itemsIterator = items.iterator();
            while (itemsIterator.hasNext()) {
                FileItem item = (FileItem) itemsIterator.next();
                if (item.isFormField()) {
                    String paramKey = item.getFieldName();
                    String paramValue = item.getString();
                    if (paramKey.equals("playlistId")) {
                        playlistId = paramValue;
                    }
                } else {
                    fileItems.add(item);
                }
            }
            Playlist playlist = MusicBox.getInstance().getPlaylistById(playlistId);
            Enumeration<FileItem> fileItemEnum = fileItems.elements();
            while (fileItemEnum.hasMoreElements()) {
                FileItem item = fileItemEnum.nextElement();
                String fileName = "";
                Matcher matcher = pattern.matcher(item.getName());
                if (!matcher.find()) {
                    continue;
                }
                fileName = matcher.group(1);
                File uploadedFile = new File(playlist.getUrl() + System.getProperty("file.separator") + fileName);
                item.write(uploadedFile);
            }
            result = playlist.sync();
        } catch (FileUploadException ex) {
            Logger.getLogger(UploadMusic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UploadMusic.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("<?xml version='1.0' encoding='UTF-8'?>");
        out.println("<result value='" + Boolean.toString(result) + "' />");
        out.close();
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
