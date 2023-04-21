package com.iprojectmanager.views;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.iprojectmanager.entities.*;
import com.iprojectmanager.managers.*;

/**
 *
 * @author Enrique
 */
public class FileManager extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("VistaUsuario");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = (String) request.getSession().getAttribute("actividad");
        ActivitiesManager manager = new ActivitiesManager();
        Actividad act = manager.getActividad(Long.valueOf(id));
        try {
            int leer = 0;
            File avandir;
            FileOutputStream writer;
            ServletInputStream stream;
            String limite = "", contenttype = "", read = "", filename = "";
            ContentPart part = new ContentPart();
            HttpSession user = request.getSession();
            IntegranteProyecto integrante = (IntegranteProyecto) user.getAttribute("user");
            byte[] buffer;
            contenttype = request.getContentType();
            System.out.println(contenttype);
            limite = "--" + contenttype.split(";")[1].split("=")[1];
            stream = request.getInputStream();
            buffer = new byte[254];
            leer = stream.readLine(buffer, 0, buffer.length);
            read = new String(buffer, 0, leer);
            do {
                if (read.startsWith(limite) && stream.available() != 0) {
                    leer = stream.readLine(buffer, 0, buffer.length);
                    read = new String(buffer, 0, leer);
                    part = new ContentPart();
                    part.contentdisposition = read.split(";")[0];
                    part.name = read.split(";")[1].split("=")[1];
                    part.value = ((read.split(";").length == 3) ? read.split(";")[2] : "");
                }
                leer = stream.readLine(buffer, 0, buffer.length);
                read = (leer != -1) ? new String(buffer, 0, leer) : null;
                if (read != null && read.startsWith("Content-Type")) {
                    part.contenttype = read.split(":")[1];
                    leer = stream.readLine(buffer, 0, buffer.length);
                }
                do {
                    if (read != null && read.startsWith("Content-Type")) {
                        String name = part.value.substring(part.value.lastIndexOf(File.separator));
                        avandir = new File(getServletContext().getRealPath("/") + integrante.getProyecto().getNombre() + File.separator + act.getNombre());
                        avandir.mkdirs();
                        filename = name.substring(0, name.lastIndexOf("\""));
                        writer = new FileOutputStream(getServletContext().getRealPath("/") + integrante.getProyecto().getNombre() + File.separator + act.getNombre() + File.separator + filename);
                        System.out.println("Escrito en: " + getServletContext().getRealPath("/") + integrante.getProyecto().getNombre() + File.separator + act.getNombre() + File.separator + filename);
                        do {
                            leer = stream.readLine(buffer, 0, buffer.length);
                            read = new String(buffer, 0, leer);
                            if (read.startsWith(limite) == false) {
                                writer.write(buffer, 0, leer);
                                part.part += read;
                            }
                        } while (read.startsWith(limite) == false);
                        writer.close();
                        ;
                    } else {
                        leer = stream.readLine(buffer, 0, buffer.length);
                        read = (leer != -1) ? new String(buffer, 0, leer) : null;
                    }
                } while (read != null && read.startsWith(limite) == false);
            } while (leer != -1);
            manager.updateAdvance(id, filename);
        } catch (Exception ex) {
            try {
                PrintWriter out = response.getWriter();
                ex.printStackTrace(out);
            } catch (Exception e) {
            }
        }
        this.doGet(request, response);
    }
}

class ContentPart {

    public String contentdisposition;

    public String name;

    public String value;

    public String contenttype;

    public String part;

    public ContentPart() {
        contentdisposition = "";
        name = "";
        value = "";
        contenttype = "";
        part = "";
    }
}
