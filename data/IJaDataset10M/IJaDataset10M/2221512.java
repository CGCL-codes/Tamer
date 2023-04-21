package net.sf.opentranquera.dynabean.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class DefaultSerializer implements BeanSerializer {

    private static final String STR_ITEM = "ITEM";

    public Element toDom(Element dst, Object obj) {
        if (obj != null) {
            dst.setAttribute(STR_TYPE, obj.getClass().getName());
            Text data = dst.getOwnerDocument().createTextNode(obj.toString());
            dst.appendChild(data);
        }
        return dst;
    }
}
