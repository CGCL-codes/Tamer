package rtjdds.rtps.types;

/**
 * Holder class for : SequenceNumber_t
 * 
 * @author OpenORB Compiler
 */
public final class SequenceNumber_tHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal SequenceNumber_t value
     */
    public rtjdds.rtps.types.SequenceNumber_t value;

    /**
     * Default constructor
     */
    public SequenceNumber_tHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public SequenceNumber_tHolder(rtjdds.rtps.types.SequenceNumber_t initial) {
        value = initial;
    }

    /**
     * Read SequenceNumber_t from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = SequenceNumber_tHelper.read(istream);
    }

    /**
     * Write SequenceNumber_t into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        SequenceNumber_tHelper.write(ostream, value);
    }

    /**
     * Return the SequenceNumber_t TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return SequenceNumber_tHelper.type();
    }
}
