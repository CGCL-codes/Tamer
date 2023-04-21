package org.fcrepo.server.security.jaas;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.security.auth.Subject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.fcrepo.server.security.jaas.util.DataUtils;
import org.fcrepo.server.security.jaas.util.SubjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet produces an XML representation of a currently logged in user.
 * The User is logged in if they have authenticated using the JAAS
 * authentication filter. The XML representation is the same as that of the
 * XMLUsersFile that is used by fedora by default. This servlet provides a
 * technology agnostic way over providing applications with user attributes that
 * might be required for performing various user-centric activities like sending
 * them emails. This servlet should always be protected by the filter for
 * authentication.
 *
 * @author nish.naidoo@gmail.com
 */
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = -6735420697733987757L;

    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    private static final String SESSION_SUBJECT_KEY = "javax.security.auth.subject";

    private DocumentBuilder documentBuilder = null;

    @Override
    public void init() throws ServletException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            logger.error("Unable to initialise UserServlet: " + pce.getMessage(), pce);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Subject subject = (Subject) request.getSession().getAttribute(SESSION_SUBJECT_KEY);
        java.security.Principal principal = request.getUserPrincipal();
        String userId = null;
        if (principal == null || principal.getName() == null || "".equals(principal.getName())) {
            userId = "anonymous";
        } else {
            userId = principal.getName();
        }
        Document doc = documentBuilder.newDocument();
        doc.setXmlVersion("1.0");
        Element root = doc.createElement("user");
        root.setAttribute("id", userId);
        doc.appendChild(root);
        Map<String, Set<String>> attributes = SubjectUtils.getAttributes(subject);
        if (attributes != null && attributes.size() > 0) {
            for (String attr : attributes.keySet()) {
                Element attribute = doc.createElement("attribute");
                attribute.setAttribute("name", attr);
                root.appendChild(attribute);
                for (String value : attributes.get(attr)) {
                    Element v = doc.createElement("value");
                    v.appendChild(doc.createTextNode(value));
                    attribute.appendChild(v);
                }
            }
        }
        byte[] output = null;
        try {
            output = DataUtils.format(doc).getBytes();
        } catch (Exception e) {
            logger.error("Error obtaining user information: " + e.getMessage(), e);
        }
        response.setContentType("text/xml");
        response.setContentLength(output.length);
        OutputStream out = response.getOutputStream();
        out.write(output);
        out.flush();
        out.close();
    }
}
