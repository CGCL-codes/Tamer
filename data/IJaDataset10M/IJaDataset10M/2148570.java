package de.uhilger.netzpult.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.uhilger.netzpult.server.db.DataStorage;
import de.uhilger.netzpult.shared.App;
import de.uhilger.netzpult.shared.Document;
import de.uhilger.netzpult.shared.UserShare;

/**
 * 
 * @author Copyright (c) Ulrich Hilger, http://uhilger.de
 * @author Published under the terms and conditions of the <a
 *         href="http://www.gnu.org/licenses/" target="_blank">GNU General
 *         Public License</a>
 */
public class DocumentServer {

    public static final String SEITEN_LINK = "/seiten/";

    public static final String SEPARATOR = "/";

    public static void serveDocument(HttpServletRequest req, HttpServletResponse resp, DataStorage ds, int documentId, int depth, String userName, boolean isPublic, boolean ignoreDraft) throws SQLException, IOException, Exception {
        if (isPublic || (userName != null && ds.isPermitted(userName, documentId, UserShare.READ))) {
            logText("permission for id=" + documentId);
            ResultSet rs = ds.getDocumentAsResultSet(documentId, ignoreDraft);
            if (rs.first()) {
                String mimeType = rs.getString("dc_mimetype");
                logText("mimeType=" + mimeType);
                if (mimeType.toLowerCase().equalsIgnoreCase("text/html")) {
                    htmlPage(req, resp, rs, mimeType, documentId, depth, ds, userName);
                } else if (mimeType.equalsIgnoreCase("text/plain")) {
                    resp.setContentType(mimeType);
                    Writer w = resp.getWriter();
                    Blob blob = rs.getBlob("dc_content");
                    w.write(new String(blob.getBytes((long) 1, (int) blob.length())));
                    w.flush();
                } else if (mimeType.toLowerCase().contains("image") || mimeType.equalsIgnoreCase("application/pdf")) {
                    streamDown(resp, rs, mimeType);
                } else if (mimeType.toLowerCase().startsWith("application")) {
                    resp.setContentType("application/x-download");
                    resp.setHeader("Content-Disposition", "attachment; filename=" + rs.getString("dc_title"));
                    streamDown(resp, rs, mimeType);
                }
            }
            ds.close(rs);
        } else {
            Writer w = resp.getWriter();
            w.write("<html>");
            w.write("<head>");
            w.write("<title>");
            w.write("netzpult");
            w.write("</title>");
            w.write("</head>");
            w.write("<body>");
            w.write("<p>No permission for document id " + Integer.toString(documentId));
            w.write("</body>");
            w.write("</html>");
            w.flush();
        }
    }

    public static void header(Writer w, String title, String date, int depth) throws IOException {
        w.write("<html>");
        w.write("<head>");
        w.write("<title>");
        w.write(title);
        w.write("</title>");
        w.write("<meta name=\"date\" content=\"");
        w.write(date);
        w.write("\">");
        w.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"");
        if (depth > -1) {
            w.write(getPathDepthPrefix(depth));
            w.write("../public.css\">");
        } else {
            w.write("public.css\">");
        }
        w.write("</head>");
    }

    private static void htmlPage(HttpServletRequest req, HttpServletResponse resp, ResultSet rs, String mimeType, int documentId, int depth, DataStorage ds, String userName) throws IOException, SQLException {
        resp.setContentType(mimeType);
        Writer w = resp.getWriter();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(ds.getDocumentDate(rs.getString("dc_timestamp")));
        seiteOben(req, resp, rs, mimeType, documentId, depth, ds, userName, rs.getString("dc_title"), date);
        Blob blob = rs.getBlob("dc_content");
        w.write(new String(blob.getBytes((long) 1, (int) blob.length())));
        seiteUnten(req, resp, rs, mimeType, documentId, depth, ds, userName);
        w.flush();
    }

    private static String getSearchForm() {
        StringBuffer buf = new StringBuffer();
        buf.append("<form id=\"dokKopf\" action=\"/find\" method=\"POST\">");
        buf.append("<p id=\"dokKopf\">");
        buf.append("<input id=\"dokKopf\" type=\"text\" name=\"item\" value=\"Suchbegriff\">");
        buf.append("</p>");
        buf.append("</form>");
        return buf.toString();
    }

    public static void seiteOben(HttpServletRequest req, HttpServletResponse resp, ResultSet rs, String mimeType, int documentId, int depth, DataStorage ds, String userName, String title, String date) throws IOException, SQLException {
        Writer w = resp.getWriter();
        header(w, title, date, depth);
        w.write("<body>");
        w.write("<div id=\"Seite\">");
        w.write("<table size=\"100%\" align=\"right\" id=\"dokKopf\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        w.write("<tr>");
        w.write("<td id=\"dokKopf\" width=\"40%\">&nbsp;</td>");
        w.write("<td width=\"40%\" id=\"dokKopf\">");
        w.write("<p id=\"permalink\"><a id=\"permalink\" href=\"");
        w.write(SEPARATOR);
        w.write(documentId + "\">Navigation ein</a>&nbsp;|&nbsp;");
        StringBuffer buf = new StringBuffer();
        buf.append("https://");
        buf.append(req.getServerName());
        int port = req.getServerPort();
        if (port != 80) {
            buf.append(":");
            buf.append(port);
        }
        buf.append("/ablage");
        w.write("<a id=\"permalink\" href=\"" + buf.toString() + "\">Anmeldung</a>&nbsp;|&nbsp;");
        w.write("<a id=\"permalink\" href=\"");
        w.write(documentId + "\">Permalink</a></p>");
        w.write("</td>");
        w.write("<td style=\"padding-left:20px\" width=\"20%\" id=\"dokKopf\">");
        w.write(getSearchForm());
        w.write("</td>");
        w.write("</tr>");
        w.write("</table>");
    }

    public static void seiteUnten(HttpServletRequest req, HttpServletResponse resp, ResultSet rs, String mimeType, int documentId, int depth, DataStorage ds, String userName) throws IOException, SQLException {
        Writer w = resp.getWriter();
        w.write("</div>");
        w.write("<p id=\"copyrightFooter\">");
        w.write(getCopyrightFooter());
        w.write("&nbsp;&nbsp;&bull;&nbsp;&nbsp;");
        StringBuffer buf = new StringBuffer();
        buf.append("Dokument ");
        buf.append(documentId);
        buf.append(" Version ");
        int version = rs.getInt("dc_version");
        if (version == Document.DRAFT_VERSION_ID) {
            buf.append("Entwurf");
        } else {
            buf.append(version);
        }
        buf.append("&nbsp;&nbsp;&bull;&nbsp;&nbsp;");
        buf.append(ds.getCreationTimeStrLong(rs.getString("dc_timestamp")));
        w.write(buf.toString());
        w.write("</p>");
        w.write("</body>");
        w.write("</html>");
    }

    private static void streamDown(HttpServletResponse resp, ResultSet rs, String mimeType) throws IOException, SQLException {
        resp.setContentType(mimeType);
        byte[] buf = new byte[8192];
        OutputStream stream = resp.getOutputStream();
        InputStream in = rs.getBlob("dc_content").getBinaryStream();
        int b = in.read(buf, 0, buf.length);
        while (b > -1) {
            stream.write(buf, 0, b);
            b = in.read(buf, 0, buf.length);
        }
        stream.flush();
        in.close();
    }

    public static String getCopyrightFooter() {
        return "<a id=\"copyrightFooter\" href=\"http://netzpult.de\">" + App.NAME + "</a>";
    }

    private static String getPathDepthPrefix(int depth) {
        StringBuffer buf = new StringBuffer();
        String prefix = "../";
        for (int i = 0; i < depth; i++) {
            buf.append(prefix);
        }
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest(buf.toString());
        return buf.toString();
    }

    public static void logText(String text) {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        if (logger != null) {
            logger.finest(text);
        } else {
            System.out.println("ContentServlet.logText logger is null");
        }
    }

    public static void logError(Exception e) {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        if (logger != null) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } else {
            System.out.println("ContentServlet.logError logger is null");
        }
    }
}
