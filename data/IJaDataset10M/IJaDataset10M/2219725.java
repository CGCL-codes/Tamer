package org.chiba.web.flux;

import org.chiba.xml.dom.DOMUtil;
import org.chiba.xml.events.ChibaEventNames;
import org.chiba.xml.events.XMLEvent;
import org.chiba.xml.xforms.XFormsConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * EventLog logs all events happening in XForms processor and build a DOM
 * document which represent those events.
 *
 * @author Joern Turner
 * @version $Id: EventLog.java,v 1.1 2006/09/10 19:50:45 joernt Exp $
 */
public class EventLog {

    private static List HELPER_ELEMENTS = Arrays.asList(new String[] { XFormsConstants.LABEL, XFormsConstants.HELP, XFormsConstants.HINT, XFormsConstants.ALERT, XFormsConstants.VALUE });

    private static List SELECTOR_ELEMENTS = Arrays.asList(new String[] { XFormsConstants.SELECT1, XFormsConstants.SELECT });

    private Document doc;

    private Element root;

    private Element selector;

    public EventLog() {
        this.doc = DOMUtil.newDocument(false, false);
        this.root = this.doc.createElement("eventlog");
        this.root.setAttribute("id", "eventlog");
        this.doc.appendChild(this.root);
    }

    public Element getLog() {
        return (Element) this.root.cloneNode(true);
    }

    public void add(XMLEvent event) {
        String type = event.getType();
        Element target = (Element) event.getTarget();
        String targetId = target.getAttributeNS(null, "id");
        String targetName = target.getLocalName();
        Element element;
        if (ChibaEventNames.STATE_CHANGED.equals(type) && SELECTOR_ELEMENTS.contains(targetName)) {
            element = insert(null, type, targetId, targetName);
            if (this.selector == null) {
                this.selector = element;
            }
        } else {
            element = insert(this.selector, type, targetId, targetName);
        }
        if (ChibaEventNames.STATE_CHANGED.equals(type) && HELPER_ELEMENTS.contains(targetName)) {
            String parentId = ((Element) target.getParentNode()).getAttributeNS(null, "id");
            addProperty(element, "parentId", parentId);
        }
        Iterator iterator = event.getPropertyNames().iterator();
        String name;
        Object context;
        while (iterator.hasNext()) {
            name = (String) iterator.next();
            context = event.getContextInfo(name);
            addProperty(element, name, context != null ? context.toString() : null);
        }
    }

    public Element add(String type, String targetId, String targetName) {
        return insert(this.selector, type, targetId, targetName);
    }

    public Element addProperty(Element element, String name, String value) {
        Element property = this.doc.createElement("property");
        property.setAttribute("name", name);
        if (value != null) {
            property.appendChild(this.doc.createTextNode(value));
        }
        element.appendChild(property);
        return element;
    }

    public Element addProperty(Element element, String name, Element value) {
        Element property = this.doc.createElement("property");
        property.setAttribute("name", name);
        property.appendChild(value);
        element.appendChild(property);
        return element;
    }

    private Element insert(Element ref, String type, String targetId, String targetName) {
        Element element = this.doc.createElement("event");
        this.root.insertBefore(element, ref);
        element.setAttribute("type", type);
        element.setAttribute("targetId", targetId);
        element.setAttribute("targetName", targetName);
        return element;
    }

    public void flush() {
        DOMUtil.removeAllChildren(this.root);
        this.selector = null;
    }
}
