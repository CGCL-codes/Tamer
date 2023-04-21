package com.makeabyte.jhosting.server.io.xml;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

public class XMLPluginParser extends XMLParser {

    private Log log = LogFactory.getLog(XMLPluginParser.class);

    /**
	    * Constructor - Initalize plugin XML parser
	    * 
	    * @param url Local or remote path to jhosting-plugin.xml
	    * @throws XMLParserException
	    */
    public XMLPluginParser(String url) throws XMLParserException {
        super(url);
        log.info("Parsing plugin " + url);
    }

    /**
	    * Returns a map of attributes for plugin element
	    * @return
	    */
    public Map<String, String> getAttributes() {
        Map<String, String> map = new HashMap<String, String>();
        Element plugin = root.getChild("plugin");
        map.put("id", plugin.getAttribute("id").getValue());
        map.put("name", plugin.getAttribute("name").getValue());
        map.put("type", plugin.getAttribute("type").getValue());
        map.put("status", plugin.getAttribute("status").getValue());
        map.put("version", plugin.getAttribute("version").getValue());
        map.put("classpath", plugin.getAttribute("classpath").getValue());
        map.put("view", plugin.getAttribute("view").getValue());
        map.put("icon", plugin.getAttribute("icon").getValue());
        log.info("Plugin configuration " + map.toString());
        return map;
    }

    public String[] getDependencies() {
        String[] deps = new String[10];
        return deps;
    }
}
