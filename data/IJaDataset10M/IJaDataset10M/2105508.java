package org.webguitoolkit.ui.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <pre>
 * The ResourceServlet streams the projects JavaScript and CSS files to the client.
 * There are several aliases that choin single js files.
 * 
 * full.js all relevant js files
 * base.js minimal set of js files
 * 
 * contextmenu.js for context menu
 * tree.js for the tree
 * select.js for select boxes
 * menu.js for menus
 * dragdrop.js for drag and drop
 * 
 * &lt;code&gt;
 * 
 *  place following code in your web.xml:
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;resource_servlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;com.endress.infoserve.wgt.http.ResourceServlet&lt;/servlet-class&gt;
 *      &lt;init-param&gt;
 *  		&lt;description&gt;the url pattern for addressing js and css files (wgt-resources is default)&lt;/description&gt;
 *  		&lt;param-name&gt;url-pattern&lt;/param-name&gt;
 *  		&lt;param-value&gt;resources&lt;/param-value&gt;
 *  	&lt;/init-param&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 * 	&lt;servlet-name&gt;resource_servlet&lt;/servlet-name&gt;
 * 	&lt;url-pattern&gt;/wgt-resources/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * 
 * &lt;/code&gt;
 * </pre>
 */
public class ResourceServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    public static final int MAXAGE = 60 * 60 * 24 * 30;

    public static final String PREFIX_WGT_CONTROLLER = "wgt.controller.";

    public static final String POSTFIX_COMPRESSED = ".min";

    public static final String CONTEXT_MENU_JS = "contextmenu.js";

    public static final String FULL_JS = "full.js";

    public static final String BASE_JS = "base.js";

    public static final String TREE_JS = "tree.js";

    public static final String SELECT_JS = "select.js";

    public static final String GLOSSBUTTON_JS = "glossbutton.js";

    public static final String CALENDAR_JS = "datepicker";

    public static final String TABLE_JS = "table.js";

    public static final String MENU_JS = "menu.js";

    public static final String DRAG_DROP_JS = "dragdrop.js";

    public static final String WGT_BASE = "wgt_base.js";

    public static final Collection<String> WGT_BASE_FILES = Arrays.asList("jsconsole.js", "jquery/jquery-1.4.2.min.js", "loadIndicator.js", "wgt.controller.js", "wgt.controller.base.js");

    public static final Collection<String> WGT_FULL_FILES = Arrays.asList("jquery/jquery-1.4.2.js", "loadIndicator.js", "jsconsole.js", "jquery/ui/1.8.1/jquery.ui.core.js", "jquery/ui/1.8.1/jquery.ui.widget.js", "jquery/ui/1.8.1/jquery.ui.mouse.js", "jquery/ui/1.8.1/jquery.ui.position.js", "jquery/ui/1.8.1/jquery.ui.draggable.js", "jquery/ui/1.8.1/jquery.ui.droppable.js", "jquery/ui/1.8.1/jquery.ui.sortable.js", "jquery/jquery.bgiframe.min.js", "jquery/jquery.clickmenu.js", "jquery/wgt.dropdown.js", "jquery/jquery.dynatree.js", "jquery/jquery.tooltip.js", "jquery/suggest.autocomplete.js", "wgt.controller.js", "wgt.controller.base.js", "wgt.controller.glossbutton.js", "wgt.controller.list.js", "wgt.controller.menu.js", "wgt.controller.dragdrop.js", "wgt.controller.multiselect.js", "wgt.controller.select.js", "wgt.controller.table.js", "wgt.controller.tabstrip.js", "wgt.controller.tree.js", "wgt.controller.textsuggest.js");

    public static String SERVLET_URL_PATTERN = "wgt-resources";

    public static boolean isDebug = false;

    public static boolean isStatic = false;

    public long lastModifiedDate = -1;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String urlPattern = config.getInitParameter("url-pattern");
        if (urlPattern != null && StringUtils.isNotEmpty(urlPattern)) SERVLET_URL_PATTERN = urlPattern;
        String debug = config.getInitParameter("debug");
        if (debug != null && StringUtils.isNotEmpty(debug)) isDebug = debug.equals("true");
        String staticScript = config.getInitParameter("static-script");
        if (staticScript != null && StringUtils.isNotEmpty(staticScript)) isStatic = staticScript.equals("true");
        try {
            String classContainer = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            URL manifestUrl = new URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(manifestUrl.openStream());
            Attributes attributes = manifest.getMainAttributes();
            if (attributes != null && !attributes.isEmpty()) {
                String buildString = attributes.getValue("Implementation-Build");
                String time = buildString.substring(buildString.indexOf("/") + 1);
                String dateString = time.substring(0, 10);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(dateString);
                lastModifiedDate = date.getTime();
            } else lastModifiedDate = System.currentTimeMillis();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass()).warn("Could not read manifest file");
            lastModifiedDate = System.currentTimeMillis();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedUri = req.getRequestURI();
        String requestedFile = requestedUri.substring(requestedUri.indexOf("/" + SERVLET_URL_PATTERN + "/") + ("/" + SERVLET_URL_PATTERN + "/").length());
        Logger.getLogger(this.getClass()).debug("Request for: " + requestedFile);
        PrintWriter out = null;
        try {
            if (requestedFile.endsWith(WGT_BASE) || (!requestedFile.endsWith(".js") && req.getParameter("page") == null)) {
                if (isUpToDate(req)) {
                    resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
                resp.addDateHeader("Last-Modified", lastModifiedDate);
                resp.setHeader("Cache-Control", "max-age=" + MAXAGE);
            }
            if (requestedFile.endsWith(".gif")) {
                resp.setHeader("Content-Type", "image/gif");
                sendBinay(requestedFile, resp.getOutputStream());
                return;
            }
            out = resp.getWriter();
            if (requestedFile.endsWith(WGT_BASE)) {
                if (isStatic) {
                    sendJS(ResourceServlet.PREFIX_WGT_CONTROLLER + "full.js", out, new HashSet());
                    sendCalendar("", out, req);
                } else sendJS(ResourceServlet.PREFIX_WGT_CONTROLLER + "base.js", out, new HashSet());
            } else if (requestedFile.endsWith(".js") || req.getParameter("page") != null) {
                resp.setHeader("Content-Type", "text/javascript;charset=ISO-8859-1");
                String lastModifiedString = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date((System.currentTimeMillis() - 3600000)));
                resp.setHeader("Last-Modified", lastModifiedString);
                String fileId = null;
                Set<String> files2load = null;
                Set<String> loadedFiles = new HashSet<String>();
                if (isStatic) loadedFiles.addAll(WGT_FULL_FILES); else loadedFiles.addAll(WGT_BASE_FILES);
                if (req.getParameter("page") != null) {
                    fileId = req.getParameter("page");
                    files2load = new HashSet<String>();
                    files2load.add(requestedFile);
                    if (!isStatic) {
                        Set<String> files = (Set<String>) req.getSession().getAttribute(fileId);
                        loadedFiles.addAll(files);
                    }
                } else {
                    fileId = requestedFile.substring(0, requestedFile.length() - 3);
                    files2load = (Set<String>) req.getSession().getAttribute(fileId);
                    if (files2load == null) {
                        files2load = new HashSet<String>();
                        files2load.add(requestedFile);
                    }
                }
                if (files2load != null) {
                    for (String file : files2load) {
                        if (file.startsWith(CALENDAR_JS)) {
                            if (loadedFiles.contains(CALENDAR_JS)) continue; else {
                                sendCalendar(requestedFile, out, req);
                                loadedFiles.add(CALENDAR_JS);
                            }
                            continue;
                        }
                        String realname = getRealName(file);
                        sendJS(realname, out, loadedFiles);
                    }
                    if (fileId != null) req.getSession().setAttribute(fileId, loadedFiles);
                    return;
                }
            } else if (requestedFile.endsWith(".css")) {
                resp.setHeader("Content-Type", "text/css");
                sendCss(requestedFile, out);
                return;
            } else if (requestedFile.endsWith(".html")) {
                resp.setHeader("Content-Type", "text/html");
                sendHTML(requestedFile, out);
                return;
            } else {
                Logger.getLogger(this.getClass()).error("Filedownload not allowed for file extension: " + requestedFile.substring(requestedFile.lastIndexOf(".")) + ", uri: " + requestedUri);
                return;
            }
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    private void sendCss(String requestedFile, PrintWriter out) throws IOException {
        if (requestedFile.endsWith("_theme.css")) {
            String folder = requestedFile.replaceAll("_theme\\.css", "");
            String compress = isDebug ? "" : POSTFIX_COMPRESSED;
            send("styles/" + folder + "/common" + compress + ".css", out);
            send("styles/" + folder + "/button" + compress + ".css", out);
            send("styles/" + folder + "/form" + compress + ".css", out);
            send("styles/" + folder + "/menu" + compress + ".css", out);
            send("styles/" + folder + "/tab" + compress + ".css", out);
            send("styles/" + folder + "/table" + compress + ".css", out);
            send("styles/" + folder + "/tree" + compress + ".css", out);
            send("styles/" + folder + "/clickmenu" + compress + ".css", out);
            send("styles/" + folder + "/calendar" + compress + ".css", out);
            send("styles/" + folder + "/jquery.tooltip" + compress + ".css", out);
            send("styles/" + folder + "/layout" + compress + ".css", out);
        } else send("styles/" + requestedFile, out);
    }

    private void sendHTML(String requestedFile, PrintWriter out) throws IOException {
        send("html/" + requestedFile, out);
    }

    private List<String> sendJS(String requestedFile, PrintWriter out, Set<String> files) throws IOException {
        List<String> sendFiles = new ArrayList<String>();
        String uncompressed = requestedFile;
        String compressed = requestedFile;
        if (requestedFile.endsWith(POSTFIX_COMPRESSED + ".js")) uncompressed = requestedFile.substring(0, requestedFile.length() - (POSTFIX_COMPRESSED + ".js").length()) + ".js"; else compressed = requestedFile.substring(0, requestedFile.length() - 3) + POSTFIX_COMPRESSED + ".js";
        if (files.contains(uncompressed)) return sendFiles; else files.add(uncompressed);
        if (uncompressed.startsWith(PREFIX_WGT_CONTROLLER)) {
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/pool/scripts/" + uncompressed);
            if (resourceAsStream == null) {
                Logger.getLogger(this.getClass()).warn("File not found: " + uncompressed);
                return sendFiles;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream));
            try {
                String line = in.readLine();
                if (line.startsWith("// Depends:")) {
                    String includes = line.substring("// Depends:".length());
                    String[] incs = includes.split(",");
                    for (String inc : incs) {
                        List<String> sendJS = sendJS(inc.trim(), out, files);
                        sendFiles.addAll(sendJS);
                    }
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        ;
                    }
                }
            }
        }
        boolean sendSuccess = false;
        if (!isDebug) sendSuccess = send("scripts/" + compressed, out);
        if (!sendSuccess) send("scripts/" + uncompressed, out);
        sendFiles.add(uncompressed);
        return sendFiles;
    }

    private void sendCalendar(String requestedFile, PrintWriter out, HttpServletRequest req) throws IOException {
        String lang = req.getLocale().getLanguage();
        String country = req.getLocale().getCountry();
        if (requestedFile.lastIndexOf(CALENDAR_JS) != -1 && requestedFile.endsWith(".js") && requestedFile.lastIndexOf(CALENDAR_JS) == (requestedFile.length() - CALENDAR_JS.length() - 6)) {
            lang = requestedFile.substring(requestedFile.length() - 5, requestedFile.length() - 3);
        }
        send("scripts/calendar.js", out);
        if (StringUtils.isNotBlank(country) && lang.equals("en")) {
            if (country.equals("US")) lang = lang + "_" + country;
        }
        InputStream inputStream = this.getClass().getResourceAsStream("/pool/scripts/lang/calendar-" + lang + ".js");
        if (inputStream != null) send("scripts/lang/calendar-" + lang + ".js", out); else send("scripts/lang/calendar-en.js", out);
        send("scripts/calendar-setup.js", out);
    }

    private boolean send(String requestedFile, PrintWriter out) throws IOException {
        Logger.getLogger(this.getClass()).debug("Sending File: " + requestedFile);
        InputStreamReader in = null;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/pool/" + requestedFile);
            if (inputStream == null) {
                Logger.getLogger(this.getClass()).error("requested File does not exist: " + requestedFile);
                return false;
            }
            in = new InputStreamReader(inputStream);
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        return true;
    }

    /**
	 * @param requestedFile
	 * @param outputStream
	 */
    private void sendBinay(String requestedFile, ServletOutputStream out) throws IOException {
        InputStream in = null;
        try {
            in = this.getClass().getResourceAsStream("/pool/" + requestedFile);
            if (in == null) {
                Logger.getLogger(this.getClass()).error("requested File does not exist: " + requestedFile);
                return;
            }
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                ;
            }
        }
    }

    private String getRealName(String alias) {
        if (CONTEXT_MENU_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (FULL_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (BASE_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (TREE_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (SELECT_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (GLOSSBUTTON_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (TABLE_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (MENU_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        } else if (DRAG_DROP_JS.equals(alias)) {
            return PREFIX_WGT_CONTROLLER + alias;
        }
        return alias;
    }

    public static final String getJQueryEffectLib(String effect) {
        return "jquery/ui/1.8.1/jquery.effects." + effect + ".js";
    }

    private boolean isUpToDate(HttpServletRequest req) {
        long modifiedSince = -1;
        try {
            modifiedSince = req.getDateHeader(IF_MODIFIED_SINCE);
        } catch (RuntimeException ex) {
        }
        if (modifiedSince != -1) {
            modifiedSince -= modifiedSince % 1000;
        }
        if (modifiedSince >= lastModifiedDate) {
            return true;
        }
        return false;
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        String requestedUri = req.getRequestURI();
        String requestedFile = requestedUri.substring(requestedUri.indexOf("/" + SERVLET_URL_PATTERN + "/") + ("/" + SERVLET_URL_PATTERN + "/").length());
        if (requestedFile.endsWith(WGT_BASE) || (!requestedFile.endsWith(".js") && req.getParameter("page") == null)) {
            return lastModifiedDate;
        }
        return super.getLastModified(req);
    }
}
