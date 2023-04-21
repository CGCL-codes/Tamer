package org.omg.IOP;

import org.omg.CORBA.portable.Streamable;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/**
 * A holder for the structure {@link TaggedComponent}.
 *
* @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class TaggedComponentHolder implements Streamable {

    /**
   * The stored TaggedComponent value.
   */
    public TaggedComponent value;

    /**
   * Create the unitialised instance, leaving the value field
   * with default <code>null</code> value.
   */
    public TaggedComponentHolder() {
    }

    /**
   * Create the initialised instance.
   * @param initialValue the value that will be assigned to
   * the <code>value</code> field.
   */
    public TaggedComponentHolder(TaggedComponent initialValue) {
        value = initialValue;
    }

    /**
   * Fill in the {@link value} by data from the CDR stream.
   *
   * @param input the org.omg.CORBA.portable stream to read. 
   */
    public void _read(InputStream input) {
        value = TaggedComponentHelper.read(input);
    }

    /**
   * Write the stored value into the CDR stream.
   * 
   * @param output the org.omg.CORBA.portable stream to write. 
   */
    public void _write(OutputStream output) {
        TaggedComponentHelper.write(output, value);
    }

    /**
   * Get the typecode of the TaggedComponent.
   */
    public org.omg.CORBA.TypeCode _type() {
        return TaggedComponentHelper.type();
    }
}
