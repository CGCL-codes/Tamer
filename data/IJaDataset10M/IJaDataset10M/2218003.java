package org.openorb.orb.test.iiop.fragmentedmessage;

/**
 * The default factory implementation for value type ObjectId.
 *
 * @author Michael Macaluso
 */
public class ObjectIdDefaultFactory implements org.omg.CORBA.portable.ValueFactory {

    /**
     * Read the value type from an input stream.
     *
     * @param is The input stream to read the value type from.
     * @return The valut type read from the stream.
     */
    public java.io.Serializable read_value(org.omg.CORBA_2_3.portable.InputStream is) {
        ObjectIdImpl ret = new ObjectIdImpl();
        is.read_value(ret);
        return ret;
    }
}
