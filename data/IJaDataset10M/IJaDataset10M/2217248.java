package onepoint.project.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import onepoint.express.util.XConstants;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.persistence.OpBroker;
import onepoint.persistence.OpSiteObject;
import onepoint.persistence.OpTransaction;
import onepoint.project.OpGWTServiceManager;
import onepoint.project.OpInitializer;
import onepoint.project.OpInitializerFactory;
import onepoint.project.OpProjectSession;
import onepoint.project.forms.OpLoginFormProvider;
import onepoint.project.gwt.client.OpConstants;
import onepoint.project.gwt.client.OpResourceMap;
import onepoint.project.gwt.client.login.Login;
import onepoint.project.gwt.server.OpGWTService;
import onepoint.project.modules.documents.OpContent;
import onepoint.project.modules.documents.OpContentManager;
import onepoint.project.modules.project.OpAttachment;
import onepoint.project.modules.project.OpAttachmentIfc;
import onepoint.project.modules.project_planning.OpProjectPlanningService;
import onepoint.project.modules.site_management.OpSiteManager;
import onepoint.project.modules.user.OpPermission;
import onepoint.project.modules.user.OpPermissionable;
import onepoint.project.modules.user.OpUserService;
import onepoint.project.util.OpFile;
import onepoint.project.util.OpFileUtil;
import onepoint.project.util.OpProjectConstants;
import onepoint.resource.XLocalizer;
import onepoint.service.XMessage;
import onepoint.service.XSizeInputStream;
import onepoint.service.server.XBinaryServlet;
import onepoint.service.server.XServiceManager;
import onepoint.service.server.XSession;
import onepoint.util.XBase64;
import onepoint.util.XCookieManager;
import onepoint.util.XEncodingHelper;
import onepoint.util.XEnvironmentManager;
import onepoint.util.XIOHelper;
import com.google.gwt.i18n.client.LocalizableResource;

public class OpOpenServlet extends OpOpenServletBase {

    private static final String SERVICE = "service";

    private static final String LOGIN = "login";

    private static final long serialVersionUID = 1L;

    /**
    * Path separator
    */
    protected static final String PATH_SEPARATOR = "/";

    /**
    * Application constants.
    */
    private static final String WEBPAGEICON = "opp_windows.ico";

    /**
    * Applet parameters map keys
    */
    private static final Integer ID_INDEX = 0;

    private static final Integer NAME_INDEX = 1;

    private static final Integer CODEBASE_INDEX = 2;

    private static final Integer CODE_INDEX = 3;

    private static final Integer ARCHIVE_INDEX = 4;

    private static final Integer OTHER_PARAMETERS_INDEX = 5;

    public static final Object RUN_MUTEX = new Object();

    private static URL contextPathURL = null;

    /**
    * This class logger.
    */
    private static final XLog logger = XLogFactory.getLogger(OpOpenServlet.class);

    /**
    * Servlet init parameters.
    */
    private static String appletCodebase;

    private static String appletArchive;

    private static String imagesPath;

    private static String htmlTitle;

    private static boolean initialized = false;

    /**
    * String that indicates whether secure comunication should be used (https)
    */
    public static String secureService = null;

    private static Map<String, OpGWTService> gwtServiceCache = new HashMap();

    private static final String PARAMETERS_ARGUMENT = "parameters";

    private static final String MAIN_ERROR_MAP_ID = "main.error";

    private static final String CONTENT_ID_PARAM = "contentId";

    private static final String FILENAME_PARAM = "filename";

    private static final String FILE_PARAM = "file";

    private static final String INSUFICIENT_VIEW_PERMISSIONS = "${InsuficientViewPermissions}";

    private static final String INVALID_SESSION = "${InvalidSession}";

    private static final String INVALID_FILE_URL = "${InvalidFileURL}";

    private static final String TEXT_HTML_CONTENT_TYPE = "text/html";

    public OpOpenServlet() {
    }

    public static URL getContextPathURL() {
        synchronized (RUN_MUTEX) {
            return contextPathURL;
        }
    }

    @Override
    public void onInit() throws ServletException {
        super.onInit();
        String codebase = getInitParameter("applet_codebase");
        String contextName = getClientContextPath();
        if (codebase.contains(":/")) {
            appletCodebase = codebase;
            if (!appletCodebase.endsWith(PATH_SEPARATOR)) {
                appletCodebase += PATH_SEPARATOR;
            }
        } else {
            appletCodebase = contextName + (contextName.length() > 0 ? PATH_SEPARATOR : "") + codebase + PATH_SEPARATOR;
        }
        appletArchive = getInitParameter("applet_archive");
        imagesPath = contextName + (contextName.length() > 0 ? PATH_SEPARATOR : "") + getInitParameter("webimages_path") + PATH_SEPARATOR;
        htmlTitle = getInitParameter("html_title");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!initialized) {
            initialized = true;
            onepointInit(request);
        }
        super.service(request, response);
    }

    private void onepointInit(HttpServletRequest request) {
        try {
            URL serviceUrl = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
            synchronized (RUN_MUTEX) {
                contextPathURL = serviceUrl;
                RUN_MUTEX.notifyAll();
            }
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        }
    }

    private String getPathWithoutContextPath(HttpServletRequest request) {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        return uri;
    }

    private String getLastPathEntry(String url) {
        int delim = url.lastIndexOf('/');
        if (delim >= 0) {
            return url.substring(delim + 1);
        }
        return url;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = getPathWithoutContextPath(request);
        OpInitializer initializer = getProductIntializer();
        if (initializer.getRunLevel() != OpProjectConstants.SUCCESS_RUN_LEVEL || (url.startsWith(SERVICE))) {
            logger.debug("URL: " + url + " request: " + request);
            super.doPost(request, response);
        } else {
            doPostLogin(request, response);
        }
    }

    private void doPostLogin(HttpServletRequest request, HttpServletResponse response) {
        String path = getPathWithoutContextPath(request);
        int delim = path.indexOf('/');
        if (delim > 0) {
            path = path.substring(delim + 1);
        }
        OpGWTService gwtService = gwtServiceCache.get(path);
        if (gwtService == null) {
            gwtService = OpGWTServiceManager.getInstance().getGWTServiceImpl(path);
            if (gwtService != null) {
                gwtServiceCache.put(path, gwtService);
            }
        }
        if (gwtService != null) {
            try {
                gwtService.init(this);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            OpProjectSession session = (OpProjectSession) getSession(request);
            if (session == null) {
                session = new OpProjectSession(OpSiteManager.getSystemSite());
                Cookie cookie = getJSessionCookie(request);
                if (cookie != null) {
                    session.setCookie(cookie.getValue());
                }
                request.getSession().setAttribute("opx.session", session);
            }
            XSession.setSession(session);
            gwtService.doPost(request, response);
            XSession.removeSession();
        } else {
            try {
                super.doPost(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest http_request, HttpServletResponse http_response) throws ServletException, IOException {
        if (!isFileRequest(http_request)) {
            http_response.setHeader("Cache-Control", "max-age=1");
            http_response.setHeader("Pragma", "no-cache");
            http_response.setDateHeader("Expires", 0);
        } else {
            http_response.setHeader("Cache-Control", "max-age=1");
        }
        OpProjectSession session = (OpProjectSession) getSession(http_request);
        if (isFileRequest(http_request) && !session.isLoggedOn()) {
            generateErrorPage(http_response, INVALID_SESSION, session);
            return;
        }
        String contentId = http_request.getParameter(CONTENT_ID_PARAM);
        if (contentId != null && contentId.trim().length() > 0) {
            String contentUrl = readParameter(http_request, FILENAME_PARAM);
            if (contentUrl != null) {
                if (XEncodingHelper.isValueEncoded(contentUrl)) {
                    contentUrl = XEncodingHelper.decodeValue(contentUrl);
                }
            }
            generateContentPage(contentId, contentUrl, http_response, session);
            return;
        }
        String encFile = readParameter(http_request, FILE_PARAM);
        if (encFile != null && encFile.trim().length() > 0) {
            if (XEncodingHelper.isValueEncoded(encFile)) {
                String fileName = XEncodingHelper.decodeValue(encFile);
                File tmpFile = new File(XEnvironmentManager.TMP_DIR);
                String filePath = new File(XEnvironmentManager.TMP_DIR, fileName).getCanonicalPath();
                if (!filePath.startsWith(tmpFile.getCanonicalPath())) {
                    generateErrorPage(http_response, INVALID_FILE_URL, session);
                    return;
                }
                generateFilePage(filePath, http_response);
                return;
            } else {
                generateErrorPage(http_response, INVALID_FILE_URL, session);
                return;
            }
        }
        String url = getPathWithoutContextPath(http_request);
        OpInitializer initializer = getProductIntializer();
        if (initializer.getRunLevel() != OpProjectConstants.SUCCESS_RUN_LEVEL || (((url.equals(SERVICE) || url.equals(LOGIN)) && (session.isLoggedOn()))) || (url.equals("service/old"))) {
            session.setVariable("isLoading", Boolean.TRUE);
            http_response.setContentType(TEXT_HTML_CONTENT_TYPE);
            Cookie cookie = getJSessionCookie(http_request);
            if (cookie != null) {
                http_response.addCookie(cookie);
            }
            logger.debug("loading applet for client: " + http_request.getRemoteAddr() + ", op session: " + session + ", requesting: " + http_request.getRequestURI());
            generateAppletPage(http_response.getOutputStream(), http_request);
            return;
        }
        doGetLogin(http_request, http_response, session);
    }

    private void doGetLogin(HttpServletRequest http_request, HttpServletResponse http_response, OpProjectSession session) throws IOException {
        String url = getPathWithoutContextPath(http_request);
        if ((url.equals("login")) || (url.equals(SERVICE))) {
            generateLoginPage(http_request, http_response, session);
        } else {
            String newUrl = url;
            URL resource = Login.class.getResource(url);
            if (resource == null) {
                newUrl = "/" + url;
                resource = Login.class.getResource(newUrl);
            }
            if (resource == null) {
                newUrl = "/war/" + url;
                resource = Login.class.getResource(newUrl);
                if ((resource == null) && (url.startsWith("login/"))) {
                    newUrl = url.substring("login".length() + 1);
                    resource = Login.class.getResource(newUrl);
                }
            }
            ServletContext sc = getServletContext();
            String mimeType = sc.getMimeType(newUrl);
            if (mimeType == null) {
                sc.log("Could not get MIME type of " + newUrl);
                http_response.setStatus(500);
                return;
            }
            if (resource != null) {
                http_response.setContentType(mimeType);
                OpFile file = OpFileUtil.create(resource);
                http_response.setContentLength((int) file.length());
                InputStream in = file.getInputStream();
                ServletOutputStream out = http_response.getOutputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    out.write(ch);
                }
                out.flush();
                out.close();
            } else {
                logger.error("no resource found at: " + url);
            }
        }
    }

    private void generateLoginPage(HttpServletRequest request, HttpServletResponse response, OpProjectSession session) throws IOException {
        if (request.getQueryString() != null && !request.getQueryString().startsWith("gwt.codesvr=")) {
            Map<String, String> queryParams = getQueryParameters(request.getQueryString());
            session.setVariable("get.parameters", queryParams);
            response.sendRedirect(request.getRequestURI());
            return;
        }
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        OpBroker broker = session.newBroker();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Login.class.getResourceAsStream("Login.html")));
            Map<String, String> resources = createResourceMap(session, Login.class);
            Map<String, String> queryParams = (Map<String, String>) session.getVariable("get.parameters");
            session.removeVariable("get.parameters");
            String motd = OpLoginFormProvider.getMOTD();
            if (motd != null && motd.length() > 0) {
                if (queryParams == null) {
                    queryParams = new HashMap<String, String>();
                }
                queryParams.put("motd", motd);
            }
            String i18nVar = createJavaScriptMap("i18n", resources);
            String queryParamsVar = createJavaScriptMap("queryParams", queryParams);
            String line = reader.readLine();
            boolean replacedI18n = false;
            boolean replacedIQueryParams = false;
            while (line != null) {
                if ((!replacedI18n) && (line.contains("{i18nVariable}"))) {
                    line = line.replace("{i18nVariable}", i18nVar);
                    replacedI18n = true;
                } else if ((!replacedIQueryParams) && (line.contains("{queryParams}"))) {
                    line = line.replace("{queryParams}", queryParamsVar);
                    replacedIQueryParams = true;
                }
                response.getWriter().write(line + "\n");
                line = reader.readLine();
            }
        } finally {
            broker.close();
        }
    }

    private String getQueryParametersString(Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (buffer.length() != 0) {
                buffer.append('&');
            }
            buffer.append(entry.getKey());
            buffer.append('=');
            buffer.append(entry.getValue());
        }
        return buffer.toString();
    }

    private Map<String, String> getQueryParameters(String queryString) {
        Map<String, String> queryParameters = new HashMap<String, String>();
        if (queryString == null) {
            return queryParameters;
        }
        String[] entries = queryString.split("&");
        for (int pos = 0; pos < entries.length; pos++) {
            String[] keyValue = entries[pos].split("=");
            if (keyValue.length == 2) {
                try {
                    queryParameters.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return queryParameters;
    }

    private String createJavaScriptMap(String varName, Map<String, String> resources) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("  var " + varName + " = {").append("\n");
        if (resources != null) {
            boolean first = true;
            for (Map.Entry entry : resources.entrySet()) {
                if (!first) {
                    buffer.append(",").append("\n");
                }
                first = false;
                buffer.append("    \"").append((String) entry.getKey()).append("\" : \"").append((String) entry.getValue()).append("\"");
            }
            buffer.append("\n");
        }
        buffer.append("  };");
        return buffer.toString();
    }

    private Map<String, String> createResourceMap(OpProjectSession session, Class<?> root) {
        Map resources = new HashMap();
        XLocalizer localizer = session.getLocalizer();
        List<Class<? extends OpConstants>> constants = getConstantsForClass(root);
        for (Class<? extends OpConstants> constant : constants) {
            String resourceMapId = null;
            OpResourceMap resourceMap = constant.getAnnotation(OpResourceMap.class);
            if (resourceMap != null) {
                resourceMapId = resourceMap.value();
            }
            Method[] methods = constant.getDeclaredMethods();
            for (Method method : methods) {
                String resourceKeyId = method.getName();
                LocalizableResource.Key resourceKey = method.getAnnotation(LocalizableResource.Key.class);
                if (resourceKey != null) {
                    resourceKeyId = resourceKey.value();
                }
                resources.put(method.getName(), resourceMap != null ? localizer.localizeKey(resourceMapId, resourceKeyId) : localizer.localize(resourceKeyId));
            }
        }
        return resources;
    }

    private List<Class<? extends OpConstants>> getConstantsForClass(Class<?> type) {
        LinkedList ret = new LinkedList();
        Field[] fields = type.getDeclaredFields();
        for (int pos = 0; pos < fields.length; pos++) {
            if ((OpConstants.class.isAssignableFrom(fields[pos].getType())) && (fields[pos].getType().isInterface())) {
                ret.add(fields[pos].getType());
            }
        }
        return ret;
    }

    /**
    * Checks if the given request is a file request.
    *
    * @param request a <code>HttpServletRequest</code>.
    * @return <code>true</coed> if the request is a request for a file.
    */
    private boolean isFileRequest(HttpServletRequest request) {
        return readParameter(request, FILE_PARAM) != null || request.getParameter(CONTENT_ID_PARAM) != null;
    }

    /**
    * Reads those paramters from requests whcih might have values longer than 255 chars. Values of those
    * paramters were splitted in chuncks.
    *
    * @param request         a <code>HttpServletRequest</code>.
    * @param parameterPrefix parameter prefix to look for.
    * @return value for that parameter.
    */
    private String readParameter(HttpServletRequest request, String parameterPrefix) {
        StringBuffer buff = new StringBuffer();
        String chunk;
        int counter = 0;
        while ((chunk = request.getParameter(parameterPrefix + counter)) != null) {
            buff.append(chunk);
            counter++;
        }
        return buff.length() > 0 ? buff.toString() : null;
    }

    /**
    * Generates the default response which contains the application applet.
    *
    * @param sout    a <code>ServletOutputStream</code> where the output will be written.
    * @param request a <code>HttpServletRequest</code> representing the current request.
    * @throws IOException
    */
    protected void generateAppletPage(ServletOutputStream sout, HttpServletRequest request) throws IOException {
        PrintStream out = new PrintStream(sout);
        out.println("<html>");
        this.generatePageHeader(request, out);
        out.println("<body bgcolor=\"#ffffff\" onResize=\"resize()\" onLoad=\"resize()\" topmargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">");
        Map<Integer, Object> appletParameters = this.createAppletParameters(request);
        out.println("<script language=\"JavaScript\">");
        generateAppletResizeFunction(out);
        generateAppletMainFunction(out, appletParameters);
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }

    /**
    * Generates the header of the applet page.
    *
    * @param request a <code>HttpServletRequest</code> the client HTTP request.
    * @param out     a <code>PrintStream</code> used to write the response onto.
    * @throws IOException
    */
    protected void generatePageHeader(HttpServletRequest request, PrintStream out) throws IOException {
        out.println("<head>");
        out.println("<title>" + htmlTitle + "</title>");
        out.println(getIconString(request));
        out.println("<script>");
        BufferedInputStream javascript = new BufferedInputStream(OpOpenServlet.class.getResourceAsStream("deployJava.js"));
        byte[] buffer = new byte[4096];
        int read = javascript.read(buffer);
        while (read > 0) {
            out.write(buffer, 0, read);
            read = javascript.read(buffer);
        }
        out.println("</script>");
        out.println("</head>");
    }

    /**
    * Creates a map of applet parameters.
    *
    * @param request a <code>HttpRequest</code> representing the client request.
    * @return a <code>Map</code> of applet parameters.
    */
    private Map<Integer, Object> createAppletParameters(HttpServletRequest request) {
        String contextName = getServletConfig().getServletContext().getServletContextName();
        String codebase;
        if (appletCodebase.contains(":/")) {
            codebase = appletCodebase;
        } else {
            codebase = urlBase(request).concat(appletCodebase);
        }
        String sessionTimeoutSecs = String.valueOf(request.getSession().getMaxInactiveInterval());
        Map<Integer, Object> appletParams = new HashMap<Integer, Object>();
        appletParams.put(NAME_INDEX, contextName);
        appletParams.put(ID_INDEX, "onepoint");
        appletParams.put(CODEBASE_INDEX, codebase);
        appletParams.put(ARCHIVE_INDEX, appletArchive);
        appletParams.put(CODE_INDEX, getAppletClassName());
        Map<String, Object> otherAppletParams = new HashMap<String, Object>();
        otherAppletParams.put("host", request.getServerName());
        otherAppletParams.put("port", String.valueOf(request.getServerPort()));
        otherAppletParams.put("path", request.getContextPath() + request.getServletPath());
        otherAppletParams.put("secure-service", Boolean.toString(request.isSecure()));
        otherAppletParams.put("session-timeout", sessionTimeoutSecs);
        Cookie cookie = getJSessionCookie(request);
        if (cookie != null) {
            otherAppletParams.put("Cookie", cookie.getValue());
        }
        otherAppletParams.put("Referer", request.getHeader("Referer"));
        OpInitializer initializer = OpInitializerFactory.getInstance().getInitializer();
        Map<String, Object> params = initializer.getInitParams();
        otherAppletParams.put(OpProjectConstants.RUN_LEVEL, String.valueOf(initializer.getRunLevel()));
        OpProjectSession session = (OpProjectSession) request.getSession(true).getAttribute(XBinaryServlet.OP_SESSION_ATTRIBUTE);
        if (session != null && session.isLoggedOn()) {
            otherAppletParams.put(OpProjectConstants.START_FORM, params.get(OpProjectConstants.AUTO_LOGIN_START_FORM));
        } else {
            otherAppletParams.put(OpProjectConstants.START_FORM, params.get(OpProjectConstants.START_FORM));
        }
        otherAppletParams.put(XConstants.REDIRECT_LOGIN_FORM, "/login");
        otherAppletParams.put(OpProjectConstants.START_FORM_PARAMETERS, params.get(OpProjectConstants.START_FORM_PARAMETERS));
        otherAppletParams.put(OpProjectConstants.AUTO_LOGIN_START_FORM, initializer.getAutoLoginStartForm());
        Map<String, String> queryParams = (Map<String, String>) session.getVariable("get.parameters");
        if (queryParams == null) {
            queryParams = new HashMap<String, String>();
        }
        queryParams.putAll(getQueryParameters(request.getQueryString()));
        String queryString = getQueryParametersString(queryParams);
        otherAppletParams.put(OpProjectConstants.PARAMETERS, queryString);
        if (initializer.getConfiguration() != null && initializer.getConfiguration().getClientJvmOptions() != null) {
            String clientJVMOptions = initializer.getConfiguration().getClientJvmOptions();
            otherAppletParams.put(OpProjectConstants.CLIENT_JVM_OPTIONS, clientJVMOptions);
        }
        String parameterNames = request.getParameter(PARAMETERS_ARGUMENT);
        if (parameterNames != null) {
            otherAppletParams.put("parameters", parameterNames);
            try {
                parameterNames = URLDecoder.decode(parameterNames, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.warn("Could not decode start_form for applet: " + e);
            }
            StringTokenizer st = new StringTokenizer(parameterNames, ";");
            while (st.hasMoreTokens()) {
                String parameter = st.nextToken();
                String val = parameter;
                int idx = parameter.indexOf(":");
                if (idx != -1) {
                    val = parameter.substring(0, idx);
                }
                otherAppletParams.put(val, request.getParameter(val));
            }
        }
        appletParams.put(OTHER_PARAMETERS_INDEX, otherAppletParams);
        return appletParams;
    }

    protected String getAppletClassName() {
        return "onepoint.project.applet/OpOpenApplet.class";
    }

    /**
    * Gets the base url for the given request.
    *
    * @param request a <code>HttpServletRequest</code> object.
    * @return a <code>String</code> representing the base url of the request.
    */
    private String urlBase(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + PATH_SEPARATOR;
    }

    /**
    * Return a string that contains a path to an icon that will displayed for the application.
    *
    * @param request a <code>HttpServletRequest</code> object.
    * @return a <code>String</code> representing an html snippet of code.
    */
    private String getIconString(HttpServletRequest request) {
        return "<link rel=\"SHORTCUT ICON\" href=\"" + urlBase(request).concat(imagesPath).concat(WEBPAGEICON) + "\" type=\"image/ico\" />";
    }

    /**
    * Generates the server response in the case when a persisted contentId is requested.
    *
    * @param contentId     a <code>String</code> representing a content id.
    * @param contentUrl    a <code>String</code> representing the url of the content.
    * @param http_response a <code>HttpServletResponse</code> representing the server response.
    * @param session       a <code>OpProjectSession</code> object representing the server session.
    */
    private void generateContentPage(String contentId, String contentUrl, HttpServletResponse http_response, OpProjectSession session) {
        OpBroker broker = session.newBroker();
        OpTransaction t = broker.newTransaction();
        try {
            XSession.setSession(session);
            OpContent cnt = (OpContent) broker.getObject(contentId);
            if (cnt != null) {
                if (!hasContentPermissions(session, broker, cnt)) {
                    try {
                        PrintStream ps = new PrintStream(http_response.getOutputStream());
                        generateErrorPage(ps, INSUFICIENT_VIEW_PERMISSIONS, session);
                        ps.flush();
                        ps.close();
                    } catch (IOException e) {
                        logger.error("Cannot generate error response", e);
                    }
                    return;
                }
                InputStream content = cnt.getStream();
                String mimeType = cnt.getMediaType();
                logger.info("content " + cnt.getName() + " has contentType: " + mimeType);
                http_response.setContentType(mimeType);
                if (contentUrl != null) {
                    setContentDisposition(http_response, contentUrl);
                }
                try {
                    OutputStream stream = http_response.getOutputStream();
                    XIOHelper.copy(content, stream);
                } catch (IOException e) {
                    logger.error("Cannot send contentId", e);
                } finally {
                    if (content != null) {
                        try {
                            content.close();
                        } catch (IOException e) {
                            logger.error("Cannot close content stream", e);
                        }
                    }
                }
            } else {
                http_response.setContentType("text/plain");
                try {
                    PrintStream out = new PrintStream(http_response.getOutputStream());
                    out.println("Content with id " + contentId + " could not be found");
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        } finally {
            XSession.removeSession();
            t.commit();
            broker.close();
        }
    }

    /**
    * Generates a response from the server when a file is requested.
    *
    * @param filePath     a <code>String</code> representing the full path to a file.
    * @param httpResponse a <code>HttpServletResponse</code> object representing the response.
    */
    public void generateFilePage(String filePath, HttpServletResponse httpResponse) {
        String name = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length());
        String mimeType = OpContentManager.getFileMimeType(name);
        httpResponse.setContentType(mimeType);
        setContentDisposition(httpResponse, filePath);
        byte[] buffer = new byte[1024];
        try {
            OutputStream stream = httpResponse.getOutputStream();
            InputStream is = new FileInputStream(filePath);
            int length = is.read(buffer);
            while (length != -1) {
                stream.write(buffer, 0, length);
                length = is.read(buffer);
            }
            is.close();
        } catch (IOException e) {
            logger.error("Cannot open url to file" + filePath, e);
        }
    }

    /**
    * Sets the content disposition for the http response.
    *
    * @param httpResponse a <code>HttpServletResponse</code> object.
    * @param fileName     a <code>String</code> representing a name of a file.
    */
    private void setContentDisposition(HttpServletResponse httpResponse, String fileName) {
        if (fileName == null || fileName.length() == 0) {
            fileName = "NewFile";
        }
        if (fileName.indexOf(File.separator) != -1) {
            fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
        }
        if (fileName.indexOf("/") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
        }
        fileName = fileName.replaceAll("[^-._a-zA-Z0-9]", "_");
        httpResponse.setHeader("Content-Disposition", "inline; filename=" + fileName);
    }

    /**
    * Generates a JS function that will be used to render the applet.
    *
    * @param out    a <code>PrintStream</code> were the result will be outputed to.
    * @param params a <codde>Map</code> representing the applet parameters.
    * @throws IOException
    */
    private void generateAppletMainFunction(PrintStream out, Map<Integer, Object> params) throws IOException {
        String name = (String) params.get(NAME_INDEX);
        String codebase = (String) params.get(CODEBASE_INDEX);
        String code = (String) params.get(CODE_INDEX);
        String archive = (String) params.get(ARCHIVE_INDEX);
        Map<String, String> otherParams = (Map<String, String>) params.get(OTHER_PARAMETERS_INDEX);
        StringBuffer paramBuffer = new StringBuffer();
        boolean first = true;
        for (Map.Entry<String, String> param : otherParams.entrySet()) {
            if (first) {
                first = false;
            } else {
                paramBuffer.append(", ");
            }
            paramBuffer.append("'" + param.getKey() + "':'" + param.getValue() + "'");
        }
        out.println("  deployJava.setInstallerType('online');");
        out.println("  var attributes = {id:'onepoint', codebase:'" + codebase + "', code:'" + code + (archive == null ? "" : "', archive:'" + archive) + "', name:'" + name + "',  width:'100%', height:'100%'} ;");
        out.println("  var parameters = {" + paramBuffer.toString() + "} ;");
        out.println("  deployJava.runApplet(attributes, parameters, '1.5');");
    }

    private void generateAppletResizeFunction(PrintStream out) {
        out.println("function resize() {\n" + "                var ie = (navigator.userAgent.indexOf(\"MSIE\") > 0);\n" + "                if (ie) {\n" + "                  width = document.body.clientWidth; \n" + "                  height = document.body.clientHeight - 4;\n" + "                  document.getElementById(\"onepoint\").width = width;\n" + "                  document.getElementById(\"onepoint\").height = height;\n" + "                  window.scroll(0,0);\n" + "                }\n" + "            }\n" + "            window.onResize = resize;\n" + "            window.onLoad = resize;\n");
    }

    @Override
    protected XMessage processRequest(XMessage request, boolean sessionExpired, HttpServletRequest http_request, HttpServletResponse http_response, XSession session) {
        if (request.getAction().equalsIgnoreCase(OpProjectConstants.GET_RUN_LEVEL_ACTION)) {
            XMessage response = new XMessage();
            OpInitializer initializer = OpInitializerFactory.getInstance().getInitializer();
            response.setArgument(OpProjectConstants.RUN_LEVEL, Byte.toString(initializer.getRunLevel()));
            return response;
        }
        XMessage response = super.processRequest(request, sessionExpired, http_request, http_response, session);
        addAutoLoginCookie(request, response, http_response);
        return response;
    }

    /**
    * Add an AutoLogin cookie to the HTTP request if conditions are meet.
    *
    * @param request       the <code>XMessage</code> action request
    * @param response      the <code>XMessage</code> action response
    * @param http_response the HTTP request
    */
    private void addAutoLoginCookie(XMessage request, XMessage response, HttpServletResponse http_response) {
        boolean singOnAction = request.getAction().equalsIgnoreCase(OpProjectConstants.SIGNON_ACTION);
        boolean rememberChecked = request.getArgument(OpProjectConstants.REMEMBER_PARAM) != null && ((Boolean) request.getArgument(OpProjectConstants.REMEMBER_PARAM));
        boolean noError = response != null && response.getError() == null;
        if (singOnAction && rememberChecked && noError) {
            String name = (String) request.getArgument(OpUserService.LOGIN);
            String password = "";
            if (request.getArgument(OpUserService.PASSWORD) != null) {
                password = (String) request.getArgument(OpUserService.PASSWORD);
            }
            Cookie cookie = new Cookie(XCookieManager.AUTO_LOGIN, XBase64.encodeString(name + ' ' + password));
            cookie.setVersion(0);
            cookie.setMaxAge(XCookieManager.TTL);
            http_response.addCookie(cookie);
        }
    }

    /**
    * Generates an error page, as a user response to some action.
    *
    * @param http_response a <code>HttpServletResponse</code> http response.
    * @param errorMessage  a <code>String</code> representing an error message to display. The errorMessage tries to be
    *                      i18ned from the main language res file.
    * @param session       a <code>OpProjectSession</code> representing the application user session.
    * @throws java.io.IOException if opening the output stream fails
    */
    private void generateErrorPage(HttpServletResponse http_response, String errorMessage, OpProjectSession session) throws IOException {
        http_response.setContentType(TEXT_HTML_CONTENT_TYPE);
        PrintStream ps = new PrintStream(http_response.getOutputStream());
        generateErrorPage(ps, errorMessage, session);
        ps.flush();
        ps.close();
    }

    /**
    * Generates an error page, as a user response to some action.
    *
    * @param out          a <code>PrintStream</code> representing the output stream on which the server response is written.
    * @param errorMessage a <code>String</code> representing an error message to display. The errorMessage tries to be
    *                     i18ned from the main language res file.
    * @param session      a <code>OpProjectSession</code> representing the application user session.
    */
    private void generateErrorPage(PrintStream out, String errorMessage, OpProjectSession session) {
        XLocalizer localizer = new XLocalizer();
        localizer.setResourceMap(session.getLocale().getResourceMap(MAIN_ERROR_MAP_ID));
        String i18nErrorMessage = localizer.localize(errorMessage);
        out.println("<html>");
        out.println("<head><title> " + htmlTitle + " Error </title></head>");
        out.print("<body><h1><font color=\"red\">");
        out.print(i18nErrorMessage);
        out.println("</font></h1></body>");
        out.print("</html>");
    }

    /**
    * Checks if the current logged in user has at least view-permissions of the given content.
    *
    * @param session a <code>OpProjectSession</code> representing the server session.
    * @param content a <code>OpContent</code> object representing the content the user is trying to view.
    * @param broker  a <code>OpBroker</code> used for performing business operations.
    * @return <code>true</code> if the user has rights to view the content, false otherwise.
    *         <FIXME author="Horia Chiorean" description="Is is safe to assume that it's enough to have permissions on at least 1 of the entities referencing the content ?">
    */
    private boolean hasContentPermissions(OpProjectSession session, OpBroker broker, OpContent content) {
        if (content.getRefCount() == 0) {
            return true;
        }
        Set<Long> pointers = new HashSet<Long>();
        pointers.addAll(getPermissionableIdsForAttachments(session, broker, content.getAttachments()));
        pointers.addAll(getPermissionableIdsForAttachments(session, broker, content.getAttachmentVersions()));
        pointers.addAll(OpSiteObject.getIdsFromObjects(content.getDocuments()));
        pointers.addAll(OpSiteObject.getIdsFromObjects(content.getDocumentNodes()));
        return !session.accessibleIds(broker, pointers, OpPermission.OBSERVER).isEmpty();
    }

    private Collection<Long> getPermissionableIdsForAttachments(OpProjectSession session, OpBroker broker, Set<? extends OpAttachmentIfc> attachments) {
        Collection<Long> result = new HashSet<Long>();
        if (attachments == null) {
            return result;
        }
        for (OpAttachmentIfc attachment : attachments) {
            OpPermissionable pm = attachment.getObjectForPermissions();
            if (pm == null && attachment instanceof OpAttachment) {
                OpAttachment att = (OpAttachment) attachment;
                pm = ((OpProjectPlanningService) XServiceManager.getInstance().getService(OpProjectPlanningService.SERVICE_NAME)).getPermissionableForAttachment(broker, att);
            }
            result.add(Long.valueOf(pm.getId()));
        }
        return result;
    }

    /**
    * Handle a file upload request
    *
    * @param request a file upload <code>HttpServletRequest</code>
    * @param stream  the <code>InputStream</code> to read the files from
    * @param message the <code>XMessage</code> received from the client
    * @throws java.io.IOException if the file upload handling failed
    */
    @Override
    protected void handleFileUpload(HttpServletRequest request, InputStream stream, XMessage message) throws IOException {
        LinkedHashMap<String, Long> sizes = (LinkedHashMap<String, Long>) message.getArgument(XConstants.FILE_SIZE_ARGUMENT);
        Map<String, String> names = (Map<String, String>) message.getArgument(XConstants.FILE_NAME_ARGUMENT);
        Map<String, String> references = (Map<String, String>) message.getArgument(XConstants.FILE_REF_ARGUMENT);
        boolean streamToFile = false;
        if (message.getArgumentsMap().containsKey(XConstants.STREAM_TO_FILE)) {
            streamToFile = (Boolean) message.getArgument(XConstants.STREAM_TO_FILE);
        }
        if (sizes != null && !sizes.isEmpty()) {
            if (streamToFile) {
                Map<String, File> files = new HashMap<String, File>();
                for (Map.Entry<String, Long> entry : sizes.entrySet()) {
                    String id = entry.getKey();
                    long size = entry.getValue();
                    String name = names != null ? names.get(id) : null;
                    if (name == null) {
                        name = "unknown";
                    }
                    InputStream fis = new XSizeInputStream(stream, size, true);
                    int delim = name.lastIndexOf(".");
                    String suffix = null;
                    String prefix = name;
                    if (delim >= 0) {
                        suffix = name.substring(delim);
                        prefix = name.substring(0, delim);
                    }
                    while (prefix.length() < 3) {
                        prefix = prefix + "x";
                    }
                    File newFile = File.createTempFile(prefix, suffix, new File(XEnvironmentManager.TMP_DIR));
                    OutputStream fos = new FileOutputStream(newFile);
                    XIOHelper.copy(fis, fos);
                    fos.flush();
                    fos.close();
                    fis.close();
                    Set<String> refs = getRefIds(id, references);
                    for (String refId : refs) {
                        files.put(refId, newFile);
                    }
                }
                message.insertObjectsIntoArguments(files);
            } else {
                this.checkAttachmentSizes(sizes.values());
                Map<String, String> contents = new HashMap<String, String>();
                OpProjectSession session = (OpProjectSession) getSession(request);
                OpBroker broker = session.newBroker();
                try {
                    for (Map.Entry<String, Long> entry : sizes.entrySet()) {
                        String id = entry.getKey();
                        long size = entry.getValue();
                        String name = names != null ? names.get(id) : null;
                        String mimeType = OpContentManager.getFileMimeType(name != null ? name : "");
                        XSizeInputStream inStream = new XSizeInputStream(stream, size, true);
                        OpContent content = OpContentManager.newContent(inStream, mimeType, 0);
                        OpTransaction t = broker.newTransaction();
                        broker.makePersistent(content);
                        t.commit();
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Set<String> refs = getRefIds(id, references);
                        for (String refId : refs) {
                            contents.put(refId, content.locator());
                        }
                    }
                } finally {
                    broker.close();
                }
                message.insertObjectsIntoArguments(contents);
            }
        }
    }

    /**
    * Checks if any attachments has a size larger than the configured application size, and
    * throws an exception if it does.
    *
    * @param attachmentSizes a <code>Collection(Long)</code> the attachment sizes in bytes.
    * @throws IOException if any of the attachment sizes is larget than the configured size.
    */
    private void checkAttachmentSizes(Collection<Long> attachmentSizes) throws IOException {
        OpInitializer initializer = OpInitializerFactory.getInstance().getInitializer();
        long maxSizeBytes = initializer.getMaxAttachmentSizeBytes();
        for (Long attachmentSize : attachmentSizes) {
            if (attachmentSize > maxSizeBytes) {
                String message = "Attachments larger than the configured size of " + maxSizeBytes + " are not allowed. Aborting transaction";
                logger.error(message);
                throw new IOException(message);
            }
        }
    }

    /**
    * Retrieves all the generated id of the contents that refer the same file.
    *
    * @param id         the id of the refered file
    * @param references a <code>Map</code> of key = generated id of a content, value = generated id of the refered content
    * @return a <code>Set</code> of generated ids which refere to the same file. (Includes the refered id)
    */
    private Set<String> getRefIds(String id, Map<String, String> references) {
        Set<String> refs = new HashSet<String>();
        refs.add(id);
        for (Map.Entry<String, String> entry : references.entrySet()) {
            if (entry.getValue().equals(id)) {
                refs.add(entry.getKey());
            }
        }
        return refs;
    }
}
