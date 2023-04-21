package com.liferay.applicationbuilder.portlet;

import java.util.Map;
import javax.portlet.*;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * <a href="ScriptingPortletBeanDeclarator.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Alberto Montero
 * @author Raymond Augé
 *
 */
public class BeanDeclarator {

    public static void declareBSFBeans(BSFManager bsfManager, PortletConfig portletConfig, PortletContext portletContext, PortletRequest portletRequest, PortletResponse portletResponse) throws BSFException {
        PortletPreferences preferences = portletRequest.getPreferences();
        Map<String, String> userInfo = (Map<String, String>) portletRequest.getAttribute(PortletRequest.USER_INFO);
        bsfManager.declareBean("portletConfig", portletConfig, PortletConfig.class);
        bsfManager.declareBean("portletContext", portletContext, PortletContext.class);
        bsfManager.declareBean("portletRequest", portletRequest, PortletRequest.class);
        if (portletRequest instanceof ActionRequest) {
            bsfManager.declareBean("actionRequest", (ActionRequest) portletRequest, ActionRequest.class);
        } else if (portletRequest instanceof ResourceRequest) {
            bsfManager.declareBean("resourceRequest", (ResourceRequest) portletRequest, ResourceRequest.class);
        } else {
            bsfManager.declareBean("renderRequest", (RenderRequest) portletRequest, RenderRequest.class);
        }
        bsfManager.declareBean("portletResponse", portletResponse, PortletResponse.class);
        if (portletResponse instanceof ActionResponse) {
            bsfManager.declareBean("actionResponse", (ActionResponse) portletResponse, ActionResponse.class);
        } else if (portletResponse instanceof ResourceResponse) {
            bsfManager.declareBean("resourceResponse", (ResourceResponse) portletResponse, ResourceResponse.class);
        } else {
            bsfManager.declareBean("renderResponse", (RenderResponse) portletResponse, RenderResponse.class);
        }
        bsfManager.declareBean("preferences", preferences, PortletPreferences.class);
        bsfManager.declareBean("userInfo", userInfo, Map.class);
    }

    public static void declareRhinoBeans(Scriptable scope, PortletConfig portletConfig, PortletContext portletContext, PortletRequest portletRequest, PortletResponse portletResponse) {
        PortletPreferences preferences = portletRequest.getPreferences();
        Map<String, String> userInfo = (Map<String, String>) portletRequest.getAttribute(PortletRequest.USER_INFO);
        Object out = Context.javaToJS(System.out, scope);
        ScriptableObject.putProperty(scope, "out", out);
        ScriptableObject.putProperty(scope, "portletConfig", Context.javaToJS(portletConfig, scope));
        ScriptableObject.putProperty(scope, "portletContext", Context.javaToJS(portletContext, scope));
        ScriptableObject.putProperty(scope, "portletRequest", Context.javaToJS(portletRequest, scope));
        if (portletRequest instanceof ActionRequest) {
            ScriptableObject.putProperty(scope, "actionRequest", Context.javaToJS((ActionRequest) portletRequest, scope));
        } else if (portletRequest instanceof ResourceRequest) {
            ScriptableObject.putProperty(scope, "resourceRequest", Context.javaToJS((ResourceRequest) portletRequest, scope));
        } else {
            ScriptableObject.putProperty(scope, "renderRequest", Context.javaToJS((RenderRequest) portletRequest, scope));
        }
        ScriptableObject.putProperty(scope, "portletResponse", Context.javaToJS(portletResponse, scope));
        if (portletResponse instanceof ActionResponse) {
            ScriptableObject.putProperty(scope, "actionResponse", Context.javaToJS((ActionResponse) portletResponse, scope));
        } else if (portletResponse instanceof ResourceResponse) {
            ScriptableObject.putProperty(scope, "resourceResponse", Context.javaToJS((ResourceResponse) portletResponse, scope));
        } else {
            ScriptableObject.putProperty(scope, "renderResponse", Context.javaToJS((RenderResponse) portletResponse, scope));
        }
        ScriptableObject.putProperty(scope, "preferences", Context.javaToJS(preferences, scope));
        ScriptableObject.putProperty(scope, "userInfo", Context.javaToJS(userInfo, scope));
    }
}
