package org.jxpfw.jsp.tag.debug;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import org.jxpfw.jsp.AbstractJSPBean;
import org.jxpfw.jsp.tag.AbstractTag;
import org.jxpfw.jsp.tag.BeanTag;

/**
 * Custom JSP tag that aids in debugging jsp pages by showing debug info into
 * the output page.
 * Usage:<br>
 * &lt;jxpfwdebug:debugInfo/&gt;<br>
 * &lt;jxpfwdebug:debugInfo show="all|application|common|jxpfw|request|response|session"/&gt;
 * <br>Note: jxpfw info is only available when this tag is nested within a
 * jxpfw:bean tag.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.27 $
 */
public class DebugInfoTag extends AbstractTag {

    /**
     * All debug information.
     */
    public static final String ALL = "all";

    /**
     * Application related debug information.
     */
    public static final String APPLICATION = "application";

    /**
     * The most commonly used debug information.
     */
    public static final String COMMON = "common";

    /**
     * jxpfw specific debug information.
     */
    public static final String JXPFW = "jxpfw";

    /**
     * Request related debug information.
     */
    public static final String REQUEST = "request";

    /**
     * Response related debug information.
     */
    public static final String RESPONSE = "response";

    /**
     * Session related debug information.
     */
    public static final String SESSION = "session";

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 8387968178537655094L;

    /**
     * Type of debug information that has been requested, defaults to COMMON.
     */
    private String info = COMMON;

    /**
     * Gets the type of debug info that has been requested.
     * @return Debug type that has been requested.
     *  <br>The return value is either: {@link #ALL}, {@link #APPLICATION},
     *  {@link #COMMON}, {@link #REQUEST}, {@link #RESPONSE} or
     *  {@link #SESSION}.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Sets the requested type of debug info.
     * @param info Debug information that should be added to the page.
     *  <br>info should be one of: {@link #ALL}, {@link #APPLICATION},
     *  {@link #COMMON}, {@link #JXPFW}, {@link #REQUEST}, {@link #RESPONSE} or
     *  {@link #SESSION}.<br>
     *  If the supplied info is not in the list it will be changed to
     *  {@link #COMMON} which will give you a selection of the most commonly
     *  used debug info.
     */
    public void setInfo(final String info) {
        if (ALL.equals(info) || APPLICATION.equals(info) || JXPFW.equals(info) || REQUEST.equals(info) || RESPONSE.equals(info) || SESSION.equals(info)) {
            this.info = info;
        } else {
            this.info = COMMON;
        }
    }

    /**
     * Adds debug information to the page.
     * @return SKIP_BODY
     * @throws JspTagException When an error occurs while writing the debug info
     *  to the output stream.
     */
    @Override
    public int doStartTag() throws JspTagException {
        try {
            final JspWriter writer = pageContext.getOut();
            writer.println("<table border=\"1\">");
            if (ALL.equals(info)) {
                addJxpfwInfo(writer);
                addApplicationInfo(writer);
                addRequestInfo(writer);
                addResponseInfo(writer);
                addSessionInfo(writer);
            } else if (APPLICATION.equals(info)) {
                addApplicationInfo(writer);
            } else if (JXPFW.equals(info)) {
                addJxpfwInfo(writer);
            } else if (REQUEST.equals(info)) {
                addRequestInfo(writer);
            } else if (RESPONSE.equals(info)) {
                addResponseInfo(writer);
            } else if (SESSION.equals(info)) {
                addSessionInfo(writer);
            } else {
                addCommonInfo(writer);
            }
            writer.println("</table>");
        } catch (final IOException exception) {
            throw new JspTagException(exception.getMessage(), exception);
        }
        return SKIP_BODY;
    }

    /**
     * Adds jxpfw specific info to the supplied writer.
     * @param writer JspWriter to which the jxpfw info will be written.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    private void addJxpfwInfo(final JspWriter writer) throws IOException {
        writer.println("<tr><th colspan=\"2\">JXPFW specific info</th></tr>");
        final BeanTag beanTag = BeanTag.findAncestorBeanTag(this);
        if (beanTag == null) {
            addTableDataRow(writer, "Info unavailable", "Is this tag nested within a jxpfw:bean tag?");
        } else {
            final AbstractJSPBean jspBean = beanTag.getJSPBean();
            addTableHeaderRow(writer, "Method", "Value");
            addTableDataRow(writer, "getMode()", jspBean.getMode());
            addTableDataRow(writer, "getObjectID()", jspBean.getObjectID());
            addTableDataRow(writer, "getPassState()", jspBean.getPassState());
            addTableDataRow(writer, "getNextPassState()", jspBean.getNextPassState());
            addTableDataRow(writer, "getReturnURL()", jspBean.getReturnURL());
            addTableDataRow(writer, "getPageNumber()", jspBean.getPageNumber());
            addTableDataRow(writer, "#Generic Exceptions", String.valueOf(jspBean.getGenericExceptions().size()));
            addTableDataRow(writer, "#Ignorable Exceptions", String.valueOf(jspBean.getIgnorableExceptions().size()));
            addTableDataRow(writer, "#Property Exceptions", String.valueOf(jspBean.getPropertyExceptions().size()));
            addTableDataRow(writer, "#Messages", String.valueOf(jspBean.getGenericMessages().size()));
        }
    }

    /**
     * Adds application info to the supplied writer.
     * @param writer JspWriter to which the application info will be written.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    @SuppressWarnings("unchecked")
    private void addApplicationInfo(final JspWriter writer) throws IOException {
        final ServletContext context = pageContext.getServletContext();
        writer.println("<tr><th colspan=\"2\">Application info</th></tr>");
        addTableHeaderRow(writer, "Attribute", "Value");
        final Enumeration<String> attributes = (Enumeration<String>) context.getAttributeNames();
        while (attributes.hasMoreElements()) {
            final String attribute = attributes.nextElement();
            addTableDataRow(writer, attribute, context.getAttribute(attribute));
        }
        addTableHeaderRow(writer, "Init parameter", "Value");
        final Enumeration<String> initParameters = (Enumeration<String>) context.getInitParameterNames();
        while (initParameters.hasMoreElements()) {
            final String initParameter = initParameters.nextElement();
            addTableDataRow(writer, initParameter, context.getInitParameter(initParameter));
        }
        addTableHeaderRow(writer, "Method", "Value");
        addTableDataRow(writer, "getMajorVersion()", String.valueOf(context.getMajorVersion()));
        addTableDataRow(writer, "getMinorVersion()", String.valueOf(context.getMinorVersion()));
        addTableDataRow(writer, "getServerInfo()", context.getServerInfo());
    }

    /**
     * Adds common info to the supplied writer.
     * @param writer JspWriter to which the common info will be written.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    @SuppressWarnings("unchecked")
    private void addCommonInfo(final JspWriter writer) throws IOException {
        final HttpServletRequest request = getRequest();
        final HttpSession session = pageContext.getSession();
        addTableHeaderRow(writer, "Request Parameter", "Value");
        final Enumeration<String> parameters = (Enumeration<String>) request.getParameterNames();
        while (parameters.hasMoreElements()) {
            final String parameter = parameters.nextElement();
            addTableDataRow(writer, parameter, Arrays.toString(request.getParameterValues(parameter)));
        }
        addTableHeaderRow(writer, "Session attribute", "Value");
        final Enumeration<String> attributes = (Enumeration<String>) session.getAttributeNames();
        while (attributes.hasMoreElements()) {
            final String attribute = attributes.nextElement();
            addTableDataRow(writer, attribute, session.getAttribute(attribute));
        }
        addTableHeaderRow(writer, "Method", "Value");
        addTableDataRow(writer, "request.getQueryString()", request.getQueryString());
        addTableDataRow(writer, "request.getRequestURI()", request.getRequestURI());
    }

    /**
     * Adds request info to the supplied writer.
     * @param writer JspWriter to which the request info will be written.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    @SuppressWarnings("unchecked")
    private void addRequestInfo(final JspWriter writer) throws IOException {
        final HttpServletRequest request = getRequest();
        writer.println("<tr><th colspan=\"2\">Request info</th></tr>");
        addTableHeaderRow(writer, "Attribute", "Value");
        final Enumeration<String> attributes = (Enumeration<String>) request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            final String attribute = attributes.nextElement();
            addTableDataRow(writer, attribute, request.getAttribute(attribute));
        }
        addTableHeaderRow(writer, "Cookie", "Content");
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                final StringBuffer buffer = new StringBuffer();
                buffer.append("value=").append(cookies[i].getValue());
                buffer.append("<br>domain=").append(cookies[i].getDomain());
                buffer.append("<br>maxAge=").append(cookies[i].getMaxAge());
                buffer.append("<br>path=").append(cookies[i].getPath());
                buffer.append("<br>secure=").append(cookies[i].getSecure());
                addTableDataRow(writer, cookies[i].getName(), buffer);
            }
        }
        addTableHeaderRow(writer, "Header", "Value");
        final Enumeration<String> headers = (Enumeration<String>) request.getHeaderNames();
        while (headers.hasMoreElements()) {
            final String header = headers.nextElement();
            addTableDataRow(writer, header, request.getHeader(header));
        }
        addTableHeaderRow(writer, "Locale", "Value");
        final Enumeration<Locale> locales = request.getLocales();
        while (locales.hasMoreElements()) {
            final Locale locale = (Locale) locales.nextElement();
            addTableDataRow(writer, locale.getDisplayName(), locale);
        }
        addTableHeaderRow(writer, "Parameter", "Value");
        final Enumeration<String> parameters = (Enumeration<String>) request.getParameterNames();
        while (parameters.hasMoreElements()) {
            final String parameter = parameters.nextElement();
            addTableDataRow(writer, parameter, Arrays.toString(request.getParameterValues(parameter)));
        }
        addTableHeaderRow(writer, "Method", "Value");
        addTableDataRow(writer, "getAuthType()", request.getAuthType());
        addTableDataRow(writer, "getCharacterEncoding()", request.getCharacterEncoding());
        addTableDataRow(writer, "getContentLength()", String.valueOf(request.getContentLength()));
        addTableDataRow(writer, "getContentType()", request.getContentType());
        addTableDataRow(writer, "getContextPath()", request.getContextPath());
        addTableDataRow(writer, "getLocale()", request.getLocale());
        addTableDataRow(writer, "getMethod()", request.getMethod());
        addTableDataRow(writer, "getPathInfo()", request.getPathInfo());
        addTableDataRow(writer, "getPathTranslated()", request.getPathTranslated());
        addTableDataRow(writer, "getProtocol()", request.getProtocol());
        addTableDataRow(writer, "getQueryString()", request.getQueryString());
        addTableDataRow(writer, "getRemoteAddr()", request.getRemoteAddr());
        addTableDataRow(writer, "getRemoteHost()", request.getRemoteHost());
        addTableDataRow(writer, "getRemoteUser()", request.getRemoteUser());
        addTableDataRow(writer, "getRequestedSessionId()", request.getRequestedSessionId());
        addTableDataRow(writer, "getRequestURI()", request.getRequestURI());
        addTableDataRow(writer, "getRequestURL()", request.getRequestURL());
        addTableDataRow(writer, "getScheme()", request.getScheme());
        addTableDataRow(writer, "getServerName()", request.getServerName());
        addTableDataRow(writer, "getServerPort()", String.valueOf(request.getServerPort()));
        addTableDataRow(writer, "getServletPath()", request.getServletPath());
        addTableDataRow(writer, "getUserPrincipal()", request.getUserPrincipal());
        addTableDataRow(writer, "isRequestedSessionIdFromCookie()", String.valueOf(request.isRequestedSessionIdFromCookie()));
        addTableDataRow(writer, "isRequestedSessionIdFromURL()", String.valueOf(request.isRequestedSessionIdFromURL()));
        addTableDataRow(writer, "isRequestedSessionIdValid()", String.valueOf(request.isRequestedSessionIdValid()));
        addTableDataRow(writer, "isSecure()", String.valueOf(request.isSecure()));
    }

    /**
     * Adds response info to the supplied writer.
     * @param writer JspWriter to which the response info will be written.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    private void addResponseInfo(final JspWriter writer) throws IOException {
        final HttpServletResponse response = getResponse();
        writer.println("<tr><th colspan=\"2\">Response info</th></tr>");
        addTableHeaderRow(writer, "Method", "Value");
        addTableDataRow(writer, "getBufferSize()", String.valueOf(response.getBufferSize()));
        addTableDataRow(writer, "getCharacterEncoding()", response.getCharacterEncoding());
        addTableDataRow(writer, "getLocale()", response.getLocale());
        addTableDataRow(writer, "isCommitted()", String.valueOf(response.isCommitted()));
    }

    /**
     * Adds session info to the supplied writer.
     * @param writer JspWriter to which the session info will be written.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    @SuppressWarnings("unchecked")
    private void addSessionInfo(final JspWriter writer) throws IOException {
        final HttpSession session = pageContext.getSession();
        writer.println("<tr><th colspan=\"2\">Session info</th></tr>");
        addTableHeaderRow(writer, "Attribute", "Value");
        final Enumeration<String> attributes = (Enumeration<String>) session.getAttributeNames();
        while (attributes.hasMoreElements()) {
            final String attribute = attributes.nextElement();
            addTableDataRow(writer, attribute, session.getAttribute(attribute));
        }
        addTableHeaderRow(writer, "Method", "Value");
        addTableDataRow(writer, "getCreationTime()", String.valueOf(session.getCreationTime()));
        addTableDataRow(writer, "getId()", String.valueOf(session.getId()));
        addTableDataRow(writer, "getLastAccessedTime()", String.valueOf(session.getLastAccessedTime()));
        addTableDataRow(writer, "getMaxInactiveInterval()", String.valueOf(session.getMaxInactiveInterval()));
        addTableDataRow(writer, "isNew()", String.valueOf(session.isNew()));
    }

    /**
     * Adds a table header row to the supplied writer.
     * @param writer JspWriter to which the function name and value will be
     *  written.
     * @param firstColumn Data that will be placed in the first column.
     * @param secondColumn Data that will be placed in the second column.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    private static void addTableHeaderRow(final JspWriter writer, final String firstColumn, final String secondColumn) throws IOException {
        final StringBuffer buffer = new StringBuffer(100);
        buffer.append("\t<tr>\n");
        buffer.append("\t  <th>").append(firstColumn).append("</th>\n");
        buffer.append("\t  <th>").append(secondColumn).append("</th>\n");
        buffer.append("\t</tr>");
        writer.println(buffer.toString());
    }

    /**
     * Adds a table data row to the supplied writer.
     * @param writer JspWriter to which the function name and value will be
     *  written.
     * @param firstColumn Data that will be placed in the first column.
     * @param secondColumn Data that will be placed in the second column.
     * @throws IOException When an error occurs while writing the info to the
     *  writer.
     */
    private static void addTableDataRow(final JspWriter writer, final String firstColumn, final Object secondColumn) throws IOException {
        final StringBuffer buffer = new StringBuffer(100);
        buffer.append("\t<tr>\n");
        buffer.append("\t  <td>").append(firstColumn).append("</td>\n");
        buffer.append("\t  <td>").append(secondColumn).append("</td>\n");
        buffer.append("\t</tr>");
        writer.println(buffer.toString());
    }

    /**
     * Assures that the rest of the page is evaluated.
     * @return EVAL_PAGE.
     */
    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    /**
     * Clears attributes so the next tag won't re-use them incorrectly.
     */
    @Override
    public void release() {
        super.release();
        info = COMMON;
    }
}
