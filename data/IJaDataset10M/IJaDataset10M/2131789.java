package org.jpox.samples.types.list;

/**
 * An item to store in an FK List container. Used for container inheritance.
 *
 * @version $Revision: 1.1 $  
 */
public class List2ItemChild extends List2Item {

    protected String code = null;

    protected List2ItemChild() {
        super();
    }

    public List2ItemChild(String name, double value, int status, String code) {
        super(name, value, status);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        return getClass().getName() + " " + getName() + " - value=" + getValue() + " [status=" + getStatus() + "] CODE=" + code;
    }
}
