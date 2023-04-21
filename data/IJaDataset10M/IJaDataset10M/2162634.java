package net.sourceforge.jtombstone.framework;

/**
 * 
 * @author <b>Mike Clark</b> (mike@clarkware.com)
 * @author Clarkware Consulting, Inc.
 * @author Bill Alexander
 */
public class AttributeInfo {

    private String name;

    private byte[] value;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return this.value;
    }
}
