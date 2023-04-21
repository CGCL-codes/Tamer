package openvend.component;

import java.util.Iterator;
import java.util.Map;
import javax.portlet.PortletPreferences;
import openvend.cgi.OvCgiSubmission;
import openvend.cgi.OvNumberCgiValue;
import openvend.event.OvClearCacheEvent;
import openvend.lang.OvStringUtils;
import openvend.main.I_OvCgiValue;
import openvend.main.I_OvRequestContext;
import openvend.main.OvLog;
import openvend.main.OvXmlModel;
import openvend.portlet.A_OvPortletRequestContext;
import openvend.portlet.OvPortletActionRequestContext;
import openvend.portlet.OvPortletLifecycleData;
import openvend.portlet.OvPortletRenderRequestContext;
import openvend.service.OvEventDispatcherService;
import openvend.servlet.OvRequestAttributes;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 * Stores portlet preferences.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.8 $
 * @since 1.0
 */
public class OvPortletPreferencesComponent extends A_OvComponent {

    private static Log log = OvLog.getLog(OvPortletPreferencesComponent.class);

    protected static final int ACTION_SAVE = 1;

    /**
     * @see openvend.component.A_OvComponent#handleRequest(openvend.main.I_OvRequestContext)
     */
    public void handleRequest(I_OvRequestContext requestContext) throws Exception {
        if (requestContext.isPortletRequest()) {
            A_OvPortletRequestContext portletRequestContext = (A_OvPortletRequestContext) requestContext;
            if (portletRequestContext.isPortletActionRequest()) {
                handleActionRequest((OvPortletActionRequestContext) portletRequestContext);
            } else if (portletRequestContext.isAfterPortletActionRequest()) {
                handlePostActionRequest((OvPortletRenderRequestContext) portletRequestContext);
            }
        }
    }

    protected void handlePostActionRequest(OvPortletRenderRequestContext renderRequestContext) {
        OvPortletLifecycleData portletLifecycleData = (OvPortletLifecycleData) renderRequestContext.getTransientAttribute(OvRequestAttributes.TRANSIENT_REQUEST_ATTRIB_PORTLET_LIFECYCLE_DATA);
        DocumentFragment fragment = (DocumentFragment) portletLifecycleData.getAttribute(getId());
        OvXmlModel xmlModel = renderRequestContext.getXmlModel();
        xmlModel.appendDocumentFragmet(xmlModel.getDocument().getDocumentElement(), fragment);
    }

    protected void handleActionRequest(OvPortletActionRequestContext actionRequestContext) {
        OvXmlModel xmlModel = actionRequestContext.getXmlModel();
        Element componentElement = xmlModel.appendElement(getId());
        OvCgiSubmission submission = null;
        try {
            int action = -1;
            submission = new OvCgiSubmission(actionRequestContext, getCgiParams());
            OvNumberCgiValue cgiAction = (OvNumberCgiValue) submission.getCgiValue("action");
            if (!cgiAction.isEmpty() && cgiAction.isValid()) {
                action = cgiAction.getIntValue();
            }
            switch(action) {
                case ACTION_SAVE:
                    actionSave(actionRequestContext, xmlModel, componentElement, submission);
                    break;
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error handling request!", e);
            }
            xmlModel.appendElement(componentElement, "error", "Error handling request!");
        } finally {
            xmlModel.appendCgiParams(componentElement, submission, true);
            DocumentFragment fragment = xmlModel.asDocumentFragment(componentElement);
            OvPortletLifecycleData portletLifecycleData = (OvPortletLifecycleData) actionRequestContext.getTransientAttribute(OvRequestAttributes.TRANSIENT_REQUEST_ATTRIB_PORTLET_LIFECYCLE_DATA);
            portletLifecycleData.setAttribute(getId(), fragment);
        }
    }

    protected void actionSave(OvPortletActionRequestContext actionRequestContext, OvXmlModel xmlModel, Element componentElement, OvCgiSubmission submission) {
        try {
            if (submission.isValid()) {
                String prefix = getParam("cgi-param-prefix", "preference_");
                PortletPreferences portletPreferences = actionRequestContext.getActionRequest().getPreferences();
                Map cgiValues = submission.getCgiValues();
                Iterator i = cgiValues.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry entry = (Map.Entry) i.next();
                    String name = (String) entry.getKey();
                    if (!name.startsWith(prefix)) {
                        continue;
                    }
                    I_OvCgiValue cgiParamValue = (I_OvCgiValue) entry.getValue();
                    String[] values = cgiParamValue.getStringValues();
                    if (values != null && values.length > 0 && OvStringUtils.isNotEmpty(values)) {
                        portletPreferences.setValues(name, values);
                        if (log.isDebugEnabled()) {
                            log.debug("Set portlet preference '" + name + "' with value(s) '" + new StrBuilder().appendAll(values).toString() + "'");
                        }
                    } else {
                        portletPreferences.reset(name);
                        if (log.isDebugEnabled()) {
                            log.debug("Reset portlet preference '" + name + "'");
                        }
                    }
                }
                portletPreferences.store();
                if (log.isDebugEnabled()) {
                    log.debug("Stored portlet preferences!");
                }
                OvEventDispatcherService eventDispatcherService = (OvEventDispatcherService) actionRequestContext.getConfig().getServiceByAttribute("event-dispatcher-service-id");
                eventDispatcherService.fireEvent(new OvClearCacheEvent());
                xmlModel.appendElement(componentElement, "success", "true");
            } else {
                xmlModel.appendElement(componentElement, "error", "Form has invalid values!");
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error saving portlet preferences!", e);
            }
            xmlModel.appendElement(componentElement, "error", "Error saving portlet preferences!");
        }
    }
}
