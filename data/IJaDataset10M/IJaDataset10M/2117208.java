package org.omg.CSI;

/** 
 * Helper class for : GSS_NT_ExportedName
 *  
 * @author OpenORB Compiler
 */
public class GSS_NT_ExportedNameHelper {

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
     * Insert GSS_NT_ExportedName into an any
     * @param a an any
     * @param t GSS_NT_ExportedName value
     */
    public static void insert(org.omg.CORBA.Any a, byte[] t) {
        a.insert_Streamable(new org.omg.CSI.GSS_NT_ExportedNameHolder(t));
    }

    /**
     * Extract GSS_NT_ExportedName from an any
     *
     * @param a an any
     * @return the extracted GSS_NT_ExportedName value
     */
    public static byte[] extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        if (HAS_OPENORB && a instanceof org.openorb.orb.core.Any) {
            org.openorb.orb.core.Any any = (org.openorb.orb.core.Any) a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if (s instanceof org.omg.CSI.GSS_NT_ExportedNameHolder) {
                    return ((org.omg.CSI.GSS_NT_ExportedNameHolder) s).value;
                }
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.omg.CSI.GSS_NT_ExportedNameHolder h = new org.omg.CSI.GSS_NT_ExportedNameHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the GSS_NT_ExportedName TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "GSS_NT_ExportedName", orb.create_sequence_tc(0, orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_octet)));
        }
        return _tc;
    }

    /**
     * Return the GSS_NT_ExportedName IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/CSI/GSS_NT_ExportedName:1.0";

    /**
     * Read GSS_NT_ExportedName from a marshalled stream
     * @param istream the input stream
     * @return the readed GSS_NT_ExportedName value
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
     * Write GSS_NT_ExportedName into a marshalled stream
     * @param ostream the output stream
     * @param value GSS_NT_ExportedName value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, byte[] value) {
        ostream.write_ulong(value.length);
        ostream.write_octet_array(value, 0, value.length);
    }
}
