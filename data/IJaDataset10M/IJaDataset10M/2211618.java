package org.mc4j.ems.store;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Apr 12, 2005
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class Value {

    private Object value;

    private long recorded;

    public Value(Object value, long recorded) {
        this.value = value;
        this.recorded = recorded;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getRecorded() {
        return recorded;
    }

    public void setRecorded(long recorded) {
        this.recorded = recorded;
    }
}
