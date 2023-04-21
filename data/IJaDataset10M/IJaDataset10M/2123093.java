package com.aptana.ide.parsing.experimental;

import java.util.ArrayList;

/**
 * @author Kevin Lindsey
 */
public class Alternate {

    ArrayList _elements;

    /**
	 * Return this alternate's elements
	 * 
	 * @return Returns all elements in this alternate as an array
	 */
    public Element[] getElements() {
        return (Element[]) this._elements.toArray(new Element[0]);
    }

    /**
	 * Create a new instance of Alternate
	 */
    public Alternate() {
        this._elements = new ArrayList();
    }

    /**
	 * Add a new element to this alternate
	 * 
	 * @param element
	 *            The element to add to this alternate
	 */
    public void addElement(Element element) {
        this._elements.add(element);
    }

    /**
	 * Add a new element to this alternate
	 * 
	 * @param name
	 *            The name of the element
	 * @param type
	 *            The type of element
	 */
    public void addElement(String name, int type) {
        this._elements.add(new Element(name, type));
    }

    /**
	 * Return a string representation of this object
	 * 
	 * @return The string representation of this object
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this._elements.size() > 0) {
            sb.append(this._elements.get(0).toString());
            for (int i = 1; i < this._elements.size(); i++) {
                sb.append(" ").append(this._elements.get(i).toString());
            }
        }
        return sb.toString();
    }
}
