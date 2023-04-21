package org.mc4j.ems.connection.bean.attribute;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Apr 6, 2005
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class AttributeChangeEvent {

    private EmsAttribute attribute;

    private Object oldValue;

    private Object newValue;

    private long timeOfChange;

    public AttributeChangeEvent() {
    }

    public AttributeChangeEvent(EmsAttribute attribute, Object oldValue, Object newValue) {
        this.attribute = attribute;
        this.oldValue = oldValue;
        this.newValue = newValue;
        timeOfChange = System.currentTimeMillis();
    }
}
