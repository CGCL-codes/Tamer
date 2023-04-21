package gov.fnal.mcas.portlets;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.RenderRequest;

public class HeatMap extends gov.fnal.mcas.portlets.base.XsltPortlet {

    public String getXsltScript() {
        return "/WEB-INF/lib/door-heatMap.xsl";
    }

    public Map<String, String> getXsltParams(RenderRequest request) throws Exception {
        String portletTitle = request.getPreferences().getValue("PortletTitle", "HeatMap");
        Map<String, String> params = new HashMap<String, String>();
        params.put("PortletTitle", portletTitle);
        params.put("FunctionName", "draw" + portletTitle);
        return params;
    }
}
