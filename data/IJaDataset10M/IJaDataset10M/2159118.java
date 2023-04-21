package de.iritgo.aktera.ui.ng;

import de.iritgo.simplelife.string.StringTools;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ConfigurationBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        if (element.hasAttribute("forward")) {
            builder.addPropertyValue("forward", element.getAttribute("forward"));
        }
        if (element.hasAttribute("bundle")) {
            builder.addPropertyValue("bundle", element.getAttribute("bundle"));
        }
        if (element.hasAttribute("security")) {
            builder.addPropertyValue("security", element.getAttribute("security"));
        }
        builder.addPropertyReference("logger", "de.iritgo.aktera.logger.Logger");
        Configuration configuration = createConfigurationFromElement(element);
        builder.addPropertyValue("configuration", configuration);
    }

    private Configuration createConfigurationFromElement(Node element) {
        DefaultConfiguration configuration = new DefaultConfiguration(element.getLocalName());
        NamedNodeMap attributeNodes = element.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); ++i) {
                Node attributeNode = attributeNodes.item(i);
                configuration.setAttribute(attributeNode.getLocalName(), attributeNode.getTextContent());
            }
        }
        StringBuilder value = new StringBuilder();
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            Node childNode = childNodes.item(i);
            if (Node.TEXT_NODE == childNode.getNodeType()) {
                value.append(childNode.getTextContent());
            } else if (Node.CDATA_SECTION_NODE == childNode.getNodeType()) {
                value.append(StringTools.trim(childNode.getTextContent()));
            } else {
                Configuration childConfiguration = createConfigurationFromElement(childNode);
                configuration.addChild(childConfiguration);
            }
        }
        if (StringTools.isNotTrimEmpty(value)) {
            configuration.setValue(StringTools.trim(value));
        }
        return configuration;
    }
}
