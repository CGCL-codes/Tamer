package org.omg.PortableInterceptor;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/**
 * A holder for the object {@link ObjectReferenceFactory}.
 *
 * @since 1.5
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class ObjectReferenceFactoryHolder implements Streamable {

    /**
   * The stored ObjectReferenceFactory value.
   */
    public ObjectReferenceFactory value;

    /**
   * Create the unitialised instance, leaving the value field
   * with default <code>null</code> value.
   */
    public ObjectReferenceFactoryHolder() {
    }

    /**
   * Create the initialised instance.
   * @param initialValue the value that will be assigned to
   * the <code>value</code> field.
   */
    public ObjectReferenceFactoryHolder(ObjectReferenceFactory initialValue) {
        value = initialValue;
    }

    /**
   * Fill in the {@link value} by data from the CDR stream.
   *
   * @param input the org.omg.CORBA.portable stream to read.
   */
    public void _read(InputStream input) {
        value = ObjectReferenceFactoryHelper.read(input);
    }

    /**
   * Write the stored value into the CDR stream.
   *
   * @param output the org.omg.CORBA.portable stream to write.
   */
    public void _write(OutputStream output) {
        ObjectReferenceFactoryHelper.write(output, value);
    }

    /**
   * Get the typecode of the ObjectReferenceFactory.
   */
    public org.omg.CORBA.TypeCode _type() {
        return ObjectReferenceFactoryHelper.type();
    }
}
