package org.gomba.contrib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.gomba.AbstractServlet;
import org.gomba.Expression;
import org.gomba.ParameterResolver;
import org.gomba.utils.xml.ContentHandlerUtils;
import org.gomba.utils.xml.ObjectInputSource;
import org.gomba.utils.xml.ObjectXMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Perform a file system operation on a directory. This servlet inherits the
 * init-params of {@link org.gomba.AbstractServlet}, plus:
 * <dl>
 * <dt>name</dt>
 * <dd>The directory to operate on. The name must begin with a "/" and is
 * interpreted as relative to the current context root. (Required)</dd>
 * <dt>http-method</dt>
 * <dd>The value can be GET. (Required)</dd>
 * </dl>
 * 
 * Note about HTTP method usage. The GET method is normally used for directory
 * listing operations.
 * 
 * @author Patrick Dreyer
 * @version $Id: FileSystemDirectoryServlet.java,v 1.1 2007/05/16 13:16:37 flaviotordini Exp $
 */
public class FileSystemDirectoryServlet extends AbstractServlet {

    private static final String INIT_PARAM_HTTP_METHOD = "http-method";

    private static final String INIT_PARAM_NAME = "name";

    private static final String ELEMENT_ROOT = "files";

    private static final String ELEMENT_ROW = "file";

    private static final String PATTERN_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * <code>true</code> if this servlet supports the GET HTTP method.
     */
    private boolean supportGet;

    private Expression name;

    /** DTD public identifier, if any. */
    private String doctypePublic;

    /** DTD system identifier, if any. */
    private String doctypeSystem;

    /** The resource MIME content type, if any. */
    private String mediaType;

    /** The parsed XSLT stylesheet, if any. */
    private Templates templates;

    /** The element names. */
    private String rootElementName, rowElementName;

    /** XSLT parameters. */
    private Map xsltFixedParameters;

    /** XSLT output properties. */
    private Properties xsltOutputProperties;

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String httpMethod = config.getInitParameter(INIT_PARAM_HTTP_METHOD);
        if (httpMethod == null) {
            throw new ServletException("Missing init-param: " + INIT_PARAM_HTTP_METHOD);
        }
        if (httpMethod.equals("GET")) this.supportGet = true; else throw new ServletException("Unsupported HTTP method: " + httpMethod);
        String name = config.getInitParameter(INIT_PARAM_NAME);
        if (name == null) throw new ServletException("Missing init-param: " + INIT_PARAM_NAME);
        try {
            if (new File(name).isAbsolute()) this.name = new Expression(name); else this.name = new Expression(getServletContext().getRealPath("/") + name);
        } catch (Exception e) {
            throw new ServletException("Error parsing name.", e);
        }
        this.doctypePublic = config.getInitParameter("doctype-public");
        this.doctypeSystem = config.getInitParameter("doctype-system");
        this.mediaType = config.getInitParameter("media-type");
        this.rootElementName = config.getInitParameter("root-element");
        if (this.rootElementName == null) this.rootElementName = ELEMENT_ROOT;
        this.rowElementName = config.getInitParameter("row-element");
        if (this.rowElementName == null) this.rowElementName = ELEMENT_ROW;
        final String xsltStyleSheet = config.getInitParameter("xslt");
        if (xsltStyleSheet != null) {
            InputStream is = getServletContext().getResourceAsStream(xsltStyleSheet);
            if (is == null) throw new ServletException("Cannot find stylesheet: " + xsltStyleSheet);
            try {
                TransformerFactory tfactory = TransformerFactory.newInstance();
                Source xslSource = new StreamSource(is);
                xslSource.setSystemId(getServletContext().getRealPath(xsltStyleSheet));
                this.templates = tfactory.newTemplates(xslSource);
            } catch (TransformerConfigurationException tce) {
                throw new ServletException("Error parsing XSLT stylesheet: " + xsltStyleSheet, tce);
            }
            final String xsltParams = config.getInitParameter("xslt-params");
            if (xsltParams != null) try {
                this.xsltFixedParameters = stringToProperties(xsltParams);
            } catch (Exception e) {
                throw new ServletException("Error parsing XSLT params: " + xsltParams, e);
            }
            final String xsltOutputProperties = config.getInitParameter("xslt-output-properties");
            if (xsltOutputProperties != null) try {
                this.xsltOutputProperties = stringToProperties(xsltOutputProperties);
            } catch (Exception e) {
                throw new ServletException("Error parsing XSLT output properties: " + xsltOutputProperties, e);
            }
        }
    }

    /**
     * @throws IOException
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!this.supportGet) response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        final long startTime = System.currentTimeMillis();
        final ParameterResolver parameterResolver = new ParameterResolver(request);
        File directory = resolveName(parameterResolver);
        if (!directory.isDirectory()) throw new ServletException("Is not a directory.");
        File[] files = directory.listFiles();
        try {
            ObjectXMLReader saxReader = new FileListXMLReader();
            serializeXML(files, saxReader, response);
            response.setStatus(getHttpStatusCode());
        } catch (Exception e) {
            throw new ServletException("Error rendering results.", e);
        } finally {
            String msg = getProfilingMessage(request, startTime);
            if (msg != null) {
                log(msg);
            }
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    protected final File resolveName(ParameterResolver parameterResolver) throws ServletException {
        try {
            return new File(this.name.replaceParameters(parameterResolver).toString());
        } catch (Exception e) {
            throw new ServletException("Error setting name.", e);
        }
    }

    /**
     * Serialize an object to XML using SAX and TrAX APIs in a smart way.
     * 
     * @param object
     *            The object to serialize
     * @param saxReader
     *            The SAX "parser"
     * @param response
     *            The HTTP response
     * @see <a
     *      href="http://java.sun.com/j2se/1.4.2/docs/api/javax/xml/transform/package-summary.html">TrAX
     *      API </a>
     */
    private void serializeXML(Object object, ObjectXMLReader saxReader, HttpServletResponse response) throws Exception {
        String mediaType = null;
        if (this.mediaType != null) {
            mediaType = this.mediaType;
        }
        if (mediaType == null && this.xsltOutputProperties != null) {
            mediaType = this.xsltOutputProperties.getProperty(OutputKeys.MEDIA_TYPE);
        }
        if (mediaType == null && this.templates != null) {
            mediaType = this.templates.getOutputProperties().getProperty(OutputKeys.MEDIA_TYPE);
        }
        if (mediaType == null) {
            mediaType = "text/xml";
        }
        response.setContentType(mediaType);
        Transformer t;
        if (this.templates != null) {
            t = this.templates.newTransformer();
            if (this.xsltFixedParameters != null) {
                for (Iterator i = this.xsltFixedParameters.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry mapEntry = (Map.Entry) i.next();
                    t.setParameter((String) mapEntry.getKey(), mapEntry.getValue());
                }
            }
        } else {
            t = TransformerFactory.newInstance().newTransformer();
        }
        if (this.doctypePublic != null) {
            t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, this.doctypePublic);
        }
        if (this.doctypeSystem != null) {
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, this.doctypeSystem);
        }
        if (this.xsltOutputProperties != null) {
            t.setOutputProperties(this.xsltOutputProperties);
        }
        String preferredEncoding = t.getOutputProperties().getProperty(OutputKeys.ENCODING);
        if (preferredEncoding != null) {
            response.setCharacterEncoding(preferredEncoding);
        }
        InputSource inputSource = new ObjectInputSource(object);
        Source source = new SAXSource(saxReader, inputSource);
        Result result = new StreamResult(response.getOutputStream());
        t.transform(source, result);
    }

    private static Properties stringToProperties(String string) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new ByteArrayInputStream(string.getBytes());
        try {
            properties.load(inputStream);
        } finally {
            inputStream.close();
        }
        return properties;
    }

    /**
     * This SAX XMLReader generates an XML document from a file list.
     */
    final class FileListXMLReader extends ObjectXMLReader {

        /**
         * @see org.gomba.utils.xml.ObjectXMLReader#parse(org.gomba.utils.xml.ObjectInputSource)
         */
        public void parse(ObjectInputSource input) throws IOException, SAXException {
            SimpleDateFormat timestampFormatter = new SimpleDateFormat(PATTERN_TIMESTAMP);
            File[] files = (File[]) input.getObject();
            this.handler.startDocument();
            this.handler.startElement(ContentHandlerUtils.DUMMY_NSU, FileSystemDirectoryServlet.this.rootElementName, FileSystemDirectoryServlet.this.rootElementName, ContentHandlerUtils.DUMMY_ATTS);
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (FileSystemDirectoryServlet.this.rowElementName.length() > 0) {
                    this.handler.startElement(ContentHandlerUtils.DUMMY_NSU, FileSystemDirectoryServlet.this.rowElementName, FileSystemDirectoryServlet.this.rowElementName, ContentHandlerUtils.DUMMY_ATTS);
                }
                ContentHandlerUtils.tag(this.handler, "name", file.getName());
                ContentHandlerUtils.tag(this.handler, "size", Long.toString(file.length()));
                long timestamp = file.lastModified();
                String d = timestampFormatter.format(new Date(timestamp));
                ContentHandlerUtils.tag(this.handler, "lastModified", d);
                if (FileSystemDirectoryServlet.this.rowElementName.length() > 0) {
                    this.handler.endElement(ContentHandlerUtils.DUMMY_NSU, FileSystemDirectoryServlet.this.rowElementName, FileSystemDirectoryServlet.this.rowElementName);
                }
            }
            this.handler.endElement(ContentHandlerUtils.DUMMY_NSU, FileSystemDirectoryServlet.this.rootElementName, FileSystemDirectoryServlet.this.rootElementName);
            this.handler.endDocument();
        }
    }
}
