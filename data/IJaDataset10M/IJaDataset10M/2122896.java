package openvend.handler;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import openvend.main.I_OvPortletRequestHandler;
import openvend.main.I_OvRequestContext;
import openvend.main.OvLog;
import openvend.portlet.A_OvPortletRequestContext;
import openvend.portlet.OvPortletLifecycleData;
import openvend.servlet.OvRequestAttributes;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;

/**
 * Displays in the render phase the exceptions thrown in the action phase of a portlet request.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.4 $
 * @since 1.0
 */
public class OvPortletExceptionHandler extends A_OvRequestHandler implements I_OvPortletRequestHandler {

    private static Log log = OvLog.getLog(OvPortletExceptionHandler.class);

    /**
	 * @see openvend.main.I_OvRequestHandler#handleRequest(openvend.main.I_OvRequestContext)
	 */
    public void handleRequest(I_OvRequestContext requestContext) throws Exception {
        if (requestContext.isPortletRequest()) {
            A_OvPortletRequestContext portletRequestContext = (A_OvPortletRequestContext) requestContext;
            try {
                if (portletRequestContext.isAfterPortletActionRequest()) {
                    OvPortletLifecycleData lifecycleData = (OvPortletLifecycleData) portletRequestContext.getTransientAttribute(OvRequestAttributes.TRANSIENT_REQUEST_ATTRIB_PORTLET_LIFECYCLE_DATA);
                    if (lifecycleData.hasException()) {
                        handleException(requestContext, lifecycleData.getException());
                        return;
                    }
                }
                executeNextHandler(requestContext);
            } catch (Exception e) {
                if (portletRequestContext.isPortletActionRequest()) {
                    OvPortletLifecycleData lifecycleData = (OvPortletLifecycleData) portletRequestContext.getTransientAttribute(OvRequestAttributes.TRANSIENT_REQUEST_ATTRIB_PORTLET_LIFECYCLE_DATA);
                    lifecycleData.setException(e);
                } else {
                    handleException(requestContext, e);
                }
            }
        }
    }

    protected void handleException(I_OvRequestContext requestContext, Exception e) throws UnsupportedEncodingException {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        StrBuilder buf = new StrBuilder();
        buf.append("<h1 style=\"color:#336699\">Internal server error</h1>\r\n");
        buf.append("<hr/>\r\n");
        buf.append("<p>The server encountered an internal error that prevented it from fulfilling this request:</p>\r\n");
        buf.append("<br/><p style=\"font-weight:bold;\">").append(e.getMessage()).append("</p><br/>\r\n");
        buf.append("<p>Please contact your system administrator to check the log files for further error messages.</p>\r\n");
        buf.append("<h4>").append(new java.util.Date().toString()).append("</h4>\r\n");
        buf.append("<p><a href=\"#\" onClick=\"javascript:history.back();\">Back...</a></p>\r\n");
        buf.append(OvServletExceptionHandler.TRASH);
        requestContext.setContentType("text/html");
        requestContext.setOutput(buf.toString().getBytes(CharEncoding.UTF_8));
        requestContext.setContentEncoding(CharEncoding.UTF_8);
        requestContext.getI18nContext().setContentLanguage(Locale.ENGLISH);
    }
}
