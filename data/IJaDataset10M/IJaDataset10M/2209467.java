package org.charvolant.tmsnet.protocol;

import org.charvolant.tmsnet.protocol.TMSNetField;
import org.charvolant.tmsnet.protocol.TMSNetType;
import org.charvolant.tmsnet.protocol.TMSNetElement;

/**
 * A test object for marshalling.
 * <p>
 * A record with two fields
 * 
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@TMSNetElement(type = TMSNetType.RECORD, id = 4002)
public class TestRecord3 {

    @TMSNetField
    private long value;

    public TestRecord3(long value) {
        super();
        this.value = value;
    }

    public TestRecord3() {
        super();
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
