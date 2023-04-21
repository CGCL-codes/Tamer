package org.xmi.xml.reader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import org.xmi.infoset.ExtensionElement;
import org.xmi.infoset.XMIExtension;
import org.xmi.infoset.impl.ExtensionElementImpl;
import org.xmi.infoset.impl.XMIExtensionImpl;
import org.xmi.xml.reader.parser.ElementIterator;
import org.xmi.xml.reader.parser.Event;
import org.xmlpull.v1.XmlPullParser;

public class ExtensionReader {

    public XMIExtension parseExtension(Event e, ElementIterator iXMI) throws IOException {
        XMIExtension ext = new XMIExtensionImpl();
        ext.setLinenumber(e.linenumer);
        if (e.getProperty("", "extender") != null && e.getProperty("", "extender").size() > 0) ext.setExtender(e.getProperty("", "extender").get(0));
        if (e.getProperty("", "extenderID") != null && e.getProperty("", "extenderID").size() > 0) ext.setExtenderId(e.getProperty("", "extenderID").get(0));
        ext.setXmiNamespace(e.namespace);
        ext.setType(e.name);
        ext.setXmiId("Extension-" + e.linenumer);
        Map<String, List<String>> values = new HashMap<String, List<String>>();
        for (Entry<List<String>, List<String>> entry : e.properties.entrySet()) {
            if (!entry.getKey().get(1).equals("extender") && !entry.getKey().get(1).equals("extenderID")) {
                values.put(entry.getKey().get(1), entry.getValue());
            }
        }
        ext.setValues(values);
        Event myEvent = iXMI.next();
        List<ExtensionElement> rootElements = new ArrayList<ExtensionElement>();
        Stack<ExtensionElement> elements = null;
        do {
            if (myEvent.type == XmlPullParser.START_TAG) {
                ExtensionElement el = getElement(myEvent);
                if (elements == null) {
                    rootElements.add(el);
                    elements = new Stack<ExtensionElement>();
                    elements.push(el);
                } else {
                    elements.peek().addChild(el);
                    elements.push(el);
                }
            } else if (myEvent.type == XmlPullParser.END_TAG) {
                if (elements != null && !elements.isEmpty()) {
                    elements.pop();
                    if (elements.isEmpty() || elements.size() == 1) elements = null;
                }
            }
            myEvent = iXMI.next();
        } while (!(myEvent.type == XmlPullParser.END_TAG && myEvent.name.equals("Extension")));
        ext.setRootElements(rootElements);
        return ext;
    }

    private ExtensionElement getElement(Event e) {
        ExtensionElement el = new ExtensionElementImpl();
        el.setName(e.name);
        el.setNamespace(e.namespace);
        Map<String, List<String>> values = new HashMap<String, List<String>>();
        for (Entry<List<String>, List<String>> entry : e.properties.entrySet()) {
            values.put(entry.getKey().get(1), entry.getValue());
        }
        el.setValues(values);
        return el;
    }
}
