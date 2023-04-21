package org.omg.TerminologyServices;

/** 
 * Helper class for : Constraint
 *  
 * @author OpenORB Compiler
 */
public class ConstraintHelper {

    /**
     * Insert Constraint into an any
     * @param a an any
     * @param t Constraint value
     */
    public static void insert(org.omg.CORBA.Any a, String t) {
        a.type(type());
        write(a.create_output_stream(), t);
    }

    /**
     * Extract Constraint from an any
     *
     * @param a an any
     * @return the extracted Constraint value
     */
    public static String extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the Constraint TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "Constraint", orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string));
        }
        return _tc;
    }

    /**
     * Return the Constraint IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/TerminologyServices/Constraint:1.0";

    /**
     * Read Constraint from a marshalled stream
     * @param istream the input stream
     * @return the readed Constraint value
     */
    public static String read(org.omg.CORBA.portable.InputStream istream) {
        String new_one;
        new_one = istream.read_string();
        return new_one;
    }

    /**
     * Write Constraint into a marshalled stream
     * @param ostream the output stream
     * @param value Constraint value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, String value) {
        ostream.write_string(value);
    }
}
