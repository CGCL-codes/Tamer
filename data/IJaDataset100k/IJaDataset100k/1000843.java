package de.schlund.pfixxml.config;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class PagerequestRule extends CheckedRule {

    private ContextXMLServletConfig config;

    public PagerequestRule(ContextXMLServletConfig config) {
        this.config = config;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        check(namespace, name, attributes);
        ContextConfig ctxConfig = config.getContextConfig();
        PageRequestConfig pageConfig = new PageRequestConfig();
        String pageName = attributes.getValue("name");
        if (pageName == null) {
            throw new SAXException("Mandatory attribute \"name\" is missing!");
        }
        pageConfig.setPageName(pageName);
        String copyfrom = attributes.getValue("copyfrom");
        if (copyfrom != null) {
            pageConfig.setCopyFromPage(copyfrom);
        }
        String nostore = attributes.getValue("nostore");
        if (nostore != null) {
            pageConfig.setStoreXML(!Boolean.parseBoolean(nostore));
        } else {
            pageConfig.setStoreXML(true);
        }
        pageConfig.setDefaultStaticState(config.getDefaultStaticState());
        pageConfig.setDefaultIHandlerState(config.getDefaultIHandlerState());
        ctxConfig.addPageRequest(pageConfig);
        this.getDigester().push(pageConfig);
    }

    public void end(String namespace, String name) throws Exception {
        this.getDigester().pop();
    }

    protected Map<String, Boolean> wantsAttributes() {
        HashMap<String, Boolean> atts = new HashMap<String, Boolean>();
        atts.put("name", true);
        atts.put("nostore", false);
        atts.put("copyfrom", false);
        return atts;
    }
}
