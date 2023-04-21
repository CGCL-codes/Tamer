package net.sf.staccatocommons;

import java.util.AbstractList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author flbulgarelli
 * 
 */
public class DomEDynamic implements EDynamic {

    private Element element;

    public DomEDynamic(Element element) {
        this.element = element;
    }

    public Object value() {
        return element;
    }

    public EDynamic get(String field) {
        String attribute = element.getAttribute(field);
        if (attribute.length() != 0) return EDynamics.from(attribute);
        final NodeList elements = element.getElementsByTagName(field);
        return EDynamics.from(new AbstractList<EDynamic>() {

            public EDynamic get(int index) {
                return new DomEDynamic((Element) elements.item(index));
            }

            public int size() {
                return elements.getLength();
            }
        });
    }
}
