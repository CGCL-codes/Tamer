package org.omg.CSI;

/** 
 * Helper class for : X501DistinguishedName
 *  
 * @author OpenORB Compiler
 */
public class X501DistinguishedNameHelper {

    private static final boolean HAS_OPENORB;

    static {
        boolean hasOpenORB = false;
        try {
            Thread.currentThread().getContextClassLoader().loadClass("org.openorb.orb.core.Any");
            hasOpenORB = true;
        } catch (ClassNotFoundException ex) {
        }
        HAS_OPENORB = hasOpenORB;
    }

    /**
     * Insert X501DistinguishedName into an any
     * @param a an any
     * @param t X501DistinguishedName value
     */
    public static void insert(org.omg.CORBA.Any a, byte[] t) {
        a.insert_Streamable(new org.omg.CSI.X501DistinguishedNameHolder(t));
    }

    /**
     * Extract X501DistinguishedName from an any
     *
     * @param a an any
     * @return the extracted X501DistinguishedName value
     */
    public static byte[] extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        if (HAS_OPENORB && a instanceof org.openorb.orb.core.Any) {
            org.openorb.orb.core.Any any = (org.openorb.orb.core.Any) a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if (s instanceof org.omg.CSI.X501DistinguishedNameHolder) {
                    return ((org.omg.CSI.X501DistinguishedNameHolder) s).value;
                }
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.omg.CSI.X501DistinguishedNameHolder h = new org.omg.CSI.X501DistinguishedNameHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the X501DistinguishedName TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "X501DistinguishedName", orb.create_sequence_tc(0, orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_octet)));
        }
        return _tc;
    }

    /**
     * Return the X501DistinguishedName IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/CSI/X501DistinguishedName:1.0";

    /**
     * Read X501DistinguishedName from a marshalled stream
     * @param istream the input stream
     * @return the readed X501DistinguishedName value
     */
    public static byte[] read(org.omg.CORBA.portable.InputStream istream) {
        byte[] new_one;
        {
            int size7 = istream.read_ulong();
            new_one = new byte[size7];
            istream.read_octet_array(new_one, 0, new_one.length);
        }
        return new_one;
    }

    /**
     * Write X501DistinguishedName into a marshalled stream
     * @param ostream the output stream
     * @param value X501DistinguishedName value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, byte[] value) {
        ostream.write_ulong(value.length);
        ostream.write_octet_array(value, 0, value.length);
    }
}
