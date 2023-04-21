package com.j2xtreme.xbean;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
* @author <SCRIPT language="javascript">eval(unescape('%64%6f%63%75%6d%65%6e%74%2e%77%72%69%74%65%28%27%3c%61%20%68%72%65%66%3d%22%6d%61%69%6c%74%6f%3a%72%6f%62%40%6a%32%78%74%72%65%6d%65%2e%63%6f%6d%22%3e%52%6f%62%20%53%63%68%6f%65%6e%69%6e%67%3c%2f%61%3e%27%29%3b'))</SCRIPT>
 */
class XMLServiceConfigurationParser {

    static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(XMLServiceConfigurationParser.class);

    protected ServiceConfigurationImpl parseService(Element service) {
        ServiceConfigurationImpl config = new ServiceConfigurationImpl();
        config.serviceName = service.getAttribute(ServiceConfiguration.SERVICE_NAME_ATTRIBUTE);
        if (service.getAttributeNode(ServiceConfiguration.CLASS_ATTRIBUTE) != null) {
            config.className = service.getAttribute(ServiceConfiguration.CLASS_ATTRIBUTE);
        }
        if (service.getAttributeNode(ServiceConfiguration.SCOPE_ATTRIBUTE) != null) {
            config.setScopeName(service.getAttribute(ServiceConfiguration.SCOPE_ATTRIBUTE));
        }
        if (service.getAttributeNode(ServiceConfiguration.FACTORY_ATTRIBUTE) != null) {
            config.factory = service.getAttribute(ServiceConfiguration.FACTORY_ATTRIBUTE);
        }
        if (service.getAttributeNode(ServiceConfiguration.PRIORITY_ATTRIBUTE) != null) {
            config.explicitPriority = Integer.parseInt(service.getAttribute(ServiceConfiguration.PRIORITY_ATTRIBUTE));
        }
        if (service.getAttributeNode(ServiceConfiguration.USE_PROXY_ATTRIBUTE) != null) {
            config.useProxy = service.getAttribute(ServiceConfiguration.USE_PROXY_ATTRIBUTE);
        }
        if (service.getAttributeNode(ServiceConfiguration.LOADONSTARTUP_ATTRIBUTE) != null) {
            config.loadOnStartup = service.getAttribute(ServiceConfiguration.LOADONSTARTUP_ATTRIBUTE);
        }
        if (service.getAttributeNode(ServiceConfiguration.CONTRIBUTION_TARGET_ATTRIBUTE) != null) {
            config.setContributionTarget(service.getAttribute(ServiceConfiguration.CONTRIBUTION_TARGET_ATTRIBUTE));
        }
        NodeList nl = service.getElementsByTagName("property");
        for (int i = 0; i < nl.getLength(); i++) {
            Element propertyElement = (Element) nl.item(i);
            String propertyName = propertyElement.getAttribute("name");
            String encodingName = propertyElement.getAttribute("encoding");
            if (encodingName != null) {
                encodingName = encodingName.trim();
                if (encodingName.length() == 0) {
                    encodingName = null;
                }
            }
            String val = getTextContents(propertyElement);
            ServiceConfigurationPropertyImpl property = new ServiceConfigurationPropertyImpl(propertyName, val, encodingName);
            config.properties.put(propertyName, property);
        }
        return config;
    }

    private String getTextContents(Element element) {
        StringBuffer sb = new StringBuffer();
        Node n = element.getFirstChild();
        while (n != null) {
            try {
                Text text = (Text) n;
                String val = text.getData();
                if (val != null) {
                    sb.append(val);
                }
            } catch (ClassCastException e) {
            }
            n = n.getNextSibling();
        }
        return sb.toString();
    }

    public List parse(java.net.URL url) {
        LinkedList services = new LinkedList();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(url.toString());
            NodeList nl = document.getElementsByTagName("service");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                ServiceConfigurationImpl sc = parseService(e);
                services.add(sc);
                sc.source = url.toString();
                log.debug("Parsed: " + sc);
            }
            return services;
        } catch (ParserConfigurationException e) {
            throw new FrameworkInititializationException("Could not parse " + url, e);
        } catch (SAXException e) {
            throw new FrameworkInititializationException("Could not parse " + url, e);
        } catch (IOException e) {
            throw new FrameworkInititializationException("Could not parse " + url, e);
        }
    }
}
