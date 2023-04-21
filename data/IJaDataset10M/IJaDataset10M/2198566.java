package com.ecs.soap.proxy.servlets;

import static com.ecs.soap.proxy.util.Utils.nodeToString;
import static com.ecs.soap.proxy.util.Utils.parseXMLRootElement;
import static com.ecs.soap.proxy.util.Utils.parseXMLSchemaNode;
import static com.ecs.soap.proxy.util.Utils.schemaFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.ecs.soap.proxy.config.Configuration;
import com.ecs.soap.proxy.handler.TargetResponseHandler;
import com.ecs.soap.proxy.result.ResultsManager;
import com.ecs.soap.proxy.result.model.CallResult;
import com.ecs.soap.proxy.result.model.CallResult.Status;
import com.ecs.soap.proxy.util.Base64Coder;

public class SOAPServlet extends HttpServlet {

    /**
	 *
	 */
    private static final long serialVersionUID = -4636110643101878500L;

    private static Logger logger = Logger.getLogger(SOAPServlet.class);

    public static final String servletContextPath = "/soap";

    private static final String ENV_BEGIN_PATTERN = "(^<[a-zA-Z]+:[e|E]nvelope>.*)|(^<[e|E]nvelope>.*)";

    private static final String ENV_END_PATTERN = "(^.*</[a-zA-Z]+:[e|E]nvelope>)|(^.*</[e|E]nvelope>)";

    private static final String MULTIPART_RELATED = "multipart/related";

    private static final String XOP_XML = "application/xop+xml";

    private static final String XOP_INCLUDE_PATTERN = "(?s)((<xop:[i|I]nclude.*?/>)|(<xop:[i|I]nclude.*?</xop:[i|I]nclude>))";

    private static final int BAD_REQUEST_CODE = 400;

    private static final int INTERNAL_ERROR_CODE = 500;

    private static final String BINARY_REPLACEMENT = Base64Coder.encodeString("*** binary content ignored ***");

    private Configuration config;

    private ResultsManager stats;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String targetURI = requestURI.substring((contextPath + servletContextPath).length());
        String requestContentType = req.getContentType();
        if (requestContentType == null) {
            requestContentType = "";
        }
        if (targetURI.endsWith("/")) {
            targetURI = targetURI.substring(0, targetURI.length() - 1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("new request received ...");
            logger.debug("request URI : " + requestURI);
            logger.debug("target URI : " + targetURI);
            logger.debug("content type : " + requestContentType);
        }
        CallResult callResult = new CallResult(targetURI);
        URL targetURL = null;
        try {
            targetURL = fetchTargetURL(targetURI);
        } catch (IOException e) {
            logger.error("failed to read uri mapping file !", e);
            callResult.setRequestStatus(Status.KO);
            callResult.addRequestDetailedError(e);
            this.stats.addResult(callResult);
            resp.sendError(INTERNAL_ERROR_CODE, e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            logger.error("uri mapping problem" + targetURI, e);
            callResult.setRequestStatus(Status.KO);
            callResult.addRequestDetailedError(e);
            this.stats.addResult(callResult);
            resp.sendError(INTERNAL_ERROR_CODE, e.getMessage());
            return;
        }
        String queryString = req.getQueryString();
        if (logger.isDebugEnabled()) {
            logger.debug("Query string is: " + req.getQueryString());
        }
        if (!StringUtils.isEmpty(queryString) && queryString.trim().equalsIgnoreCase("wsdl")) {
            String forward = targetURL.toExternalForm() + "?wsdl";
            if (logger.isDebugEnabled()) {
                logger.debug("wsdl requested, forwarding request to " + forward);
            }
            resp.sendRedirect(forward);
            return;
        }
        byte[] requestBody = this.readHttpRequestBody(req);
        String requestXMLBody = extractSoapEnvelope(requestBody);
        if (requestContentType.contains(MULTIPART_RELATED) && requestContentType.contains(XOP_XML)) {
            logger.debug("MTOM detected, removing <xop:Include .../> declarations from XML");
            requestXMLBody = requestXMLBody.replaceAll(XOP_INCLUDE_PATTERN, BINARY_REPLACEMENT);
        }
        callResult.setSoapRequest(requestXMLBody);
        if (logger.isTraceEnabled()) {
            logger.trace("request XML body : \n" + requestXMLBody);
        }
        Schema schema = null;
        try {
            schema = fetchSchema(targetURI);
        } catch (IOException e) {
            logger.warn("failed to read schema mapping file", e);
            callResult.addRequestDetailedError(e);
        }
        if (schema == null) {
            logger.warn("no schema is mapped to URI " + targetURI + " or schema file is wrong, skipping validation");
            callResult.addRequestDetailedError("no schema is mapped to URI " + targetURI + " or schema file is wrong, skipping validation");
        } else {
            try {
                Element root = parseXMLRootElement(requestXMLBody);
                String soapEnvNamespace = "http://schemas.xmlsoap.org/soap/envelope/";
                Node bodyNode = root.getElementsByTagNameNS(soapEnvNamespace, "Body").item(0);
                Node requestNode = bodyNode.getFirstChild();
                try {
                    String requestContent = nodeToString(requestNode);
                    try {
                        if (logger.isDebugEnabled()) {
                            StringBuilder message = new StringBuilder("validating SOAP request content");
                            message.append(":\n");
                            message.append(requestContent);
                            logger.debug(message);
                        }
                        schema.newValidator().validate(new SAXSource(new InputSource(new StringReader(requestContent))));
                        logger.debug("content is ok.");
                    } catch (Exception e) {
                        logger.debug("SOAP request content is not valid", e);
                        callResult.setRequestStatus(Status.KO);
                        callResult.addRequestDetailedError(e);
                        this.stats.addResult(callResult);
                        resp.sendError(BAD_REQUEST_CODE, e.getMessage());
                        return;
                    }
                } catch (TransformerException e) {
                    logger.warn("failed to print SOAP request content, skipping validation", e);
                    callResult.addRequestDetailedError(e);
                }
            } catch (ParserConfigurationException e) {
                logger.warn("failed to read SOAP request content, skipping validation", e);
                callResult.addRequestDetailedError(e);
            } catch (SAXException e) {
                logger.warn("failed to read SOAP request content, skipping validation", e);
                callResult.addRequestDetailedError(e);
            } catch (IOException e) {
                logger.warn("failed to read SOAP request content, skipping validation", e);
                callResult.addRequestDetailedError(e);
            }
        }
        TargetResponseHandler responseHandler = null;
        try {
            long begin = System.currentTimeMillis();
            responseHandler = this.callTargetEndpoint(req, targetURL, requestBody);
            callResult.setResponseTime(new Long(System.currentTimeMillis() - begin));
        } catch (IOException e) {
            logger.debug("failed to invoke target endpoint", e);
            callResult.setResponseStatus(Status.KO);
            callResult.addResponseDetailedError(e);
            this.stats.addResult(callResult);
            resp.sendError(INTERNAL_ERROR_CODE, e.getMessage());
            return;
        }
        int responseCode = responseHandler.getResponseCode();
        if (responseCode != 200) {
            if (logger.isDebugEnabled()) {
                logger.debug("target endpoint did not return error code 200 (actual=" + responseCode + ")");
            }
            callResult.setResponseStatus(Status.KO);
            callResult.addResponseDetailedError("target endpoint did not return error code 200 (actual=" + responseCode + ")");
            this.stats.addResult(callResult);
            resp.sendError(responseCode, responseHandler.getResponseMessage());
            return;
        }
        String responseContentType = responseHandler.getContentType();
        if (responseContentType == null) {
            responseContentType = "";
        }
        byte[] responseBody = responseHandler.getResponseBody();
        if (responseBody != null && responseBody.length > 0) {
            String responseXMLBody = extractSoapEnvelope(responseBody);
            if (responseContentType.contains(MULTIPART_RELATED) && responseContentType.contains(XOP_XML)) {
                logger.debug("MTOM detected, removing <xop:Include .../> declaration from XML");
                responseXMLBody = responseXMLBody.replaceAll(XOP_INCLUDE_PATTERN, BINARY_REPLACEMENT);
            }
            callResult.setSoapResponse(responseXMLBody);
            if (logger.isTraceEnabled()) {
                logger.trace("request XML body : \n" + responseXMLBody);
            }
            if (schema == null) {
                logger.warn("no schema is mapped to URI " + targetURI + " or schema file is wrong, skipping validation");
                callResult.addResponseDetailedError("no schema is mapped to URI " + targetURI + " or schema file is wrong, skipping validation");
            } else {
                try {
                    Element root = parseXMLRootElement(responseXMLBody);
                    String soapEnvNamespace = "http://schemas.xmlsoap.org/soap/envelope/";
                    Node bodyNode = root.getElementsByTagNameNS(soapEnvNamespace, "Body").item(0);
                    Node responseNode = bodyNode.getFirstChild();
                    try {
                        String responseContent = nodeToString(responseNode);
                        try {
                            if (logger.isDebugEnabled()) {
                                StringBuilder message = new StringBuilder("validating SOAP response content");
                                message.append(":\n");
                                message.append(responseContent);
                                logger.debug(message);
                            }
                            schema.newValidator().validate(new SAXSource(new InputSource(new StringReader(responseContent))));
                            logger.debug("content is ok.");
                        } catch (Exception e) {
                            logger.debug("SOAP response content is not valid", e);
                            callResult.setResponseStatus(Status.KO);
                            callResult.addResponseDetailedError(e);
                            this.stats.addResult(callResult);
                            resp.sendError(BAD_REQUEST_CODE, e.getMessage());
                            return;
                        }
                    } catch (TransformerException e) {
                        logger.warn("failed to print SOAP response content, skipping validation", e);
                        callResult.addResponseDetailedError(e);
                    }
                } catch (ParserConfigurationException e) {
                    logger.warn("failed to read SOAP response content, skipping validation", e);
                    callResult.addResponseDetailedError(e);
                } catch (SAXException e) {
                    logger.warn("failed to read SOAP response content, skipping validation", e);
                    callResult.addResponseDetailedError(e);
                } catch (IOException e) {
                    logger.warn("failed to read SOAP response content, skipping validation", e);
                    callResult.addResponseDetailedError(e);
                }
            }
        }
        logger.debug("forwarding target response to client ...");
        this.addResponseHeaders(resp, responseHandler);
        if (responseBody != null && responseBody.length > 0) {
            resp.setContentLength(responseBody.length);
            resp.getOutputStream().write(responseBody);
            resp.getOutputStream().close();
        }
        this.stats.addResult(callResult);
        logger.debug("done.");
        return;
    }

    private String extractSoapEnvelope(byte[] body) throws IOException {
        StringBuilder requestXMLBuf = new StringBuilder();
        BufferedReader xmlRequestReader = new BufferedReader(new StringReader(new String(body)));
        boolean beginFound = false;
        boolean endFound = false;
        String requestLine = null;
        while (!beginFound && !endFound && (requestLine = xmlRequestReader.readLine()) != null) {
            if (requestLine.matches(ENV_BEGIN_PATTERN) && requestLine.matches(ENV_END_PATTERN)) {
                requestXMLBuf.append(requestLine);
                break;
            }
            if (requestLine.matches(ENV_BEGIN_PATTERN)) {
                requestXMLBuf.append(requestLine);
                beginFound = true;
                continue;
            }
            if (requestLine.matches(ENV_END_PATTERN)) {
                requestXMLBuf.append(requestLine);
                endFound = true;
                continue;
            }
            if (beginFound) {
                requestXMLBuf.append(requestLine);
            }
        }
        return requestXMLBuf.toString();
    }

    public URL fetchTargetURL(String uri) throws IOException {
        URL targetURL = null;
        Properties mapping = new Properties();
        mapping.load(new FileInputStream(this.config.getUriMappingFile()));
        String urlAsString = null;
        if (mapping.containsKey(uri)) {
            urlAsString = mapping.getProperty(uri);
            if (StringUtils.isEmpty(urlAsString)) {
                throw new IllegalArgumentException("target URL for URI " + uri + " is empty");
            } else {
                try {
                    targetURL = new URL(urlAsString);
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException("target URL " + urlAsString + " is not valid for URI " + uri);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("found target URL " + urlAsString + " for URI " + uri);
            }
        } else {
            throw new IllegalArgumentException("no target URL found for URI " + uri);
        }
        return targetURL;
    }

    public Schema fetchSchema(String uri) throws IOException {
        Schema schema = null;
        Properties mapping = new Properties();
        mapping.load(new FileInputStream(this.config.getSchemaMappingFile()));
        String schemaFileNames = null;
        if (mapping.containsKey(uri)) {
            schemaFileNames = mapping.getProperty(uri);
            if (StringUtils.isEmpty(schemaFileNames)) {
                throw new IllegalArgumentException("no schema declared for URI " + uri);
            } else {
                List<Source> schemaSources = new LinkedList<Source>();
                String[] schemaFileTokens = schemaFileNames.split(",");
                for (String schemaFileName : schemaFileTokens) {
                    File schemaFile = new File(this.config.getXsdDir(), schemaFileName);
                    if (schemaFile.exists() || schemaFile.isFile() || schemaFile.canRead()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("found schema " + schemaFile.getAbsolutePath() + " for URI " + uri);
                        }
                        try {
                            Node schemaNode = parseXMLSchemaNode(schemaFile);
                            try {
                                String schemaContent = nodeToString(schemaNode);
                                if (logger.isTraceEnabled()) {
                                    logger.trace("schema content: \n" + schemaContent);
                                }
                                schemaSources.add(new SAXSource(new InputSource(new StringReader(schemaContent))));
                            } catch (TransformerException e) {
                                logger.debug("failed to print schema content", e);
                            }
                        } catch (SAXException e) {
                            logger.warn("a problem occured while retrieving schema from file " + schemaFile.getAbsolutePath(), e);
                        } catch (ParserConfigurationException e) {
                            logger.warn("a problem occured while retrieving schema from file " + schemaFile.getAbsolutePath(), e);
                        }
                    } else {
                        throw new IOException("failed to acces schema file " + schemaFile.getAbsolutePath());
                    }
                }
                if (!schemaSources.isEmpty()) {
                    Source[] schemaSourcesAsArray = new Source[schemaSources.size()];
                    schemaSources.toArray(schemaSourcesAsArray);
                    try {
                        schema = schemaFactory.newSchema(schemaSourcesAsArray);
                    } catch (SAXException e) {
                        logger.warn("an error occured while building schema from Sources", e);
                    }
                }
            }
        }
        return schema;
    }

    private byte[] readHttpRequestBody(HttpServletRequest req) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream reqIS = req.getInputStream();
        int readed;
        while ((readed = reqIS.read()) != -1) {
            baos.write(readed);
        }
        baos.close();
        byte[] requestBody = baos.toByteArray();
        return requestBody;
    }

    private byte[] readHttpConnResponseBody(HttpURLConnection resp) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream respIS = resp.getInputStream();
        int readed;
        while ((readed = respIS.read()) != -1) {
            baos.write(readed);
        }
        baos.close();
        byte[] requestBody = baos.toByteArray();
        return requestBody;
    }

    @SuppressWarnings("unchecked")
    private void addRequestHeaders(HttpServletRequest req, HttpURLConnection httpConn) {
        for (Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements(); ) {
            String headerName = e.nextElement();
            String headerValue = req.getHeader(headerName);
            if (headerName != null && !headerName.toLowerCase().equals("transfer-encoding")) {
                if (logger.isTraceEnabled()) {
                    logger.trace("adding header (" + headerName + "=" + headerValue + ") to request");
                }
                httpConn.addRequestProperty(headerName, headerValue);
            }
        }
    }

    private void addResponseHeaders(HttpServletResponse resp, TargetResponseHandler responseHandler) {
        for (Entry<String, List<String>> respHeader : responseHandler.getHeaders().entrySet()) {
            String headerName = respHeader.getKey();
            List<String> headerValues = respHeader.getValue();
            for (String headerValue : headerValues) {
                if (headerName != null && !headerName.toLowerCase().equals("transfer-encoding")) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("adding header (" + headerName + "=" + headerValue + ") to response");
                    }
                    resp.addHeader(headerName, headerValue);
                }
            }
        }
    }

    private TargetResponseHandler callTargetEndpoint(HttpServletRequest req, URL targetURL, byte[] requestBody) throws IOException {
        logger.debug("sending request to target endpoint ...");
        HttpURLConnection httpConn = (HttpURLConnection) targetURL.openConnection();
        httpConn.setDoOutput(false);
        this.addRequestHeaders(req, httpConn);
        httpConn.setRequestMethod(req.getMethod());
        httpConn.setDoOutput(true);
        httpConn.getOutputStream().write(requestBody);
        httpConn.getOutputStream().close();
        TargetResponseHandler responseHandler = new TargetResponseHandler();
        int responseCode = httpConn.getResponseCode();
        String responseMessage = httpConn.getResponseMessage();
        if (logger.isDebugEnabled()) {
            logger.debug("target response code: " + responseCode);
            logger.debug("target response message: " + responseMessage);
        }
        responseHandler.setResponseCode(httpConn.getResponseCode());
        responseHandler.setResponseMessage(responseMessage);
        responseHandler.setHeaders(httpConn.getHeaderFields());
        responseHandler.setContentType(httpConn.getContentType());
        try {
            responseHandler.setResponseBody(this.readHttpConnResponseBody(httpConn));
        } catch (IOException e) {
            logger.debug("Failed to read target response body", e);
        }
        return responseHandler;
    }

    @Override
    public void init() throws ServletException {
        logger.trace("SOAPServlet.init()");
        this.config = Configuration.getInstance();
        this.stats = ResultsManager.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
