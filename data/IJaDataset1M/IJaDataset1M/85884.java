package org.openthinclient.common.model.schema;

/**
 * @author Joerg Henne
 */
public class EntryNode extends Node {

    private static final long serialVersionUID = 1L;

    protected String defaultValue;

    /**
   * @param name
   */
    public EntryNode(String name, String value) {
        super(name);
        this.defaultValue = value;
    }

    public String getValue() {
        return defaultValue;
    }

    public void setValue(String value) {
        this.defaultValue = value;
    }

    @Override
    public long getUID() {
        return super.getUID() ^ (null != defaultValue ? defaultValue.hashCode() : 123424234);
    }
}
