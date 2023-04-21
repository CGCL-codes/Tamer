package gov.fnal.mcas.portlets;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.RenderRequest;

public class ImageViewerV2 extends gov.fnal.mcas.portlets.base.XsltPortlet {

    public String getXsltScript() {
        return "WEB-INF/lib/ImageViewerV2Transform.xsl";
    }

    @Override
    public Map<String, String> getXsltParams(RenderRequest request) throws Exception {
        String numberOfColumns = request.getPreferences().getValue("NumberOfColumns", "3");
        Map<String, String> params = new HashMap<String, String>();
        params.put("NumberOfColumns", numberOfColumns);
        return params;
    }
}
