package org.omg.DsObservationAccess;

/** 
 * Helper class for : ObservationRemoteSeq
 *  
 * @author OpenORB Compiler
 */
public class ObservationRemoteSeqHelper {

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
     * Insert ObservationRemoteSeq into an any
     * @param a an any
     * @param t ObservationRemoteSeq value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.DsObservationAccess.ObservationRemote[] t) {
        a.insert_Streamable(new org.omg.DsObservationAccess.ObservationRemoteSeqHolder(t));
    }

    /**
     * Extract ObservationRemoteSeq from an any
     *
     * @param a an any
     * @return the extracted ObservationRemoteSeq value
     */
    public static org.omg.DsObservationAccess.ObservationRemote[] extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        java.lang.reflect.Method meth = getExtract(a.getClass());
        if (meth != null) {
            try {
                org.omg.CORBA.portable.Streamable s = (org.omg.CORBA.portable.Streamable) meth.invoke(a, null);
                if (s instanceof org.omg.DsObservationAccess.ObservationRemoteSeqHolder) {
                    return ((org.omg.DsObservationAccess.ObservationRemoteSeqHolder) s).value;
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
            org.omg.DsObservationAccess.ObservationRemoteSeqHolder h = new org.omg.DsObservationAccess.ObservationRemoteSeqHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the ObservationRemoteSeq TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "ObservationRemoteSeq", orb.create_sequence_tc(0, org.omg.DsObservationAccess.ObservationRemoteHelper.type()));
        }
        return _tc;
    }

    /**
     * Return the ObservationRemoteSeq IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/DsObservationAccess/ObservationRemoteSeq:1.0";

    /**
     * Read ObservationRemoteSeq from a marshalled stream
     * @param istream the input stream
     * @return the readed ObservationRemoteSeq value
     */
    public static org.omg.DsObservationAccess.ObservationRemote[] read(org.omg.CORBA.portable.InputStream istream) {
        org.omg.DsObservationAccess.ObservationRemote[] new_one;
        {
            int size7 = istream.read_ulong();
            new_one = new org.omg.DsObservationAccess.ObservationRemote[size7];
            for (int i7 = 0; i7 < new_one.length; i7++) {
                new_one[i7] = org.omg.DsObservationAccess.ObservationRemoteHelper.read(istream);
            }
        }
        return new_one;
    }

    /**
     * Write ObservationRemoteSeq into a marshalled stream
     * @param ostream the output stream
     * @param value ObservationRemoteSeq value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.DsObservationAccess.ObservationRemote[] value) {
        ostream.write_ulong(value.length);
        for (int i7 = 0; i7 < value.length; i7++) {
            org.omg.DsObservationAccess.ObservationRemoteHelper.write(ostream, value[i7]);
        }
    }
}
