package openvend.handler;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.text.StrBuilder;
import org.w3c.dom.Element;
import openvend.main.I_OvPortletRequestHandler;
import openvend.main.I_OvRequestContext;
import openvend.main.I_OvServletRequestHandler;

/**
 * Creates the href for an xmldump link.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.6 $
 * @since 1.0
 */
public class OvXmlDumpHrefHandler extends A_OvRequestHandler implements I_OvServletRequestHandler, I_OvPortletRequestHandler {

    /**
	 * @see openvend.main.I_OvRequestHandler#handleRequest(openvend.main.I_OvRequestContext)
	 */
    public void handleRequest(I_OvRequestContext requestContext) throws Exception {
        StrBuilder xmlDumpLink = new StrBuilder();
        Map params = requestContext.getServletRequest().getParameterMap();
        if (!params.isEmpty()) {
            boolean addXmlDumpParam = false;
            Iterator i = params.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                String name = (String) entry.getKey();
                if ("xmldump".equalsIgnoreCase(name)) {
                    continue;
                }
                String[] values = (String[]) entry.getValue();
                if (values != null && values.length > 0) {
                    addXmlDumpParam = true;
                    for (int j = 0; j < values.length; j++) {
                        if (xmlDumpLink.length() > 0) {
                            xmlDumpLink.append("&");
                        }
                        xmlDumpLink.append(name).append("=").append(values[j]);
                    }
                }
            }
            if (addXmlDumpParam) {
                xmlDumpLink.append("&xmldump=true");
            } else {
                xmlDumpLink.append("xmldump=true");
            }
        } else {
            xmlDumpLink.append("xmldump=true");
        }
        Element xmlDumpElement = requestContext.getXmlModel().appendElement("xmldump");
        xmlDumpElement.setAttribute("href", xmlDumpLink.toString());
        executeNextHandler(requestContext);
    }
}
