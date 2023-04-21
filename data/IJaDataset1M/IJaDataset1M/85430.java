package org.openorb.orb.test.iiop.value;

public class ValueHDefaultFactory implements org.omg.CORBA.portable.ValueFactory {

    /**
     * Read the value from an InputStream.
     *
     * @param is The InputStream instance to read the value from.
     * @return The value read from the stream.
     */
    public java.io.Serializable read_value(org.omg.CORBA_2_3.portable.InputStream is) {
        return is.read_value(new ValueHImpl());
    }
}
