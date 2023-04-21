package br.ufsc.gsigma.portlets;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.VariableInfo;

public class DefineObjectsTag extends TagSupport {

    public static final String PORTLET_REQUEST = "javax.portlet.request";

    public static final String PORTLET_RESPONSE = "javax.portlet.response";

    public static final String PORTLET_CONFIG = "javax.portlet.config";

    /**
	 * Processes the <CODE>defineObjects</CODE> tag.
	 * 
	 * @return <CODE>SKIP_BODY</CODE>
	 */
    @Override
    public int doStartTag() throws JspException {
        PortletRequest renderRequest = (PortletRequest) pageContext.getRequest().getAttribute(PORTLET_REQUEST);
        RenderResponse renderResponse = (RenderResponse) pageContext.getRequest().getAttribute(PORTLET_RESPONSE);
        PortletConfig portletConfig = (PortletConfig) pageContext.getRequest().getAttribute(PORTLET_CONFIG);
        if (pageContext.getAttribute("renderRequest") == null) {
            pageContext.setAttribute("renderRequest", renderRequest, PageContext.PAGE_SCOPE);
        }
        if (pageContext.getAttribute("renderResponse") == null) {
            pageContext.setAttribute("renderResponse", renderResponse, PageContext.PAGE_SCOPE);
        }
        if (pageContext.getAttribute("portletConfig") == null) {
            pageContext.setAttribute("portletConfig", portletConfig, PageContext.PAGE_SCOPE);
        }
        if (pageContext.getAttribute("portletSessionScope") == null) {
            pageContext.setAttribute("portletSessionScope", createPortletSessionMap(renderRequest), PageContext.PAGE_SCOPE);
        }
        return SKIP_BODY;
    }

    public static class TEI extends TagExtraInfo {

        @Override
        public VariableInfo[] getVariableInfo(TagData tagData) {
            VariableInfo[] info = new VariableInfo[] { new VariableInfo("renderRequest", "javax.portlet.RenderRequest", true, VariableInfo.AT_BEGIN), new VariableInfo("renderResponse", "javax.portlet.RenderResponse", true, VariableInfo.AT_BEGIN), new VariableInfo("portletConfig", "javax.portlet.PortletConfig", true, VariableInfo.AT_BEGIN), new VariableInfo("portletSessionScope", "java.util.Map", true, VariableInfo.AT_BEGIN) };
            return info;
        }
    }

    private Map<String, Object> createPortletSessionMap(final PortletRequest renderRequest) {
        return new EnumMap() {

            @SuppressWarnings("unchecked")
            @Override
            public Enumeration<String> getNames() {
                return renderRequest.getPortletSession().getAttributeNames();
            }

            @Override
            public Object getValue(String name) {
                return renderRequest.getPortletSession().getAttribute(name);
            }
        };
    }

    private abstract static class EnumMap extends AbstractMap<String, Object> {

        public abstract Enumeration<String> getNames();

        public abstract Object getValue(String name);

        @Override
        public Set<Entry<String, Object>> entrySet() {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> names = getNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getValue(name));
            }
            return map.entrySet();
        }
    }
}
