package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/**
* A holder for the object {@link Policy}.
*
* @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
*/
public class PolicyHolder implements Streamable {

    /**
   * The stored Policy value.
   */
    public Policy value;

    /**
   * Create the unitialised instance, leaving the value field
   * with default <code>null</code> value.
   */
    public PolicyHolder() {
    }

    /**
   * Create the initialised instance.
   * @param initialValue the value that will be assigned to
   * the <code>value</code> field.
   */
    public PolicyHolder(Policy initialValue) {
        value = initialValue;
    }

    /**
   * Fill in the {@link value} by data from the CDR stream.
   * Delegates work to {@link PolicyHelper}.
   *
   * @param input the org.omg.CORBA.portable stream to read.
   */
    public void _read(InputStream input) {
        value = PolicyHelper.read(input);
    }

    /**
   * Write the stored value into the CDR stream.
   * Delegates work to {@link PolicyHelper}.
   *
   * @param output the org.omg.CORBA.portable stream to write.
   */
    public void _write(OutputStream output) {
        PolicyHelper.write(output, value);
    }

    /**
   * Get the typecode of the Policy.
   * Delegates work to {@link PolicyHelper}.
   */
    public org.omg.CORBA.TypeCode _type() {
        return PolicyHelper.type();
    }
}
