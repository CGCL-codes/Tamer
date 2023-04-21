package org.dcm4chee.xero.model;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles the SAX events required to parse the XmlModel object.
 * @author bwallace
 */
public class XmlModelHandler extends DefaultHandler {

    static final Logger log = LoggerFactory.getLogger(XmlModelHandler.class);

    protected List<XmlModel> xmlModels = new ArrayList<XmlModel>();

    protected XmlModel root;

    /** Sets up an Xml model handler, with all default parameters/values */
    public XmlModelHandler(XmlModel root) {
        this.root = root;
    }

    /** Starts a new child element - registers the element with the parent element to ensure it gets parsed. */
    @SuppressWarnings("unchecked")
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        XmlModel parent = null;
        XmlModel child;
        if (localName != null) name = localName;
        if (xmlModels.size() > 0) {
            parent = xmlModels.get(xmlModels.size() - 1);
            List<XmlModel> children = (List<XmlModel>) parent.get(name);
            child = new XmlModel(attributes.getLength() + 1);
            if (children == null) {
                log.debug("Created nested child " + name + " on " + parent);
                children = new ArrayList<XmlModel>();
                parent.put(name, children);
                child.put("xmlFirst", true);
            } else {
                child.put("xmlFirst", false);
            }
            child.setParent(parent);
            children.add(child);
        } else {
            child = root;
        }
        for (int i = 0, n = attributes.getLength(); i < n; i++) {
            if (log.isDebugEnabled()) log.debug("Setting attribute " + attributes.getQName(i) + "=" + attributes.getValue(i) + " on " + child);
            child.put(attributes.getQName(i), attributes.getValue(i));
        }
        xmlModels.add(child);
    }

    /** Remove the last element */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        xmlModels.remove(xmlModels.size() - 1);
    }
}
