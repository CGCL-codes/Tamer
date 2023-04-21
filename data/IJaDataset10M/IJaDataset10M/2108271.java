package org.jpox.samples.detach;

import java.util.ArrayList;
import java.util.List;

/**
 * Object containing a List that will be detached.
 * 
 * @version $Revision: 1.1 $
 */
public class DetachList {

    String name;

    List elements = new ArrayList();

    public DetachList(String name) {
        this.name = name;
    }

    public void addElement(DetachListElement element) {
        elements.add(element);
    }

    public List getElements() {
        return elements;
    }

    public int getNumberOfElements() {
        return elements.size();
    }

    public String getName() {
        return name;
    }
}
