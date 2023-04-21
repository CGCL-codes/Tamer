package javax.naming.ldap;

import javax.naming.ldap.Control;

/**
 * TODO: JavaDoc
 */
public class BasicControl implements Control {

    private static final long serialVersionUID = -4233907508771791687L;

    protected String id;

    protected boolean criticality = false;

    protected byte[] value;

    public BasicControl(String id) {
        this.id = id;
    }

    public BasicControl(String id, boolean criticality, byte[] value) {
        this.id = id;
        this.criticality = criticality;
        this.value = value;
    }

    public byte[] getEncodedValue() {
        return value;
    }

    public boolean isCritical() {
        return criticality;
    }

    public String getID() {
        return id;
    }
}
