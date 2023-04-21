package org.fao.waicent.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.fao.waicent.util.XMLRepository;
import org.w3c.dom.Document;

public class BaseServlet extends HttpServlet {

    protected boolean debug = false;

    protected String errorPage = null;

    protected String defaultPage = null;

    protected String loginPage = null;

    private static XMLRepository xmlrepo = null;

    private static Hashtable htRep = new Hashtable();

    private static Vector LANGUAGES = new Vector();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        errorPage = findInitParameter("errorPage", "error.jsp");
        defaultPage = findInitParameter("defaultPage", "search.jspx");
        loginPage = findInitParameter("loginPage", "login.jspx");
        try {
            initRepository(getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Enumeration getNames() {
        return xmlrepo.getNames();
    }

    public XMLRepository getRepository() {
        return xmlrepo;
    }

    private void initRepository(String path) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException {
        for (int i = 0; i < LANGUAGES.size(); i++) {
            XMLRepository x = new XMLRepository(path + (String) LANGUAGES.elementAt(i));
            htRep.put((String) LANGUAGES.elementAt(i), x);
        }
    }

    public org.w3c.dom.Element getRoot(String lang, String filename) {
        if (lang == null || lang == "") {
            lang = "en";
        }
        return xmlrepo.get(lang + "." + filename);
    }

    private String getPath() {
        return getServletContext().getRealPath(File.separator);
    }

    public org.w3c.dom.Element getFileAsElementToRepository(String language_code, String filename) {
        XMLRepository rep = (XMLRepository) htRep.get(language_code);
        org.w3c.dom.Element file = null;
        if (rep != null) {
            file = rep.get(filename);
        } else {
            throw new IllegalArgumentException("Language not recognized: " + language_code);
        }
        return file;
    }

    protected String findInitParameter(String name, String default_value) {
        String value = findInitParameter(name);
        if (value == null) {
            value = default_value;
        }
        return value;
    }

    protected String findInitParameter(String name) {
        String value = getServletConfig().getInitParameter(name);
        return value;
    }

    public void debug(String msg) {
        if (debug) {
            getServletContext().log(msg);
        }
    }

    public static String getResponsePage(HttpServletRequest request) {
        return (String) request.getAttribute("ResponsePage");
    }

    public static void setResponsePage(HttpServletRequest request, String responsePage) {
        request.setAttribute("ResponsePage", responsePage);
    }

    protected boolean isRequestValid(HttpServletRequest request) {
        if (getRequestException(request) instanceof RequestValidationException) {
            return false;
        } else {
            return true;
        }
    }

    void setRequestException(HttpServletRequest request, RequestException e) {
        request.setAttribute("RequestException", e);
    }

    protected RequestException getRequestException(HttpServletRequest request) {
        return (RequestException) request.getAttribute("RequestException");
    }

    protected void executeActionList(String actionNameList[], HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (int i = 0; i < actionNameList.length; i++) {
            executeAction(actionNameList[i], request, response);
        }
    }

    public Document getXMLResponse(HttpServletRequest request) {
        return (Document) request.getAttribute("XML");
    }

    public void setXMLResponse(HttpServletRequest request, Document xml) {
        request.setAttribute("XML", xml);
    }

    protected void executeAction(String actionName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (actionName != null) {
            if (debug) {
                log("begin: executeAction: " + actionName);
            }
            request.getRequestDispatcher(actionName).include(request, new NullResponseWrapper((HttpServletResponse) response));
            if (debug) {
                log("end: executeAction: " + actionName);
            }
        }
    }

    protected class ByteArrayServletStream extends ServletOutputStream {

        ByteArrayOutputStream _buf = null;

        ByteArrayServletStream() {
            super();
            _buf = new ByteArrayOutputStream();
        }

        public void writeTo(OutputStream out) throws IOException {
            out.write(_buf.toByteArray());
        }

        public void write(int param) throws IOException {
            _buf.write(param);
        }

        public void write(byte buf[]) throws IOException {
            _buf.write(buf);
        }

        public void write(byte buf[], int off, int len) throws IOException {
            _buf.write(buf, off, len);
        }

        public byte[] toByteArray() {
            return _buf.toByteArray();
        }

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return _buf;
        }
    }

    protected class ControllerResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayServletStream sos = null;

        private PrintWriter pw = null;

        public ControllerResponseWrapper(HttpServletResponse response) {
            super(response);
            try {
                sos = new ByteArrayServletStream();
                pw = new PrintWriter(new OutputStreamWriter(sos.getByteArrayOutputStream(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }

        public PrintWriter getWriter() {
            return pw;
        }

        public ServletOutputStream getOutputStream() {
            return sos;
        }

        public byte[] toByteArray() {
            return sos.toByteArray();
        }

        public void setContentType(String type) {
            if (type.equals("text/xml")) {
            } else {
            }
        }

        public void setContentLength(int len) {
        }

        public void flushBuffer() throws java.io.IOException {
        }

        public boolean isCommitted() {
            return false;
        }
    }

    protected class NullResponseWrapper extends HttpServletResponseWrapper {

        public NullResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        public PrintWriter getWriter() throws java.io.IOException {
            throw new IOException("NullResponseWrapper: getWriter may NOT be called !");
        }

        public ServletOutputStream getOutputStream() throws java.io.IOException {
            throw new IOException("NullResponseWrapper: getOutputStream may NOT be called !");
        }
    }

    public static String toHTML(String str) {
        if (null == str) {
            return str;
        }
        if (str.length() == 0) {
            return str;
        }
        StringBuffer html_strbuf = new StringBuffer(str);
        for (int i = 0; i < html_strbuf.length(); i++) {
            switch(html_strbuf.charAt(i)) {
                case '<':
                    html_strbuf.setCharAt(i, ';');
                    html_strbuf.insert(i, "&lt");
                    i += 3;
                    break;
                case '>':
                    html_strbuf.setCharAt(i, ';');
                    html_strbuf.insert(i, "&gt");
                    i += 3;
                    break;
                case '&':
                    html_strbuf.setCharAt(i, ';');
                    html_strbuf.insert(i, "&amp");
                    i += 4;
                    break;
                case '"':
                    html_strbuf.setCharAt(i, ';');
                    html_strbuf.insert(i, "&quot");
                    i += 5;
                    break;
                default:
                    break;
            }
        }
        return new String(html_strbuf);
    }

    public static void setLanguages(Vector language_options) {
        LANGUAGES = language_options;
    }

    public static Vector getLanguages() {
        return LANGUAGES;
    }
}
