package org.dbe.studio.core.smcore.core.eb.ent;

/**
 * @author gdorigo
 *
 */
public class SCSData {

    private String value = new String();

    /**
     * @return ##
     */
    public final String getValue() {
        return value;
    }

    /**
     * @param inValue ##
     */
    public final void setValue(final String inValue) {
        this.value = inValue;
    }

    /**
     * Constructor
     */
    public SCSData() {
        super();
        this.value = "";
    }
}
