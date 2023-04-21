package org.formaria.swing;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;
import java.awt.SystemColor;

/**
 * A dummy component type, used if no other component wrapper can be found
 * <p>Copyright (c) Formaria Ltd., <br>
 * License:      see license.txt
 * $Revision: 2.4 $
 */
public class Unknown extends Label {

    private Vector attributes;

    private Vector attributeNames;

    /**
   * Create a new unknown component
   */
    public Unknown() {
        attributes = new Vector();
        attributeNames = new Vector();
        attributes.add("Unknown");
        attributeNames.add("type");
        attributeNames.add("name");
        attributes.add("");
        attributeNames.add("x");
        attributes.add("0");
        attributeNames.add("y");
        attributes.add("0");
        attributeNames.add("w");
        attributes.add("100");
        attributeNames.add("h");
        attributes.add("20");
        attributeNames.add("style");
        attributes.add("");
    }

    /**
   * Get the component type name
   * @return the type
   */
    public String getTypeName() {
        return (String) attributes.elementAt(0);
    }

    /**
   * Render the component
   * @param g the graphics context
   */
    public void paint(Graphics g) {
        super.paint(g);
        Rectangle rect = getBounds();
        g.setColor(SystemColor.control);
        g.drawRect(0, 0, rect.width - 1, rect.height - 1);
    }

    /**
   * Set one or more attributes of the component.
   * @param attribName the attribute name
   * @param attribValue the attribute value
   * @return 0 for success, non zero for failure or to require some further action
   */
    public int setAttribute(String attribName, Object attribValue) {
        String attribNameLwr = attribName.toLowerCase();
        int numAttribute = attributeNames.size();
        for (int i = 0; i < numAttribute; i++) {
            if (((String) attributeNames.elementAt(i)).equals(attribNameLwr)) {
                attributes.setElementAt(attribValue, i);
                return 0;
            }
        }
        attributes.addElement(attribValue);
        attributeNames.addElement(attribNameLwr);
        return 0;
    }

    /**
   * Get the number of attributes this component wrapper exposes
   * @return The number of attributes this component exposes
   */
    public int getNumAttributes() {
        return attributes.size();
    }

    /**
   * Get an attribute value
   * @param idx the attribute index
   * @return the attribute value
   */
    public String getAttribute(int idx) {
        if (idx >= attributes.size()) return null;
        return (String) attributes.elementAt(idx);
    }

    /**
   * Get the name of an indexed attribute
   * @param idx the attribute index
   * @return the name
   */
    public String getAttributeName(int idx) {
        if (idx >= attributes.size()) return null;
        return (String) attributeNames.elementAt(idx);
    }
}
