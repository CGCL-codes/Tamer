package de.iritgo.openmetix.core.resource.resourcexmlparser;

import org.jdom.Element;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ElementIterator.
 *
 * @version $Id: ElementIterator.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ElementIterator implements Iterator {

    private int pos = 0;

    private Element element;

    private List elementList = null;

    private LinkedList stack = new LinkedList();

    private String path = "";

    private ElementContainer elementContainer = null;

    public ElementIterator(Element element) {
        this.element = (Element) element;
        this.elementList = this.element.getChildren();
        elementContainer = new ElementContainer(this.element, 0, this.element.getAttribute("treename").getValue());
        path = this.element.getAttribute("treename").getValue();
    }

    public boolean hasNext() {
        return (pos < elementList.size() || (stack.size() > 0));
    }

    public Object next() {
        if (pos < elementList.size()) {
            stack.addFirst(new ElementContainer(element, pos, path));
            element = (Element) elementList.get(pos);
            if (element.getAttribute("treename") != null) {
                path = path + "." + element.getAttribute("treename").getValue();
            } else {
                path = path + ".classdev";
            }
            elementList = element.getChildren();
            pos = 0;
            elementContainer = new ElementContainer(element, pos, path);
            return elementContainer;
        }
        if ((stack.size() > 0) && (pos >= elementList.size())) {
            elementContainer = (ElementContainer) stack.removeFirst();
            pos = elementContainer.getPos();
            path = elementContainer.getPath();
            pos++;
            element = elementContainer.getElement();
            elementList = element.getChildren();
        }
        return elementContainer;
    }

    public Object current() {
        return elementContainer;
    }

    public void remove() {
    }
}
