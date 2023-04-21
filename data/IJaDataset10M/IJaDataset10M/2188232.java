package servlets;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import java.util.*;

/**
 *
 * @author  Roberto Canales
 * @version
 */
public class UploadImage2 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        System.out.println("Comenzamos procesamiento ficheros");
        procesaFicheros(request, out);
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    void depura(String cadena) {
        System.out.println("El error es " + cadena);
    }

    public boolean procesaFicheros(HttpServletRequest req, PrintWriter out) {
        try {
            DiskFileUpload fu = new DiskFileUpload();
            fu.setSizeMax(1024 * 512);
            depura("Ponemos el tamaño máximo");
            fu.setSizeThreshold(4096);
            fu.setRepositoryPath("/tmp");
            List fileItems = fu.parseRequest(req);
            if (fileItems == null) {
                depura("La lista es nula");
                return false;
            }
            out.print("<br>El número de ficheros subidos es: " + fileItems.size());
            Iterator i = fileItems.iterator();
            FileItem actual = null;
            depura("estamos en la iteración");
            while (i.hasNext()) {
                actual = (FileItem) i.next();
                String fileName = actual.getName();
                out.println("<br>Nos han subido el fichero" + fileName);
                File fichero = new File(fileName);
                depura("El nombre del fichero es " + fichero.getName());
                fichero = new File("c:\\ficherossubidos\\" + fichero.getName());
                actual.write(fichero);
            }
        } catch (Exception e) {
            depura("Error de Aplicación " + e.getMessage());
            return false;
        }
        return true;
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
