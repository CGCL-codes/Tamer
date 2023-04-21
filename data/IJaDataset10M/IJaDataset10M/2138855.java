package org.bug4j.server.jsp;

import org.apache.commons.io.output.CloseShieldOutputStream;
import org.apache.log4j.Logger;
import org.bug4j.server.store.Store;
import org.bug4j.server.store.StoreFactory;
import org.jetbrains.annotations.Nullable;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 */
public class ExportServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExportServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doit(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doit(request, response);
    }

    private void doit(HttpServletRequest request, HttpServletResponse response) {
        final String app = request.getParameter("a");
        export(response, app);
    }

    static void export(HttpServletResponse response, @Nullable String app) {
        try {
            response.setHeader("Expires", "-1");
            response.setHeader("Cache-Control", "private, max-age=0");
            response.setHeader("Content-Type", "text/xml");
            response.setHeader("Content-Disposition", "attachment; filename=\"bugs.zip\"");
            final ServletOutputStream outputStream = response.getOutputStream();
            final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            try {
                zipOutputStream.putNextEntry(new ZipEntry("bugs.xml"));
                final CloseShieldOutputStream closeShieldOutputStream = new CloseShieldOutputStream(zipOutputStream);
                final Store store = StoreFactory.getStore();
                final Exporter exporter = new Exporter(store);
                if (app != null) {
                    exporter.exportApplicationBugs(closeShieldOutputStream, app);
                } else {
                    exporter.exportAll(closeShieldOutputStream);
                }
            } catch (XMLStreamException e) {
                response.sendError(500);
            } finally {
                zipOutputStream.closeEntry();
                zipOutputStream.close();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to export", e);
            throw new IllegalStateException("Failed to export", e);
        }
    }
}
