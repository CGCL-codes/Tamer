package org.qsari.effectopedia.core.embeddedobjects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.Element;
import org.jdom.Namespace;
import org.qsari.effectopedia.base.XMLExportable;
import org.qsari.effectopedia.base.XMLImportable;

public class KeyWords extends HashMap<Long, KeyWord> implements XMLImportable, XMLExportable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public void loadFromXMLElement(Element element, Namespace namespace) {
        if (element != null) {
            Element e = element.getChild("KeyWords", namespace);
            if (e != null) {
                int count = Integer.parseInt(e.getAttributeValue("count", namespace));
                List<Element> children = e.getChildren();
                if ((count != 0) && (children != null) && (children.size() == count)) {
                    clear();
                    Iterator<Element> iterator = children.iterator();
                    while (iterator.hasNext()) {
                        Element child = iterator.next();
                        KeyWord kw = new KeyWord(false);
                        kw.loadFromXMLElement(child, namespace);
                        put(kw.getID(), kw);
                    }
                }
            }
        }
    }

    public Element storeToXMLElement(Element element, Namespace namespace, boolean visualAttributes) {
        int count = size();
        Element e = new Element("KeyWords", namespace);
        e.setAttribute("count", Integer.toString(count), namespace);
        if (count != 0) {
            Iterator<Map.Entry<Long, KeyWord>> it = entrySet().iterator();
            while (it.hasNext()) {
                Element s = new Element("KeyWord", namespace);
                it.next().getValue().storeToXMLElement(s, namespace, visualAttributes);
                element.addContent(s);
            }
        }
        element.addContent(e);
        return element;
    }
}
