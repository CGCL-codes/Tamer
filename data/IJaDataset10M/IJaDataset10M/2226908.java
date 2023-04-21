package org.marcont.portal.rest.server;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.marcont.portal.rest.Authentication;
import org.marcont.portal.user.UserManager;

/**
 *
 * @author katar
 */
public abstract class BaseServlet extends HttpServlet {

    public static final String FS = System.getProperty("file.separator");

    protected String login = "";

    protected String password = "";

    protected boolean allowUser(String auth, Authentication a) throws ServletException {
        if (auth == null) return false;
        if (!auth.toUpperCase().startsWith("BASIC ")) return false;
        String userpassEncoded = auth.substring(6);
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        String userpassDecoded;
        try {
            userpassDecoded = new String(dec.decodeBuffer(userpassEncoded));
            int separatorIndex = userpassDecoded.indexOf(':');
            login = userpassDecoded.substring(0, separatorIndex);
            password = userpassDecoded.substring(separatorIndex + 1);
            return a.authenticate(login, password);
        } catch (IOException e) {
            throw new ServletException(e.getMessage());
        }
    }

    protected void outputModel(HttpServletResponse response, Model m) {
        StringWriter sr = new StringWriter();
        PrintWriter out;
        try {
            response.setContentType("text/rdf;charset=UTF-8");
            out = response.getWriter();
            m.write(sr);
            out.println(sr.toString());
            out.println("---------------------------------------------------");
            TransformerFactory tFactory = TransformerFactory.newInstance();
            String ctx = getServletContext().getRealPath("") + FS;
            Source xmlSource = new StreamSource(new StringReader(sr.toString()));
            Source xslSource = new StreamSource(new java.net.URL("file", "", ctx + "xsl/rdf2html.xsl").openStream());
            Transformer transformer = tFactory.newTransformer(xslSource);
            transformer.transform(xmlSource, new StreamResult(out));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void message(HttpServletResponse response, String message) {
        PrintWriter out;
        try {
            out = response.getWriter();
            out.println(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void successMessage(HttpServletResponse response, int messageCode, String message) {
        response.setStatus(messageCode);
        response.setContentType("application/xml rdf");
        message(response, message);
    }

    protected void errorMessage(HttpServletResponse response, int messageCode, String message) {
        response.setStatus(messageCode);
        message(response, message);
    }

    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        execute(req, res);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        doProcess(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        doProcess(req, res);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        doProcess(req, res);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        doProcess(req, res);
    }

    protected abstract void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException;

    public String getServletInfo() {
        return "Resource State Transfer Service for MarcOnt Portal: " + this.getClass().getName();
    }
}
