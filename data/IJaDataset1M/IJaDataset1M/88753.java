package org.omg.CSI;

/** 
 * Helper class for : OID
 *  
 * @author OpenORB Compiler
 */
public class OIDHelper {

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
     * Insert OID into an any
     * @param a an any
     * @param t OID value
     */
    public static void insert(org.omg.CORBA.Any a, byte[] t) {
        a.insert_Streamable(new org.omg.CSI.OIDHolder(t));
    }

    /**
     * Extract OID from an any
     *
     * @param a an any
     * @return the extracted OID value
     */
    public static byte[] extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        if (HAS_OPENORB && a instanceof org.openorb.orb.core.Any) {
            org.openorb.orb.core.Any any = (org.openorb.orb.core.Any) a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if (s instanceof org.omg.CSI.OIDHolder) {
                    return ((org.omg.CSI.OIDHolder) s).value;
                }
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.omg.CSI.OIDHolder h = new org.omg.CSI.OIDHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the OID TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "OID", orb.create_sequence_tc(0, orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_octet)));
        }
        return _tc;
    }

    /**
     * Return the OID IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/CSI/OID:1.0";

    /**
     * Read OID from a marshalled stream
     * @param istream the input stream
     * @return the readed OID value
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
     * Write OID into a marshalled stream
     * @param ostream the output stream
     * @param value OID value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, byte[] value) {
        ostream.write_ulong(value.length);
        ostream.write_octet_array(value, 0, value.length);
    }
}
