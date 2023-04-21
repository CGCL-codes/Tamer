package de.schlund.pfixxml.config.impl;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import de.schlund.pfixcore.workflow.app.ResdocFinalizer;
import de.schlund.pfixxml.config.ContextXMLServletConfig;

public class PagerequestFinalizerRule extends CheckedRule {

    public PagerequestFinalizerRule(ContextXMLServletConfig config) {
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        check(namespace, name, attributes);
        PageRequestConfigImpl pageConfig = (PageRequestConfigImpl) this.getDigester().peek();
        String className = attributes.getValue("class");
        if (className == null) {
            throw new SAXException("Mandatory attribute \"class\" is missing!");
        }
        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SAXException("Could not load class \"" + className + "\"!", e);
        }
        if (!ResdocFinalizer.class.isAssignableFrom(clazz)) {
            throw new SAXException("Finalizer class " + clazz + " on page " + pageConfig.getPageName() + " does not implement " + ResdocFinalizer.class + " interface!");
        }
        pageConfig.setFinalizer(clazz);
    }

    protected Map<String, Boolean> wantsAttributes() {
        HashMap<String, Boolean> atts = new HashMap<String, Boolean>();
        atts.put("class", true);
        return atts;
    }
}
