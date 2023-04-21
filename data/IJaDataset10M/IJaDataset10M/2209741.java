package gov.fnal.mcas.portlets;

import java.util.HashMap;
import java.util.Map;
import gov.fnal.mcas.portlets.base.XsltPortlet;
import javax.portlet.RenderRequest;

public class GenericTableView extends XsltPortlet {

    public String getXsltScript() {
        return "WEB-INF/lib/genericTableView.xsl";
    }

    public Map<String, String> getXsltParams(RenderRequest request) throws Exception {
        String numberOfLinesDisplayed = request.getPreferences().getValue("NumberOfLinesDisplayed", "100");
        String portletTitle = request.getPreferences().getValue("PortletTitle", "GenericTableView");
        String tableNumber = request.getPreferences().getValue("TableNumber", "1");
        String thresholdHigh = request.getPreferences().getValue("ThresholdHigh", "-1");
        String thresholdLow = request.getPreferences().getValue("ThresholdLow", "-1");
        Map<String, String> params = new HashMap<String, String>();
        params.put("NumberOfLinesDisplayed", numberOfLinesDisplayed);
        params.put("PortletTitle", portletTitle);
        params.put("TableNumber", tableNumber);
        params.put("ThresholdHigh", thresholdHigh);
        params.put("ThresholdLow", thresholdLow);
        return params;
    }
}
