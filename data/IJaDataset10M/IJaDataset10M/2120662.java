package gnu.classpath.jdwp.value;

import gnu.classpath.jdwp.JdwpConstants;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Wrapper for an long value.
 * 
 * @author Kyle Galloway <kgallowa@redhat.com>
 */
public final class LongValue extends Value {

    long _value;

    /**
   * Create a new LongValue from an long
   * 
   * @param value the long to wrap
   */
    public LongValue(long value) {
        super(JdwpConstants.Tag.LONG);
        _value = value;
    }

    /**
   * Get the value held in this Value
   * 
   * @return the value represented by this Value object
   */
    public long getValue() {
        return _value;
    }

    /**
   * Return an object representing this type
   * 
   * @return an Object represntation of this value
   */
    @Override
    protected Object getObject() {
        return new Long(_value);
    }

    /**
   * Write the wrapped long to the given DataOutputStream.
   * 
   * @param os the output stream to write to
   */
    @Override
    protected void write(DataOutputStream os) throws IOException {
        os.writeLong(_value);
    }
}
