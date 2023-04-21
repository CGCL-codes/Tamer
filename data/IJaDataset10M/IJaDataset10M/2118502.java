package org.openorb.orb.test.iiop.value;

/**
 * @author Chris Wood
 */
public class ValueBDefaultFactory implements ValueBValueFactory {

    /**
     * Initialize the value type member.
     *
     * @param l An initial value for the l member.
     * @return An instance of the ValueB value type.
     */
    public ValueB init(int l) {
        return new ValueBImpl(l);
    }

    /**
     * Read the value from an input stream.
     *
     * @param is The input stream.
     * @return The value read from the stream.
     */
    public java.io.Serializable read_value(org.omg.CORBA_2_3.portable.InputStream is) {
        ValueB val = new ValueBImpl();
        is.read_value(val);
        return val;
    }
}
