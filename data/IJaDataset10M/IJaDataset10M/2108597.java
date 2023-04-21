package org.snipsnap.admin.install;

import org.snipsnap.admin.util.ApplicationCommand;
import org.snipsnap.snip.SnipLink;
import org.snipsnap.util.Checksum;
import org.snipsnap.util.FileUtil;
import org.snipsnap.util.JarUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;

/**
 * Application configuration servlet.
 * @author Matthias L. Jugel
 * @version $Id: Update.java 648 2003-01-09 09:49:12Z stephan $
 */
public class Update extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("admin") != null) {
            String srv = request.getParameter("server");
            String ctx = request.getParameter("context");
            session.setAttribute("server", srv);
            session.setAttribute("context", ctx);
            Map errors = new HashMap();
            if (request.getParameter("cancel") == null) {
                if (request.getParameter("update") != null) {
                    ApplicationCommand.execute(srv, ctx, ApplicationCommand.CMD_APPLICATION_REMOVE);
                    update(request.getParameterValues("install"), request.getParameterValues("extract"), ctx, errors);
                    ApplicationCommand.execute(srv, ctx, ApplicationCommand.CMD_APPLICATION_ADD);
                    ApplicationCommand.execute(srv, ctx, ApplicationCommand.CMD_APPLICATION_START);
                } else if (request.getParameter("download") != null) {
                    try {
                        downloadUpdate();
                        session.removeAttribute("SRVchecksum");
                    } catch (IOException e) {
                        errors.put("download", "The updated web application archive could not be downloaded!");
                    }
                }
                prepare(ctx, session, errors);
                session.setAttribute("errors", errors);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/exec/update.jsp");
                if (dispatcher != null) {
                    dispatcher.forward(request, response);
                    return;
                }
            }
        }
        response.sendRedirect(SnipLink.absoluteLink(request, "/"));
    }

    private void downloadUpdate() throws IOException {
        URL update = new URL("http://snipsnap.org/download/snipsnap-template.war");
        BufferedInputStream in = new BufferedInputStream(update.openStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("./lib/snipsnap-template.war"));
        byte buffer[] = new byte[8192];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        in.close();
        out.close();
    }

    private void update(String files[], String extract[], String ctx, Map errors) {
        List install = files != null ? Arrays.asList(files) : new ArrayList();
        List unpack = extract != null ? Arrays.asList(extract) : new ArrayList();
        try {
            JarFile template = new JarFile("./lib/snipsnap-template.war");
            JarUtil.extract(template, new File("./app" + ctx), install, unpack);
            JarUtil.checksumJar(new JarFile("./app" + ctx + "/WEB-INF/lib/servlets.jar"));
            JarUtil.checksumJar(template).store(new File("./app" + ctx + "/WEB-INF/CHECKSUMS"));
        } catch (Exception e) {
            errors.put("update", "Unable to update your application, see server.log for details!");
            e.printStackTrace();
        }
    }

    /**
   * Prepare installation procedure by checking for changed files.
   * @param ctx
   * @param session
   * @param errors
   */
    private void prepare(String ctx, HttpSession session, Map errors) {
        try {
            Checksum csTemplate = JarUtil.checksumJar(new JarFile("./lib/snipsnap-template.war"));
            Checksum server = (Checksum) session.getAttribute("SRVchecksum");
            if (server == null) {
                try {
                    server = new Checksum(new URL("http://snipsnap.org/download/CHECKSUMS"));
                    session.setAttribute("SRVchecksum", server);
                    session.setAttribute("available", server.compareChanged(csTemplate));
                } catch (IOException e) {
                    System.err.println("Updater: unable to check for updates on server: " + e);
                }
            }
            Checksum csAppInstall = null;
            try {
                csAppInstall = new Checksum(new File("./app" + ctx + "/WEB-INF/CHECKSUMS"));
            } catch (IOException e) {
                errors.put("APPchecksum", "No checksum of installed files found.");
                System.err.println("Updater: no checksum file found for '" + ctx + "'");
            }
            Checksum csAppCurrent = null;
            try {
                csAppCurrent = FileUtil.checksumDirectory(new File("./app" + ctx));
                if (csAppInstall == null) {
                    csAppCurrent.store(new File("./app" + ctx + "/WEB-INF/CHECKSUMS"));
                }
            } catch (IOException e) {
                errors.put("APPchecksum", "Unable to create or store checksums for installed filed.");
                System.err.println("Updater: unable to create current checksum for installed application '" + ctx + "'");
            }
            if (csTemplate != null && (csAppInstall != null || csAppCurrent != null)) {
                Set changed = null, unchanged = null, updated = null, installable = null;
                if (csAppInstall != null) {
                    session.setAttribute("changed", changed = csAppInstall.compareChanged(csAppCurrent));
                    session.setAttribute("unchanged", unchanged = csAppInstall.compareUnchanged(csAppCurrent));
                    installable = csTemplate.compareChanged(csAppInstall);
                    installable.retainAll(unchanged);
                    session.setAttribute("installable", installable);
                    updated = csTemplate.compareChanged(csAppInstall);
                    updated.retainAll(changed);
                    updated.removeAll(installable);
                    session.setAttribute("updated", updated);
                } else {
                    installable = csTemplate.compareUnchanged(csAppCurrent);
                    session.setAttribute("installable", installable);
                }
            }
        } catch (IOException e) {
            errors.put("WARchecksum", "Unable to get checksum for template archive.");
            e.printStackTrace();
            errors.put("WARchecksum", "Unable to get checksum from template web archive!");
        }
    }
}
