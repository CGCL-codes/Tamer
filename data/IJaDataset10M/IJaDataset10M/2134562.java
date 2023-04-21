package org.omg.PersonIdService;

/** 
 * Helper class for : IdStateSeq
 *  
 * @author OpenORB Compiler
 */
public class IdStateSeqHelper {

    /** extract_X methods found for the current ORBs Any type. */
    private static java.lang.Object[] _extractMethods;

    static {
        try {
            Class clz = Thread.currentThread().getContextClassLoader().loadClass("org.openorb.orb.core.Any");
            java.lang.reflect.Method meth = clz.getMethod("extract_Streamable", null);
            _extractMethods = new java.lang.Object[] { clz, meth };
        } catch (Exception ex) {
        }
        if (_extractMethods == null) {
            _extractMethods = new java.lang.Object[0];
        }
    }

    private static java.lang.reflect.Method getExtract(Class clz) {
        int len = _extractMethods.length;
        for (int i = 0; i < len; i += 2) {
            if (clz.equals(_extractMethods[i])) {
                return (java.lang.reflect.Method) _extractMethods[i + 1];
            }
        }
        synchronized (org.omg.CORBA.Any.class) {
            for (int i = len; i < _extractMethods.length; i += 2) {
                if (clz.equals(_extractMethods[i])) {
                    return (java.lang.reflect.Method) _extractMethods[i + 1];
                }
            }
            java.lang.Object[] tmp = new java.lang.Object[_extractMethods.length + 2];
            System.arraycopy(_extractMethods, 0, tmp, 0, _extractMethods.length);
            tmp[_extractMethods.length] = clz;
            try {
                tmp[_extractMethods.length + 1] = clz.getMethod("extract_Streamable", null);
            } catch (Exception ex) {
            }
            _extractMethods = tmp;
            return (java.lang.reflect.Method) _extractMethods[_extractMethods.length - 1];
        }
    }

    /**
     * Insert IdStateSeq into an any
     * @param a an any
     * @param t IdStateSeq value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.PersonIdService.IdState[] t) {
        a.insert_Streamable(new org.omg.PersonIdService.IdStateSeqHolder(t));
    }

    /**
     * Extract IdStateSeq from an any
     *
     * @param a an any
     * @return the extracted IdStateSeq value
     */
    public static org.omg.PersonIdService.IdState[] extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        java.lang.reflect.Method meth = getExtract(a.getClass());
        if (meth != null) {
            try {
                org.omg.CORBA.portable.Streamable s = (org.omg.CORBA.portable.Streamable) meth.invoke(a, null);
                if (s instanceof org.omg.PersonIdService.IdStateSeqHolder) {
                    return ((org.omg.PersonIdService.IdStateSeqHolder) s).value;
                }
            } catch (final IllegalAccessException e) {
                throw new org.omg.CORBA.INTERNAL(e.toString());
            } catch (final IllegalArgumentException e) {
                throw new org.omg.CORBA.INTERNAL(e.toString());
            } catch (final java.lang.reflect.InvocationTargetException e) {
                final Throwable rex = e.getTargetException();
                if (rex instanceof org.omg.CORBA.BAD_INV_ORDER) {
                } else if (rex instanceof Error) {
                    throw (Error) rex;
                } else if (rex instanceof RuntimeException) {
                    throw (RuntimeException) rex;
                }
            }
            org.omg.PersonIdService.IdStateSeqHolder h = new org.omg.PersonIdService.IdStateSeqHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the IdStateSeq TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "IdStateSeq", orb.create_sequence_tc(0, org.omg.PersonIdService.IdStateHelper.type()));
        }
        return _tc;
    }

    /**
     * Return the IdStateSeq IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/PersonIdService/IdStateSeq:1.0";

    /**
     * Read IdStateSeq from a marshalled stream
     * @param istream the input stream
     * @return the readed IdStateSeq value
     */
    public static org.omg.PersonIdService.IdState[] read(org.omg.CORBA.portable.InputStream istream) {
        org.omg.PersonIdService.IdState[] new_one;
        {
            int size7 = istream.read_ulong();
            new_one = new org.omg.PersonIdService.IdState[size7];
            for (int i7 = 0; i7 < new_one.length; i7++) {
                new_one[i7] = org.omg.PersonIdService.IdStateHelper.read(istream);
            }
        }
        return new_one;
    }

    /**
     * Write IdStateSeq into a marshalled stream
     * @param ostream the output stream
     * @param value IdStateSeq value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.PersonIdService.IdState[] value) {
        ostream.write_ulong(value.length);
        for (int i7 = 0; i7 < value.length; i7++) {
            org.omg.PersonIdService.IdStateHelper.write(ostream, value[i7]);
        }
    }
}
