package de.schlund.pfixxml.config.impl;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ContextRule extends CheckedRule {

    private ContextXMLServletConfigImpl config;

    public ContextRule(ContextXMLServletConfigImpl config) {
        this.config = config;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        check(namespace, name, attributes);
        ContextConfigImpl ctxConfig = new ContextConfigImpl();
        ctxConfig.setNavigationFile(this.config.getDependFile());
        String defaultFlow = attributes.getValue("defaultflow");
        if (defaultFlow == null) {
            throw new SAXException("Mandatory attribute \"defaultflow\" is missing!");
        }
        ctxConfig.setDefaultFlow(defaultFlow);
        String authPage = attributes.getValue("authpage");
        if (authPage != null) {
            ctxConfig.setAuthPage(authPage);
        }
        String syncStr = attributes.getValue("synchronized");
        if (syncStr != null) {
            ctxConfig.setSynchronized(Boolean.parseBoolean(syncStr));
        } else {
            ctxConfig.setSynchronized(true);
        }
        this.config.setContextConfig(ctxConfig);
        this.getDigester().push(ctxConfig);
    }

    public void end(String namespace, String name) throws Exception {
        this.getDigester().pop();
    }

    protected Map<String, Boolean> wantsAttributes() {
        HashMap<String, Boolean> atts = new HashMap<String, Boolean>();
        atts.put("defaultflow", true);
        atts.put("authpage", false);
        atts.put("synchronized", false);
        return atts;
    }
}
