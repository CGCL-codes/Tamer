package edu.asu.itunesu;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An entire iTunesU site, composed of many {@link Section} objects.
 */
public class Site extends ITunesUElement {

    private String name;

    private String handle;

    private Boolean allowSubscription;

    private List<Permission> permissions;

    private List<Section> sections;

    private Templates templates;

    private String themeHandle;

    public Site() {
        this.permissions = new ArrayList<Permission>();
        this.sections = new ArrayList<Section>();
    }

    public Site(String name, String handle, Boolean allowSubscription, List<Permission> permissions, List<Section> sections, Templates templates, String themeHandle) {
        this.name = name;
        this.handle = handle;
        this.allowSubscription = allowSubscription;
        this.permissions = permissions;
        this.sections = sections;
        this.templates = templates;
        this.themeHandle = themeHandle;
    }

    public String getName() {
        return this.name;
    }

    public String getHandle() {
        return this.handle;
    }

    public Boolean getAllowSubscription() {
        return this.allowSubscription;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public Templates getTemplates() {
        return this.templates;
    }

    public String getThemeHandle() {
        return this.themeHandle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setAllowSubscription(Boolean allowSubscription) {
        this.allowSubscription = allowSubscription;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

    public void setThemeHandle(String themeHandle) {
        this.themeHandle = themeHandle;
    }

    public Element toXmlElement(Document doc) {
        Element element = doc.createElement("Site");
        if (this.name != null) {
            Element nameElement = doc.createElement("Name");
            nameElement.setTextContent(this.name);
            element.appendChild(nameElement);
        }
        if (this.handle != null) {
            Element handleElement = doc.createElement("Handle");
            handleElement.setTextContent(this.handle);
            element.appendChild(handleElement);
        }
        if (this.allowSubscription != null) {
            Element allowSubscriptionElement = doc.createElement("AllowSubscription");
            allowSubscriptionElement.setTextContent(this.allowSubscription ? "true" : "false");
            element.appendChild(allowSubscriptionElement);
        }
        for (Permission permission : this.permissions) {
            element.appendChild(permission.toXmlElement(doc));
        }
        for (Section section : this.sections) {
            element.appendChild(section.toXmlElement(doc));
        }
        if (this.templates != null) {
            element.appendChild(this.templates.toXmlElement(doc));
        }
        if (this.themeHandle != null) {
            Element themeHandleElement = doc.createElement("ThemeHandle");
            themeHandleElement.setTextContent(this.themeHandle);
            element.appendChild(themeHandleElement);
        }
        return element;
    }

    public static Site fromXmlElement(Element element) throws ITunesUException {
        if (!"Site".equals(element.getNodeName())) {
            throw new ITunesUException("Expected Site, got " + element.getNodeName());
        }
        String name = null;
        String handle = null;
        Boolean allowSubscription = null;
        List<Permission> permissions = new ArrayList<Permission>();
        List<Section> sections = new ArrayList<Section>();
        Templates templates = null;
        String themeHandle = null;
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if ("Name".equals(childNode.getNodeName())) {
                    name = childNode.getTextContent();
                } else if ("Handle".equals(childNode.getNodeName())) {
                    handle = childNode.getTextContent();
                } else if ("AllowSubscription".equals(childNode.getNodeName())) {
                    allowSubscription = "true".equals(childNode.getTextContent());
                } else if ("Permission".equals(childNode.getNodeName())) {
                    permissions.add(Permission.fromXmlElement((Element) childNode));
                } else if ("Section".equals(childNode.getNodeName())) {
                    sections.add(Section.fromXmlElement((Element) childNode));
                } else if ("Templates".equals(childNode.getNodeName())) {
                    templates = Templates.fromXmlElement((Element) childNode);
                } else if ("ThemeHandle".equals(childNode.getNodeName())) {
                    themeHandle = childNode.getTextContent();
                }
            }
        }
        return new Site(name, handle, allowSubscription, permissions, sections, templates, themeHandle);
    }

    public static Site fromXml(String xml) throws ITunesUException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ITunesUException(e);
        }
        Document doc;
        try {
            doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException e) {
            throw new ITunesUException(e);
        } catch (IOException e) {
            throw new ITunesUException(e);
        }
        return Site.fromXmlElement(doc.getDocumentElement());
    }

    public String toString() {
        return (super.toString() + "[name=" + (this.getName() == null ? "<null>" : this.getName()) + ",handle=" + (this.getHandle() == null ? "<null>" : this.getHandle()) + "]");
    }
}
