package net.edwardstx;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class ProxyServlet extends HttpServlet {

    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Key for redirect location header.
     */
    private static final String LOCATION_HEADER = "Location";

    /**
     * Key for content type header.
     */
    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

    /**
     * Key for content length header.
     */
    private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";

    /**
     * Key for host header
     */
    private static final String HOST_HEADER_NAME = "Host";

    /**
     * The directory to use to temporarily store uploaded files
     */
    private static final File UPLOAD_TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

    /**
     * The host to which we are proxying requests
     */
    private String proxyHost;

    /**
     * The port on the proxy host to wihch we are proxying requests. Default value is 80.
     */
    private int proxyPort = 80;

    /**
     * The (optional) path on the proxy host to wihch we are proxying requests. Default value is "".
     */
    private String proxyPath = "";

    /**
     * The maximum size for uploaded files in bytes. Default value is 5MB.
     */
    private int maxFileUploadSize = 5 * 1024 * 1024;

    /**
     * The (optional) protocol name http or https.
     */
    private String protocol = "protocol";

    /**
     * Initialize the <code>ProxyServlet</code>
     * @param servletConfig The Servlet configuration passed in by the servlet conatiner
     */
    @Override
    public void init(ServletConfig servletConfig) {
        String newProxyHost = servletConfig.getInitParameter("proxyHost");
        if (newProxyHost == null || newProxyHost.isEmpty()) {
            throw new IllegalArgumentException("Proxy host not set, please set init-param 'proxyHost' in web.xml");
        }
        proxyHost = newProxyHost;
        String newProxyPort = servletConfig.getInitParameter("proxyPort");
        if (newProxyPort != null && !newProxyPort.isEmpty()) {
            proxyPort = Integer.parseInt(newProxyPort);
        }
        String newProxyPath = servletConfig.getInitParameter("proxyPath");
        if (newProxyPath != null && !newProxyPath.isEmpty()) {
            proxyPath = newProxyPath;
        }
        String newMaxFileUploadSize = servletConfig.getInitParameter("maxFileUploadSize");
        if (newMaxFileUploadSize != null && !newMaxFileUploadSize.isEmpty()) {
            maxFileUploadSize = Integer.parseInt(newMaxFileUploadSize);
        }
        String newProtocol = servletConfig.getInitParameter("protocol");
        if (newProtocol != null && !newProtocol.isEmpty()) {
            protocol = newProtocol;
        }
    }

    /**
     * Performs an HTTP GET request
     * @param request The {@link HttpServletRequest} object passed in by the servlet
     * engine representing the client request to be proxied
     * @param response The {@link HttpServletResponse} object by which
     * we can send a proxied response to the client 
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        GetMethod proxyRequest = new GetMethod(getProxyURL(request));
        setProxyRequestHeaders(request, proxyRequest);
        executeProxyRequest(proxyRequest, request, response);
    }

    /**
     * Performs an HTTP POST request
     * @param request The {@link HttpServletRequest} object passed
     *                            in by the servlet engine representing the
     *                            client request to be proxied
     * @param response The {@link HttpServletResponse} object by which
     *                             we can send a proxied response to the client 
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PostMethod proxyRequest = new PostMethod(getProxyURL(request));
        setProxyRequestHeaders(request, proxyRequest);
        if (ServletFileUpload.isMultipartContent(request)) {
            handleMultipartPost(proxyRequest, request);
        } else {
            handleStandardPost(proxyRequest, request);
        }
        executeProxyRequest(proxyRequest, request, response);
    }

    /**
     * Sets up the given {@link PostMethod} to send the same multipart POST
     * data as was sent in the given {@link HttpServletRequest}
     * @param proxyRequest The {@link PostMethod} that we are
     * configuring to send a multipart POST request
     * @param request The {@link HttpServletRequest} that contains
     * the mutlipart POST data to be sent via the {@link PostMethod}
     */
    @SuppressWarnings("unchecked")
    private void handleMultipartPost(PostMethod proxyRequest, HttpServletRequest request) throws ServletException {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setSizeThreshold(maxFileUploadSize);
        diskFileItemFactory.setRepository(UPLOAD_TEMP_DIRECTORY);
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        try {
            List<FileItem> listFileItems = servletFileUpload.parseRequest(request);
            List<Part> listParts = new ArrayList<Part>();
            for (FileItem fileItemCurrent : listFileItems) {
                if (fileItemCurrent.isFormField()) {
                    StringPart stringPart = new StringPart(fileItemCurrent.getFieldName(), fileItemCurrent.getString());
                    listParts.add(stringPart);
                } else {
                    FilePart filePart = new FilePart(fileItemCurrent.getFieldName(), new ByteArrayPartSource(fileItemCurrent.getName(), fileItemCurrent.get()));
                    listParts.add(filePart);
                }
            }
            MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(listParts.toArray(new Part[] {}), proxyRequest.getParams());
            proxyRequest.setRequestEntity(multipartRequestEntity);
            proxyRequest.setRequestHeader(CONTENT_TYPE_HEADER_NAME, multipartRequestEntity.getContentType());
        } catch (FileUploadException fileUploadException) {
            throw new ServletException(fileUploadException);
        }
    }

    /**
     * Sets up the given {@link PostMethod} to send the same standard POST
     * data as was sent in the given {@link HttpServletRequest}
     * @param proxyRequest The {@link PostMethod} that we are
     *                                configuring to send a standard POST request
     * @param request The {@link HttpServletRequest} that contains
     *                            the POST data to be sent via the {@link PostMethod}
     */
    private void handleStandardPost(PostMethod proxyRequest, HttpServletRequest request) {
        Map<String, String[]> mapPostParameters = request.getParameterMap();
        List<NameValuePair> listNameValuePairs = new ArrayList<NameValuePair>();
        for (String stringParameterName : mapPostParameters.keySet()) {
            String[] stringArrayParameterValues = mapPostParameters.get(stringParameterName);
            for (String stringParamterValue : stringArrayParameterValues) {
                NameValuePair nameValuePair = new NameValuePair(stringParameterName, stringParamterValue);
                listNameValuePairs.add(nameValuePair);
            }
        }
        proxyRequest.setRequestBody(listNameValuePairs.toArray(new NameValuePair[] {}));
    }

    /**
     * Executes the {@link HttpMethod} passed in and sends the proxy response
     * back to the client via the given {@link HttpServletResponse}
     * @param proxyRequest An object representing the proxy request to be made
     * @param response An object by which we can send the proxied
     * response back to the client
     * @throws IOException Can be thrown by the {@link HttpClient}.executeMethod
     * @throws ServletException Can be thrown to indicate that another error has occurred
     */
    private void executeProxyRequest(HttpMethod proxyRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpClient httpClient = new HttpClient();
        proxyRequest.setFollowRedirects(false);
        int proxyResponseCode = httpClient.executeMethod(proxyRequest);
        if (proxyResponseCode >= HttpServletResponse.SC_MULTIPLE_CHOICES && proxyResponseCode < HttpServletResponse.SC_NOT_MODIFIED) {
            String statusCode = Integer.toString(proxyResponseCode);
            String location = proxyRequest.getResponseHeader(LOCATION_HEADER).getValue();
            if (location == null) {
                throw new ServletException("Recieved status code: " + statusCode + " but no " + LOCATION_HEADER + " header was found in the response");
            }
            String myHostName = request.getServerName();
            if (request.getServerPort() != 80) {
                myHostName += ":" + request.getServerPort();
            }
            myHostName += request.getContextPath();
            response.sendRedirect(location.replace(getProxyHostAndPort() + proxyPath, myHostName));
            return;
        } else if (proxyResponseCode == HttpServletResponse.SC_NOT_MODIFIED) {
            response.setIntHeader(CONTENT_LENGTH_HEADER_NAME, 0);
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        response.setStatus(proxyResponseCode);
        Header[] responseHeaders = proxyRequest.getResponseHeaders();
        for (Header header : responseHeaders) {
            response.setHeader(header.getName(), header.getValue());
        }
        InputStream proxyResponse = proxyRequest.getResponseBodyAsStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(proxyResponse);
        OutputStream clientResponse = response.getOutputStream();
        int intNextByte;
        while ((intNextByte = bufferedInputStream.read()) != -1) {
            clientResponse.write(intNextByte);
        }
    }

    @Override
    public String getServletInfo() {
        return "Http Proxy Servlet";
    }

    /**
     * Retreives all of the headers from the servlet request and sets them on
     * the proxy request
     * 
     * @param httpServletRequest The request object representing the client's
     * request to the servlet engine
     * @param proxyRequest The request that we are about to send to
     * the proxy host
     */
    private void setProxyRequestHeaders(HttpServletRequest httpServletRequest, HttpMethod proxyRequest) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.equalsIgnoreCase(CONTENT_LENGTH_HEADER_NAME)) continue;
            Enumeration<String> headerValues = httpServletRequest.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                if (headerName.equalsIgnoreCase(HOST_HEADER_NAME)) {
                    headerValue = getProxyHostAndPort();
                }
                Header header = new Header(headerName, headerValue);
                proxyRequest.setRequestHeader(header);
            }
        }
    }

    private String getProxyURL(HttpServletRequest httpServletRequest) {
        String stringProxyURL = protocol + "://" + getProxyHostAndPort();
        if (!proxyPath.isEmpty()) {
            stringProxyURL += proxyPath;
        }
        stringProxyURL += httpServletRequest.getPathInfo();
        if (httpServletRequest.getQueryString() != null) {
            stringProxyURL += "?" + httpServletRequest.getQueryString();
        }
        return stringProxyURL;
    }

    private String getProxyHostAndPort() {
        if (proxyPort == 80) {
            return proxyHost;
        } else {
            return proxyHost + ":" + proxyPort;
        }
    }
}
