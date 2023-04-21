package org.charvolant.tmsnet.protocol;

import org.charvolant.tmsnet.protocol.TMSNetField;
import org.charvolant.tmsnet.protocol.TMSNetFieldType;
import org.charvolant.tmsnet.protocol.TMSNetType;
import org.charvolant.tmsnet.protocol.TMSNetElement;

/**
 * A test object for marshalling.
 * <p>
 * A message with a simple field.
 * 
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@TMSNetElement(type = TMSNetType.EVENT, id = 2002)
public class TestEvent2 {

    @TMSNetField(type = TMSNetFieldType.LONG)
    private int value;

    public TestEvent2(int value) {
        super();
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
