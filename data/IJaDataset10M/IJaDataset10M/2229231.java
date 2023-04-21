package net.infordata.ifw2.web.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.infordata.ifw2.util.TmpByteArrayOutputStream;
import net.infordata.ifw2.util.Util;
import net.infordata.ifw2.web.ctrl.FlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class WEBUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WEBUtil.class);

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final char NULLCHAR_REPLACEMENT = ' ';

    /** */
    protected WEBUtil() {
    }

    /**
   * Same as {@link ServletRequest#getCharacterEncoding()} but with a default.
   * @param request
   * @return the character encoding of the request. <br>
   *   If no encoding is found or if the specified encodig is ISO-8859-1 then
   *   returns CP1252 for correct euro symbol support.
   */
    public static final String getCharacterEncoding(ServletRequest request) {
        String encoding = request.getCharacterEncoding();
        if (encoding == null || "ISO-8859-1".equalsIgnoreCase(encoding)) encoding = "CP1252";
        return encoding;
    }

    /**
   * Converts the string in a format which is html friendly by replacing
   * special chars with the relative entities.
   * @return the converted string, null is converted in an empty String
   */
    public static final String nbsp(String ss) {
        if (ss == null) ss = "";
        return ("".equalsIgnoreCase(ss)) ? "&nbsp;" : string2Html(ss);
    }

    /**
   * Converts the string in a format which is xml friendly by replacing
   * special chars with the relative entities.<br>
   * null chars are replaced by  .
   */
    public static final String string2Xml(CharSequence ss) {
        if (ss == null) return "";
        StringBuilder sb = new StringBuilder(ss.length() * 2);
        char ch;
        for (int i = 0; i < ss.length(); i++) {
            ch = ss.charAt(i);
            switch(ch) {
                case 0:
                    sb.append(NULLCHAR_REPLACEMENT);
                    break;
                case '€':
                    sb.append("&#128;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
   * Converts the string in a format which is html friendly by replacing
   * special chars with the relative entities.
   */
    public static final String string2Html(CharSequence ss) {
        return string2Xml(ss);
    }

    /**
   * @deprecated see {@link #string2Url(CharSequence)}
   */
    @Deprecated
    public static final String string2HtmlParam(CharSequence ss) {
        return string2Url(ss);
    }

    /**
   * Converts the string in a format which is url friendly by replacing
   * special chars with % escape codes.<br>
   * Null chars are replaced by  .<br>
   * See: http://en.wikipedia.org/wiki/Percent-encoding 
   */
    public static final String string2Url(CharSequence ss) {
        if (ss == null) return "";
        StringBuilder sb = new StringBuilder(ss.length() * 2);
        char ch;
        for (int i = 0; i < ss.length(); i++) {
            ch = ss.charAt(i);
            if (ch <= 0x1F || ch == 0x7F) {
                sb.append('%').append(Integer.toHexString(ch));
            } else {
                switch(ch) {
                    case '!':
                        sb.append("%21");
                        break;
                    case '*':
                        sb.append("%2A");
                        break;
                    case '\'':
                        sb.append("%27");
                        break;
                    case '(':
                        sb.append("%28");
                        break;
                    case ')':
                        sb.append("%29");
                        break;
                    case ';':
                        sb.append("%3B");
                        break;
                    case ':':
                        sb.append("%3A");
                        break;
                    case '@':
                        sb.append("%40");
                        break;
                    case '&':
                        sb.append("%26");
                        break;
                    case '=':
                        sb.append("%3D");
                        break;
                    case '+':
                        sb.append("%2B");
                        break;
                    case '$':
                        sb.append("%24");
                        break;
                    case ',':
                        sb.append("%2C");
                        break;
                    case '/':
                        sb.append("%2F");
                        break;
                    case '?':
                        sb.append("%3F");
                        break;
                    case '#':
                        sb.append("%23");
                        break;
                    case '[':
                        sb.append("%5B");
                        break;
                    case ']':
                        sb.append("%5D");
                        break;
                    case ' ':
                        sb.append("%20");
                        break;
                    case '\"':
                        sb.append("%22");
                        break;
                    case '<':
                        sb.append("%3C");
                        break;
                    case '>':
                        sb.append("%3E");
                        break;
                    case '%':
                        sb.append("%25");
                        break;
                    default:
                        sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    /**
   * Converts the string in a format which is JavaScript friendly by replacing
   * special chars with \ escaped codes.<br>
   * null chars are replaced by  .
   */
    public static final String string2Js(CharSequence ss) {
        if (ss == null) return "";
        StringBuilder sb = new StringBuilder(ss.length() * 2);
        char ch;
        for (int i = 0; i < ss.length(); i++) {
            ch = ss.charAt(i);
            switch(ch) {
                case 0:
                    ch = NULLCHAR_REPLACEMENT;
                    break;
                case '\"':
                    sb.append('\\');
                    sb.append('\"');
                    break;
                case '\'':
                    sb.append('\\');
                    sb.append('\'');
                    break;
                case '\\':
                    sb.append('\\');
                    sb.append('\\');
                    break;
                case '\r':
                    break;
                case '\n':
                    sb.append('\\');
                    sb.append('n');
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
   * Converts the string in a format which is JavaScript friendly by replacing
   * special chars with \ escaped codes, the resulting string is compatible
   * with the unescape JavaScript function.<br>
   * null chars are replaced by  .
   */
    public static final String string2JsEscaped(CharSequence ss) {
        if (ss == null) return "";
        StringBuilder sb = new StringBuilder(ss.length() * 2);
        char ch;
        for (int i = 0; i < ss.length(); i++) {
            ch = ss.charAt(i);
            if (ch < 128) {
                switch(ch) {
                    case 0:
                        sb.append(NULLCHAR_REPLACEMENT);
                        break;
                    case '\"':
                        sb.append('\\');
                        sb.append('\"');
                        break;
                    case '\\':
                        sb.append('\\');
                        sb.append('\\');
                        break;
                    case '\r':
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case '%':
                        sb.append("%25");
                        break;
                    default:
                        sb.append(ch);
                }
            } else if (ch < 256) {
                String str = "%" + Util.toHex((byte) ((int) ch & 0xff));
                sb.append(str);
            } else {
                String utf16 = "%u" + Util.toHex((byte) ((int) ch >> 8)) + Util.toHex((byte) ((int) ch & 0xff));
                sb.append(utf16);
            }
        }
        return sb.toString();
    }

    /**
   * Replaces {@link HttpServletResponse#sendRedirect(String)}.
   */
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String page) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sendRedirect()  " + page);
            LOGGER.debug("  isCommitted() " + response.isCommitted());
        }
        response.reset();
        response.setHeader("Refresh", "0;URL=" + page);
        response.flushBuffer();
    }

    /**
   * Use it to send a redirect in a post request, it does not leave tracks
   * in the browser history.
   */
    public static void redirectPost(HttpServletRequest request, HttpServletResponse response, String page) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("redirectPost()  " + page);
            LOGGER.debug("  isCommitted() " + response.isCommitted());
        }
        if (!"POST".equals(request.getMethod())) throw new IllegalArgumentException("Not a post request");
        response.reset();
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", page);
        response.flushBuffer();
    }

    /** 
   * @param request
   * @return <pre>
   *         never null
   *         OP<V> = opera, version undefined
   *         IE<V> = Internet Explorer where <V> is a single char version (5, 6, 7, 8, ...)
   *         NS4 = Netscape 4
   *         MZ<V>-<gkbuild> = Mozilla where <V> is a single char version (5) and <gkbuild> is the gecko
   *                           engine build date in ISO format (yyyymmdd).
   *         WK<V> = Apple WebKit.
   *         contype = used by plugins such as Acrobat Reader
   *         MOPD = Microsoft Office Protocol Discovery  
   *         UNKNOWN
   *         </pre>
   * @throws ServletException
   * @throws IOException
   */
    public static String checkBrowser(HttpServletRequest request) throws ServletException, IOException {
        String value = request.getHeader("User-Agent");
        String browser = "UNKNOWN";
        if (value != null) {
            value = value.toLowerCase();
            int idx = value.indexOf(") opera ");
            if (idx >= 0) {
                browser = "OP0";
                return browser;
            }
            idx = value.indexOf("applewebkit/");
            if (idx >= 0) {
                int ver = Integer.valueOf(value.substring(idx + "applewebkit/".length(), idx + "applewebkit/".length() + 3)).intValue();
                if (ver >= 530) browser = "WK3-" + ver; else browser = "WK0";
                return browser;
            }
            idx = value.indexOf("mozilla/");
            if (idx >= 0) {
                int ver = Integer.valueOf(value.substring(idx + 8, idx + 9)).intValue();
                if (ver >= 5) {
                    browser = "MZ5";
                    idx = value.indexOf("gecko/");
                    if (idx >= 0) {
                        browser += "-" + value.substring(idx + 6, idx + 6 + 8);
                    }
                } else browser = "NS4";
            }
            idx = value.indexOf("msie");
            if (idx >= 0) {
                int ver = Integer.valueOf(value.substring(idx + 5, idx + 6)).intValue();
                int subver = Integer.valueOf(value.substring(idx + 7, idx + 8)).intValue();
                if (ver >= 9) browser = "XE9"; else if (ver >= 8) browser = "IE8"; else if (ver >= 7) browser = "IE7"; else if (ver >= 6) browser = "IE6"; else if (ver >= 5) {
                    if (subver >= 5) browser = "IE5.5"; else browser = "IE5";
                } else if (ver >= 4) browser = "IE4"; else browser = "IE";
            }
            idx = value.indexOf("contype");
            if (idx >= 0) {
                browser = "contype";
            }
            idx = value.indexOf("Discovery");
            if (idx >= 0) {
                idx = value.indexOf("Office Protocol");
                if (idx >= 0) {
                    browser = "MOPD";
                }
            }
        }
        return browser;
    }

    /** 
   * @param request
   * @return <pre>
   *         AN<V> = Android
   *         IP<x> = IPAd, IPHone, IPOd, ... (check the webkit browser version)
   *         null  = Irrelevant, ie not detected
   *         </pre>
   * @throws ServletException
   * @throws IOException
   */
    public static String checkBrowserPlatform(HttpServletRequest request) throws ServletException, IOException {
        String value = request.getHeader("User-Agent");
        String platform = null;
        if (value != null) {
            value = value.toLowerCase();
            int idx = value.indexOf("android");
            if (idx >= 0) {
                try {
                    int ver = Integer.valueOf(value.substring(idx + "android ".length(), idx + "android ".length() + 3).replace(".", "")).intValue();
                    platform = "AN" + ver;
                } catch (NumberFormatException ex) {
                    platform = "AN";
                }
                return platform;
            }
            idx = value.indexOf("(ipad;");
            if (idx >= 0) {
                platform = "IPA";
                return platform;
            }
            idx = value.indexOf("(iphone;");
            if (idx >= 0) {
                platform = "IPH";
                return platform;
            }
            idx = value.indexOf("(ipod;");
            if (idx >= 0) {
                platform = "IPO";
                return platform;
            }
        }
        return platform;
    }

    /**
   * @param request
   * @return not null if the application is running inside the Android IFW2 
   *   WebView.
   * @throws ServletException
   * @throws IOException
   */
    public static Integer checkWebView(HttpServletRequest request) throws ServletException, IOException {
        String value = request.getHeader("User-Agent");
        Integer version = null;
        if (value != null) {
            value = value.toLowerCase();
            int idx = value.indexOf("ifw2wv");
            if (idx >= 0) {
                int ver = Integer.valueOf(value.substring(idx + "ifw2wv".length(), idx + "ifw2wv".length() + 3)).intValue();
                version = ver;
                return version;
            }
        }
        return version;
    }

    private static final StringBuilder _getRequestURL(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) port = 80;
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        return url;
    }

    /**
   * @param request
   * @return same as {@link HttpServletRequest#getRequestURL()}, but ends
   *   with the context-path.
   */
    public static final String getRequestURLUpToContextPath(HttpServletRequest request) {
        StringBuilder url = _getRequestURL(request);
        String contextPath = request.getContextPath();
        if (contextPath != null) url.append(contextPath);
        return url.toString();
    }

    public static final String getRequestURL(HttpServletRequest request) {
        StringBuilder url = _getRequestURL(request);
        url.append(getCurrentPage(request));
        return url.toString();
    }

    /**
   * See http://www.caucho.com/resin-3.0/webapp/faq.xtp
   */
    public static final String getOrgRequestURI(HttpServletRequest request) {
        String org = (String) request.getAttribute("javax.servlet.forward.request_uri");
        String res = (org != null) ? org : request.getRequestURI();
        return res;
    }

    /**
   */
    public static final String getOrgContextPath(HttpServletRequest request) {
        String org = (String) request.getAttribute("javax.servlet.forward.context_path");
        String res = (org != null) ? org : request.getContextPath();
        return res;
    }

    /**
   */
    public static final String getOrgServletPath(HttpServletRequest request) {
        String org = (String) request.getAttribute("javax.servlet.forward.servlet_path");
        String res = (org != null) ? org : request.getServletPath();
        return res;
    }

    /**
   */
    public static final String getOrgPathInfo(HttpServletRequest request) {
        String org = (String) request.getAttribute("javax.servlet.forward.path_info");
        String pi = (org != null) ? org : request.getPathInfo();
        return pi;
    }

    /**
   */
    public static final String getOrgQueryString(HttpServletRequest request) {
        String org = (String) request.getAttribute("javax.servlet.forward.query_string");
        String queryString = (org != null) ? org : request.getQueryString();
        return queryString;
    }

    /**
   * @param request
   * @return {@link #getOrgContextPath(HttpServletRequest)} + {@link #getOrgServletPath(HttpServletRequest)}
   */
    public static final String getOrgRootPath(HttpServletRequest request) {
        String contextPath = getOrgContextPath(request);
        if (contextPath == null) contextPath = "";
        String servletPath = getOrgServletPath(request);
        if (servletPath == null) servletPath = "";
        String rootPath = contextPath + servletPath;
        return rootPath;
    }

    /**
   * @param request
   * @return {@link #getOrgRootPath(HttpServletRequest)} + {@link #getOrgPathInfo(HttpServletRequest)}
   */
    public static final String getCurrentPageUri(HttpServletRequest request) {
        String rootPath = getOrgRootPath(request);
        String pathInfo = getOrgPathInfo(request);
        if (pathInfo == null) pathInfo = "";
        String currentPageUri = rootPath + pathInfo;
        return currentPageUri;
    }

    /**
   * @param request
   * @return {@link #getCurrentPageUri(HttpServletRequest)} + {@link #getOrgQueryString(HttpServletRequest)}
   */
    public static String getCurrentPage(HttpServletRequest request) {
        String currentPage = getCurrentPageUri(request);
        String queryString = getOrgQueryString(request);
        if (queryString != null) currentPage += "?" + queryString;
        return currentPage;
    }

    /**
   * @param path - If starts with a @ it is the servlet name.
   */
    public static RequestDispatcher getRequestDispatcher(ServletContext servletContext, HttpServletRequest request, String path) {
        if (path == null) throw new NullPointerException("null path");
        RequestDispatcher rd;
        if (path.startsWith("@")) rd = servletContext.getNamedDispatcher(path.substring(1)); else if (path.startsWith(".")) rd = request.getRequestDispatcher(path.substring(1)); else rd = servletContext.getRequestDispatcher(path);
        return rd;
    }

    /**
   * @return null means none, ie there is no active {@link FlowContext}.
   */
    public static File getTempDir() {
        FlowContext ctx = FlowContext.get();
        if (ctx == null) return null;
        return getTempDir(ctx.getServletContext());
    }

    public static File getTempDir(ServletContext sctx) {
        File dir = (File) sctx.getAttribute("javax.servlet.context.tempdir");
        return dir;
    }

    /**
   */
    public static Throwable[] getNestedExceptions(Throwable exception) {
        List<Throwable> ll = new ArrayList<Throwable>();
        while (exception != null) {
            ll.add(exception);
            exception = exception.getCause();
        }
        return (Throwable[]) ll.toArray(new Throwable[ll.size()]);
    }

    protected static void _dumpBuffer(PrintStream out, int[] chs) {
        for (int j = 0; j < chs.length; j++) {
            if (chs[j] == Integer.MIN_VALUE) out.print("  "); else out.print(Util.toHex((byte) chs[j]));
            out.print(" ");
        }
        out.print(" - ");
        for (int j = 0; j < chs.length; j++) {
            if (chs[j] == Integer.MIN_VALUE) out.print(" "); else if (chs[j] >= 0 && chs[j] < 32) out.print("?"); else out.print((char) chs[j]);
        }
    }

    /** */
    public static void dumpBuffer(PrintStream out, byte[] is) {
        if (is == null) return;
        int ch;
        int i = 0;
        int[] chs = new int[16];
        for (int j = 0; j < chs.length; j++) chs[j] = Integer.MIN_VALUE;
        for (int kk = 0; kk < is.length; kk++) {
            ch = is[kk];
            chs[i % chs.length] = ch;
            i++;
            if ((i % chs.length) == 0) {
                _dumpBuffer(out, chs);
                for (int j = 0; j < chs.length; j++) chs[j] = Integer.MIN_VALUE;
                out.println();
            }
        }
        if (chs[0] != Integer.MIN_VALUE) {
            _dumpBuffer(out, chs);
            out.println();
        }
    }

    /**
   * @throws IOException 
   */
    public static void dumpRequest(PrintStream out, ServletRequest request) throws IOException {
        out.println(request.getCharacterEncoding());
        InputStream is = request.getInputStream();
        int ch;
        int i = 0;
        int[] chs = new int[16];
        for (int j = 0; j < chs.length; j++) chs[j] = Integer.MIN_VALUE;
        while ((ch = is.read()) >= 0) {
            chs[i % chs.length] = ch;
            i++;
            if ((i % chs.length) == 0) {
                _dumpBuffer(out, chs);
                for (int j = 0; j < chs.length; j++) chs[j] = Integer.MIN_VALUE;
                out.println();
            }
        }
        if (chs[0] != Integer.MIN_VALUE) {
            _dumpBuffer(out, chs);
            out.println();
        }
    }

    /** */
    public static void dumpParameters(PrintStream out, HttpServletRequest request) {
        out.println("Parameters in this request:");
        for (Enumeration<?> e = request.getParameterNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String[] values = request.getParameterValues(key);
            out.print("   " + key + " = ");
            for (int i = 0; i < values.length; i++) {
                out.print("[" + values[i] + "] ");
            }
            out.println();
        }
        out.println();
    }

    /** */
    public static CharSequence dumpParameters(HttpServletRequest request) {
        ByteArrayOutputStream baos = new TmpByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        dumpParameters(out, request);
        out.close();
        return baos.toString();
    }

    /** */
    public static void dumpHeaders(PrintStream out, HttpServletRequest request) {
        out.println("Headers in this request:");
        for (Enumeration<?> e = request.getHeaderNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            Enumeration<?> values = request.getHeaders(key);
            out.print("   " + key + " = ");
            while (values.hasMoreElements()) {
                out.print(values.nextElement() + " ");
            }
            out.println();
        }
        out.println();
    }

    /**
   */
    public static void sendMail(Session session, String from, String to, String subject, String contentType, Object content) throws NamingException, MessagingException {
        final String ENCODING = "ISO8859_1";
        MimeMessage message = new MimeMessage(session);
        if (from != null) message.setFrom(new InternetAddress(from));
        {
            String tk;
            for (StringTokenizer st = new StringTokenizer(to, ";, \t\n\r\f"); st.hasMoreTokens(); ) {
                tk = st.nextToken();
                message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(tk));
            }
        }
        message.setSubject(subject, ENCODING);
        if (contentType == null || "text/plain".equals(contentType)) message.setText((String) content, ENCODING); else message.setContent(content, contentType);
        Transport.send(message);
    }

    /**
   * I properly configured ad email is sended providing informations about the captured error, otherwise the 
   * error is simply logged.<br>
   * see {@link #sendErrorMessage(String, String)} for informations about expected configuration
   * @return the root exception
   */
    public static Throwable sendErrorNotification(HttpServletRequest request, String pId, final Throwable exx) {
        final String id = (pId != null) ? pId : "" + System.identityHashCode(exx) + " / " + DATE_FORMAT.format(new Date());
        final Throwable[] rexx = getNestedExceptions(exx);
        final Throwable res = rexx[rexx.length - 1];
        String msg = null;
        try {
            StringWriter sw = new StringWriter(1024 * 2);
            PrintWriter out = new PrintWriter(sw, true);
            String currentPage = getCurrentPage(request);
            out.println("Error id   : " + id);
            out.println("CurrentPage: " + currentPage);
            out.println("Server Name: " + request.getServerName());
            out.println("Server Port: " + request.getServerPort());
            out.println("Remote Addr: " + request.getRemoteAddr());
            out.println("Remote Host: " + request.getRemoteHost());
            out.println("User-Agent : " + request.getHeader("User-Agent"));
            out.println("Request Is Secure: " + request.isSecure());
            out.println("Auth Type: " + request.getAuthType());
            out.println("HTTP Method: " + request.getMethod());
            out.println("Remote User: " + request.getRemoteUser());
            out.println("Request URI: " + getOrgRequestURI(request));
            out.println("Context Path: " + getOrgContextPath(request));
            out.println("Servlet Path: " + getOrgServletPath(request));
            out.println("Path Info: " + getOrgPathInfo(request));
            out.println("Path Trans: " + request.getPathTranslated());
            out.println("Char. encoding:" + request.getCharacterEncoding());
            out.println("Query String: " + getOrgQueryString(request));
            out.println("Requested Session Id: " + request.getRequestedSessionId());
            out.println("Nested exceptions: " + rexx[rexx.length - 1].getClass().getName() + " - " + rexx[rexx.length - 1].getMessage());
            for (int i = rexx.length - 2; i >= 0; i--) {
                out.println("                 : " + rexx[i].getClass().getName() + " - " + rexx[i].getMessage());
            }
            for (int i = 0; i < rexx.length; i++) {
                if (rexx[i] instanceof SQLException) {
                    SQLException sexx = (SQLException) rexx[i];
                    out.println("XOPEN SQLState: " + sexx.getSQLState());
                    out.println("Error code    : " + sexx.getErrorCode());
                    break;
                }
            }
            out.println();
            out.println("Parameters in this request:");
            {
                Map<?, ?> params = (Map<?, ?>) request.getAttribute(HttpRequestFilter.PARAMETERS_MAP);
                if (params == null) params = request.getParameterMap();
                for (Iterator<?> e = params.keySet().iterator(); e.hasNext(); ) {
                    String key = (String) e.next();
                    String[] values = (String[]) params.get(key);
                    out.print("   " + key + " = ");
                    for (int i = 0; i < values.length; i++) {
                        if (key != null && key.toLowerCase().indexOf("password") >= 0) out.print("********"); else out.print(values[i] + " ");
                    }
                    out.println();
                }
            }
            out.println();
            out.println("Stack trace:");
            rexx[rexx.length - 1].printStackTrace(out);
            out.close();
            sw.close();
            msg = sw.toString();
        } catch (Exception ex) {
            LOGGER.error("Cannot notify exception: " + id, exx);
            LOGGER.error("... an error occurred during message building:", ex);
            return res;
        }
        sendErrorMessage(id, msg);
        return res;
    }

    /**
   * I properly configured ad email is sended providing informations about the captured error, otherwise the 
   * error is simply logged.<br>
   * see {@link #sendErrorMessage(String, String)} for informations about expected configuration
   * @return the root exception
   */
    public static Throwable sendErrorNotification(final Throwable exx) {
        final String id = "" + System.identityHashCode(exx) + " / " + DATE_FORMAT.format(new Date());
        if (FlowContext.get() != null) {
            HttpServletRequest request = FlowContext.get().getServletRequest();
            if (request != null) {
                return sendErrorNotification(request, id, exx);
            }
        }
        final Throwable[] rexx = getNestedExceptions(exx);
        final Throwable res = rexx[rexx.length - 1];
        String msg = null;
        try {
            StringWriter sw = new StringWriter(1024 * 2);
            PrintWriter out = new PrintWriter(sw, true);
            out.println("Nested exceptions: " + rexx[rexx.length - 1].getClass().getName() + " - " + rexx[rexx.length - 1].getMessage());
            for (int i = rexx.length - 2; i >= 0; i--) {
                out.println("                 : " + rexx[i].getClass().getName() + " - " + rexx[i].getMessage());
            }
            for (int i = 0; i < rexx.length; i++) {
                if (rexx[i] instanceof SQLException) {
                    SQLException sexx = (SQLException) rexx[i];
                    out.println("XOPEN SQLState: " + sexx.getSQLState());
                    out.println("Error code    : " + sexx.getErrorCode());
                    break;
                }
            }
            out.println();
            out.println("Stack trace:");
            rexx[rexx.length - 1].printStackTrace(out);
            out.close();
            sw.close();
            msg = sw.toString();
        } catch (Exception ex) {
            LOGGER.error("Cannot notify exception: " + id, exx);
            LOGGER.error("... an error occurred during message building:", ex);
            return res;
        }
        sendErrorMessage(id, msg);
        return res;
    }

    /**
  /**
   * I properly configured ad email is sended with the given message, otherwise the 
   * message is simply logged.<br>
   * <pre>
   * These are the java:comp/env jndi resources to configure the service:
   *   ifw2/error/mailSession : the mail session (mandatory)
   *   ifw2/error/destinations : a comma separated list of destination recipes (mandatory)
   *   ifw2/error/sender : the mail sender (optional)
   * </pre>
   * @param id
   * @param msg
   */
    public static void sendErrorMessage(String id, String msg) {
        Session session = null;
        String destinations = null;
        String sender = null;
        {
            NamingException nex = null;
            try {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");
                session = (Session) envCtx.lookup("ifw2/error/mailSession");
                destinations = (String) envCtx.lookup("ifw2/error/destinations");
                sender = (String) envCtx.lookup("ifw2/error/sender");
            } catch (NamingException ex) {
                nex = ex;
            }
            if (session == null || destinations == null) {
                if (session == null) LOGGER.error("Cannot notify exception: " + id + ", ifw2/error/mailSession is not defined " + ((nex == null) ? "" : nex.getMessage())); else LOGGER.error("Cannot notify exception: " + id + ", ifw2/error/destinations is not defined " + ((nex == null) ? "" : nex.getMessage()));
                LOGGER.error(msg);
                return;
            }
        }
        final String ENCODING = "ISO8859_1";
        MimeMessage message = new MimeMessage(session);
        try {
            if (sender != null) message.setFrom(new InternetAddress(sender));
            {
                String tk;
                for (StringTokenizer st = new StringTokenizer(destinations, ";, \t\n\r\f"); st.hasMoreTokens(); ) {
                    tk = st.nextToken();
                    message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(tk));
                }
            }
            message.setSubject("Error id: " + id);
            message.setText(msg, ENCODING);
            Transport.send(message);
            LOGGER.error("Error notification " + id + " sended:");
            LOGGER.error(msg);
        } catch (Exception ex) {
            LOGGER.error("Cannot notify exception: " + id + " ...");
            LOGGER.error(msg);
            LOGGER.error("... an error occurred during e-mail delivery:", ex);
        }
    }
}
