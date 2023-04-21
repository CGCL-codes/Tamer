package org.omg.CORBA;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/**
* A holder for the exception {@link UnknownUserException}.

* @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
*/
public class UnknownUserExceptionHolder implements Streamable {

    /**
   * The stored UnknownUserException value.
   */
    public UnknownUserException value;

    /**
   * Create the unitialised instance, leaving the value field
   * with default <code>null</code> value.
   */
    public UnknownUserExceptionHolder() {
    }

    /**
   * Create the initialised instance.
   * @param initialValue the value that will be assigned to
   * the <code>value</code> field.
   */
    public UnknownUserExceptionHolder(UnknownUserException initialValue) {
        value = initialValue;
    }

    /**
   * Fill in the {@link value} by data from the CDR stream.
   *
   * @param input the org.omg.CORBA.portable stream to read.
   */
    public void _read(InputStream input) {
        value = UnknownUserExceptionHelper.read(input);
    }

    /**
   * Write the stored value into the CDR stream.
   *
   * @param output the org.omg.CORBA.portable stream to write.
   */
    public void _write(OutputStream output) {
        UnknownUserExceptionHelper.write(output, value);
    }

    /**
   * Get the typecode of the UnknownUserException.
   */
    public TypeCode _type() {
        return UnknownUserExceptionHelper.type();
    }
}
