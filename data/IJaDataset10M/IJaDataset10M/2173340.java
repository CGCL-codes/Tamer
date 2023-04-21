package org.equanda.persistence.xml;

import org.equanda.util.xml.tree.AttributeConstraints;
import org.equanda.util.xml.tree.NodeWithAttributeConstraints;
import org.equanda.util.xml.tree.XMLTreeException;
import java.util.Map;

/**
 * item-action node
 *
 * @author NetRom team
 */
public class OMMenuaction implements NodeWithAttributeConstraints {

    String window;

    private static final String[] requiredAttributes = { "window" };

    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

    public Map<String, AttributeConstraints> getAttributeConstraints() {
        return null;
    }

    public Map getDefaultValues() {
        return null;
    }

    public void setAttribute(CharSequence name, CharSequence value) throws XMLTreeException {
        if (name.equals("window")) {
            window = value.toString();
        }
    }

    public String toString() {
        return "<menu-action window=\"" + window + "\"/>";
    }
}
