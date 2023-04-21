package com.psm.core.plugin.tag;

import org.w3c.dom.Node;
import com.psm.core.plugin.PluginUrlMapping;

public class TagInternalUrl {

    public TagInternalUrl(String urlPadre, Node node, PluginUrlMapping pluginUrlMapping) {
        String id = node.getAttributes().getNamedItem("id").getTextContent();
        String beanId = node.getAttributes().getNamedItem("bean-id").getTextContent();
        pluginUrlMapping.registraUrl(urlPadre + "/" + id + ".page", beanId);
    }
}
