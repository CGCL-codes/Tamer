package rtjdds.rtps.types;

/**
 * Holder class for : ProtocolId_t
 * 
 * @author OpenORB Compiler
 */
public final class ProtocolId_tHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal ProtocolId_t value
     */
    public rtjdds.rtps.types.ProtocolId_t value;

    /**
     * Default constructor
     */
    public ProtocolId_tHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ProtocolId_tHolder(rtjdds.rtps.types.ProtocolId_t initial) {
        value = initial;
    }

    /**
     * Read ProtocolId_t from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = ProtocolId_tHelper.read(istream);
    }

    /**
     * Write ProtocolId_t into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        ProtocolId_tHelper.write(ostream, value);
    }

    /**
     * Return the ProtocolId_t TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return ProtocolId_tHelper.type();
    }
}
