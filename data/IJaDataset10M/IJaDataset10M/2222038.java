package org.charvolant.tmsnet.protocol;

/**
 * A test object for marshalling.
 * <p>
 * A blob of data
 * 
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@TMSNetElement(type = TMSNetType.COMMAND, id = 3011)
public class TestCommand11 {

    @TMSNetField
    private byte[] value;

    public TestCommand11() {
        super();
        this.value = null;
    }

    public TestCommand11(byte[] value) {
        this();
        this.value = value;
    }

    public byte[] getValue() {
        return this.value;
    }
}
