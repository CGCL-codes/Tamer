package org.openorb.orb.test.iiop.value;

public class LeafDefaultFactory implements org.omg.CORBA.portable.ValueFactory {

    public java.io.Serializable read_value(org.omg.CORBA_2_3.portable.InputStream is) {
        return is.read_value(new LeafImpl());
    }
}
