package org.jmage.mapper;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jmage.ApplicationContext;
import org.jmage.ImageRequest;
import org.jmage.util.ExceptionUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Maps ImageRequests based on queryString params.
 */
public class ServletMapper extends HttpServlet {

    public static final String CHAINPARAM_DELIMITER = "^.*__.*$";

    public static final String CHAIN_DELIMITER = "^.*--.*$";

    protected static Logger log = Logger.getLogger(ServletMapper.class.getName());

    protected static ApplicationContext context;

    private static final String CHAIN_REGEX = "[Cc][Hh][Aa][Ii][Nn]";

    private static final String IMAGE_REGEX = "[Ii][Mm][Aa][Gg][Ee]";

    private static final String IMAGE_URI_REGEX = "[Ii][Mm][Aa][Gg][Ee]_[Uu][Rr][Ii]";

    private static final String SRC_REGEX = "[Ss][Rr][Cc]";

    private static final String URI_REGEX = "^.*[Uu][Rr][Ii]$";

    private static final String CHAINURISCHEME = "chain:";

    private static final String ENCODE = "encode";

    private static final String SERVLET_CONTEXT = "SERVLET_CONTEXT";

    private static final String SLASH = "/";

    private static final String DOT = ".";

    private static final String COLON = ":";

    private static final String CONTENT_DISPOSITION = "Content-disposition";

    private static final String FILENAME = "filename=image_";

    private static final String MAP_ERROR = "unable to map image request, cause: ";

    private static final String SOCKET_RESET_ERROR = "discarding request, connection reset by peer, cause: ";

    private static final String FILE = "file";

    protected static final String TOMCAT_CLIENTABORT = "ClientAbortException";

    protected ThreadLocalServletImageRequestMap requestMap;

    private static final String EMPTY_STRING = "";

    protected String chainParamDelimiter;

    protected String chainDelimiter;

    public ServletMapper() {
        super();
        requestMap = new ThreadLocalServletImageRequestMap();
    }

    public void init() throws ServletException {
        if (context == null) {
            context = ApplicationContext.getContext();
        }
        if (context.get(SERVLET_CONTEXT) == null) {
            context.put(SERVLET_CONTEXT, this.getServletContext());
        }
        chainParamDelimiter = context.getProperty("chainParamDelimiter", CHAINPARAM_DELIMITER);
        chainDelimiter = context.getProperty("chainDelimiter", CHAIN_DELIMITER);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            synchronized (this) {
                requestMap.setRequest(request);
                requestMap.setResponse(response);
                ImageRequestMapper imageRequestMapper = this.getImageRequestMapper();
                ImageRequest imageRequest = this.populateImageRequestFrom(request);
                imageRequestMapper.setImageRequest(imageRequest);
                requestMap.setMapper(imageRequestMapper);
            }
            requestMap.getResponse().setContentType(this.getServletContext().getMimeType(DOT + requestMap.getThreadLocalImageRequest().getEncodingFormat()));
            requestMap.getResponse().setHeader(CONTENT_DISPOSITION, FILENAME + requestMap.getThreadLocalImageRequest().hashCode() + DOT + requestMap.getThreadLocalImageRequest().getEncodingFormat());
            requestMap.getResponse().getOutputStream().write(requestMap.getMapper().processRequest());
            requestMap.getResponse().getOutputStream().flush();
        } catch (Exception e) {
            String message = MAP_ERROR + e.getMessage();
            if (new ExceptionUtil().hasMessage(e, TOMCAT_CLIENTABORT)) {
                if (log.isEnabledFor(Priority.DEBUG)) log.debug(SOCKET_RESET_ERROR + message);
            } else {
                if (log.isEnabledFor(Priority.ERROR)) log.error(message);
            }
        } finally {
            synchronized (this) {
                requestMap.removeRequest();
                requestMap.removeResponse();
                requestMap.removeMapper();
            }
        }
    }

    public ImageRequestMapper getImageRequestMapper() {
        return new ImageRequestMapper();
    }

    protected ImageRequest populateImageRequestFrom(HttpServletRequest request) throws URISyntaxException {
        ImageRequest imageRequest = new ImageRequest();
        Properties filterChainProperties = new Properties();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = ((String) paramNames.nextElement());
            String value = (String) request.getParameter(key);
            if (key.matches(URI_REGEX) || key.matches(IMAGE_REGEX) || key.matches(IMAGE_URI_REGEX) || key.matches(SRC_REGEX)) {
                String scheme = URI.create(value).getScheme();
                if (scheme == null || scheme.length() == 0) {
                    value = this.completeUri(value).toString();
                }
            }
            if (key.matches(CHAIN_REGEX)) {
                this.fillChainParam(key, value, imageRequest);
                continue;
            }
            if (key.matches(IMAGE_REGEX) || key.matches(IMAGE_URI_REGEX) || key.matches(SRC_REGEX)) {
                this.fillImageParam(key, value, imageRequest);
                continue;
            }
            if (key.matches(chainParamDelimiter)) {
                this.fillFilterChainProperties(key, value, filterChainProperties);
            } else {
                filterChainProperties.setProperty(key.toUpperCase(), value);
            }
        }
        String encode = request.getParameter(ENCODE);
        if (encode != null) {
            imageRequest.setEncodingFormat(encode);
        }
        imageRequest.setFilterChainProperties(filterChainProperties);
        return imageRequest;
    }

    /**
     * Extract per filterchain only properties. put them into their own properties and store them in global hashmap with
     * filterchain name as lookup key.
     *
     * @param key the per filterchain property key
     * @param value the per filterchain property value
     * @param filterChainProperties per image request properties
     */
    protected void fillFilterChainProperties(String key, String value, Properties filterChainProperties) {
        String[] perFilterChainProperty = key.split("__");
        Object perFilterChainProps = filterChainProperties.get(perFilterChainProperty[0] != null ? perFilterChainProperty[0] : EMPTY_STRING);
        if (perFilterChainProps == null) {
            perFilterChainProps = new Properties();
        }
        ((Properties) perFilterChainProps).setProperty(perFilterChainProperty[1] != null ? perFilterChainProperty[1].toUpperCase() : EMPTY_STRING, value);
        filterChainProperties.put(perFilterChainProperty[0] != null ? perFilterChainProperty[0] : EMPTY_STRING, perFilterChainProps);
    }

    /**
     * Set the image param on the ImageRequest
     *
     * @param key
     * @param imageRequest
     * @throws URISyntaxException
     */
    protected void fillImageParam(String key, String image, ImageRequest imageRequest) throws URISyntaxException {
        imageRequest.setImageURI(new URI(image));
        imageRequest.setEncodingFormat(determineContentType(image));
    }

    /**
     * Set the chain param on the ImageRequest
     *
     * @param key
     * @param value
     * @param imageRequest
     * @throws URISyntaxException
     */
    protected void fillChainParam(String key, String value, ImageRequest imageRequest) throws URISyntaxException {
        String[] chains = value.split("--");
        URI[] filterChainURIs = new URI[chains.length];
        for (int i = 0; i < chains.length; i++) {
            String chain = chains[i];
            if (!chain.startsWith(CHAINURISCHEME)) {
                chain = CHAINURISCHEME + chain;
            }
            filterChainURIs[i] = new URI(chain);
        }
        imageRequest.setFilterChainURI(filterChainURIs);
    }

    /**
     * Fix partial URIs
     *
     * @param resource
     * @return
     * @throws URISyntaxException
     */
    protected URI completeUri(String resource) throws URISyntaxException {
        URI uri;
        StringBuffer buffer = new StringBuffer();
        buffer.append(FILE);
        buffer.append(COLON);
        buffer.append(SLASH);
        buffer.append(SLASH);
        buffer.append(resource.startsWith(SLASH) ? resource : SLASH + resource);
        uri = new URI(buffer.toString());
        return uri;
    }

    /**
     * What content type are we looking for?
     *
     * @param imagePath
     * @return file extension
     */
    protected String determineContentType(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf(DOT) + 1).toLowerCase();
    }
}
