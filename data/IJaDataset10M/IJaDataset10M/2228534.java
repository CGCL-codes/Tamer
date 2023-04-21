package org.dom4j.tree;

import java.util.Map;
import org.dom4j.Element;

/**
 * <p>
 * <code>DefaultProcessingInstruction</code> is the default Processing
 * Instruction implementation. It is a doubly linked node which supports the
 * parent relationship and can be modified in place.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.13 $
 */
public class DefaultProcessingInstruction extends org.dom4j.tree.FlyweightProcessingInstruction {

    /** The parent of this node */
    private Element parent;

    /**
     * <p>
     * This will create a new PI with the given target and values
     * </p>
     * 
     * @param target
     *            is the name of the PI
     * @param values
     *            is the <code>Map</code> values for the PI
     */
    public DefaultProcessingInstruction(String target, Map values) {
        super(target, values);
    }

    /**
     * <p>
     * This will create a new PI with the given target and values
     * </p>
     * 
     * @param target
     *            is the name of the PI
     * @param values
     *            is the values for the PI
     */
    public DefaultProcessingInstruction(String target, String values) {
        super(target, values);
    }

    /**
     * <p>
     * This will create a new PI with the given target and values
     * </p>
     * 
     * @param parent
     *            is the parent element
     * @param target
     *            is the name of the PI
     * @param values
     *            is the values for the PI
     */
    public DefaultProcessingInstruction(Element parent, String target, String values) {
        super(target, values);
        this.parent = parent;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setText(String text) {
        this.text = text;
        this.values = parseValues(text);
    }

    public void setValues(Map values) {
        this.values = values;
        this.text = toString(values);
    }

    public void setValue(String name, String value) {
        values.put(name, value);
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public boolean supportsParent() {
        return true;
    }

    public boolean isReadOnly() {
        return false;
    }
}
