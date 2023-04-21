package org.omg.CORBA;

import gnu.CORBA.typecodes.ArrayTypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/**
 * A sequence holder for CORBA <code>LongSeq</code> that is mapped into
 * java <code>int[]</code>. 
 * 
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org)
 */
public final class LongSeqHolder implements Streamable {

    /**
   * The type code for this holder. Each holder has a different instance.
   */
    private final ArrayTypeCode typecode = new ArrayTypeCode(TCKind.tk_long);

    /**
   * The <code>int[]</code> (CORBA <code>LongSeq</code>) value,
   * held by this LongSeqHolder.
   */
    public int[] value;

    /**
   * Constructs an instance of LongSeqHolder,
   * initializing {@link #value} to <code>null</code>.
   */
    public LongSeqHolder() {
    }

    /**
   * Constructs an instance of LongSeqHolder,
   * initializing {@link #value} to the given <code>int</code>.
   * 
   * @param initial_value a value that will be assigned to the
   * {@link #value} field.
   */
    public LongSeqHolder(int[] initial_value) {
        value = initial_value;
        typecode.setLength(value.length);
    }

    /**
   * Fill in the {@link value } field by reading the required data
   * from the given stream. This method first reads the array size
   * (as CORBA <code>long</code>and then calls the
   * {@link org.omg.CORBA.portable.InputStream#input.read_long_array }.
   * 
   * @param input the input stream to read from.
   */
    public void _read(InputStream input) {
        value = new int[input.read_long()];
        input.read_long_array(value, 0, value.length);
        typecode.setLength(value.length);
    }

    /**
   * Write the {@link value } field to the given stream.
   * This method first writes the array size
   * (as CORBA <code>long</code>and then calls the
   * {@link org.omg.CORBA.portable.OutputStream#input.write_long_array }.    
   * 
   * @param output the output stream to write into.
   */
    public void _write(OutputStream output) {
        output.write_long(value.length);
        output.write_long_array(value, 0, value.length);
    }

    /**
   * Returns the TypeCode, corresponding the CORBA type that is stored
   * using this holder.
   */
    public TypeCode _type() {
        return typecode;
    }
}
