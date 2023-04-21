package backend.parser.xgmml;

import backend.core.AttributeName;
import backend.core.security.Session;

public class TempGDS {

    private static final long serialVersionUID = 1L;

    private Session s;

    private AttributeName att;

    private String value;

    private boolean doIndex = false;

    public TempGDS(Session s, AttributeName att, String value) {
        this.s = s;
        this.att = att;
        this.value = value;
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == this) return true;
        if (arg0 instanceof TempGDS) {
            TempGDS temp = (TempGDS) arg0;
            return temp.att.equals(this.att) && temp.value.equals(value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return att.hashCode() + value.hashCode();
    }

    @Override
    public String toString() {
        return att.getId(s) + " " + value;
    }

    public AttributeName getAtt() {
        return att;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getDoIndex() {
        return doIndex;
    }

    public void setDoIndex(Boolean doIndex) {
        this.doIndex = doIndex;
    }
}
