package org.omg.TerminologyServiceValues;

/** 
 * Helper class for : ConceptCode
 *  
 * @author OpenORB Compiler
 */
public class ConceptCodeHelper {

    /**
     * Insert ConceptCode into an any
     * @param a an any
     * @param t ConceptCode value
     */
    public static void insert(org.omg.CORBA.Any a, String t) {
        a.type(type());
        write(a.create_output_stream(), t);
    }

    /**
     * Extract ConceptCode from an any
     *
     * @param a an any
     * @return the extracted ConceptCode value
     */
    public static String extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the ConceptCode TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "ConceptCode", org.omg.TerminologyServices.ConceptCodeHelper.type());
        }
        return _tc;
    }

    /**
     * Return the ConceptCode IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/TerminologyServiceValues/ConceptCode:1.0";

    /**
     * Read ConceptCode from a marshalled stream
     * @param istream the input stream
     * @return the readed ConceptCode value
     */
    public static String read(org.omg.CORBA.portable.InputStream istream) {
        String new_one;
        new_one = istream.read_string();
        return new_one;
    }

    /**
     * Write ConceptCode into a marshalled stream
     * @param ostream the output stream
     * @param value ConceptCode value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, String value) {
        ostream.write_string(value);
    }
}
