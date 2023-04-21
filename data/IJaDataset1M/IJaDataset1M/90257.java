package org.omg.TerminologyServices;

/** 
 * Helper class for : Trinary
 *  
 * @author OpenORB Compiler
 */
public class TrinaryHelper {

    /**
     * Insert Trinary into an any
     * @param a an any
     * @param t Trinary value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.TerminologyServices.Trinary t) {
        a.type(type());
        write(a.create_output_stream(), t);
    }

    /**
     * Extract Trinary from an any
     *
     * @param a an any
     * @return the extracted Trinary value
     */
    public static org.omg.TerminologyServices.Trinary extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the Trinary TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            String[] _members = new String[3];
            _members[0] = "IS_FALSE";
            _members[1] = "IS_TRUE";
            _members[2] = "IS_UNKNOWN";
            _tc = orb.create_enum_tc(id(), "Trinary", _members);
        }
        return _tc;
    }

    /**
     * Return the Trinary IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/TerminologyServices/Trinary:1.0";

    /**
     * Read Trinary from a marshalled stream
     * @param istream the input stream
     * @return the readed Trinary value
     */
    public static org.omg.TerminologyServices.Trinary read(org.omg.CORBA.portable.InputStream istream) {
        return Trinary.from_int(istream.read_ulong());
    }

    /**
     * Write Trinary into a marshalled stream
     * @param ostream the output stream
     * @param value Trinary value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.TerminologyServices.Trinary value) {
        ostream.write_ulong(value.value());
    }
}
