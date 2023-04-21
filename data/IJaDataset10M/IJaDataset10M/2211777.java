package org.omg.DsObservationValue;

/** 
 * Helper class for : NoInformation
 *  
 * @author OpenORB Compiler
 */
public class NoInformationHelper {

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
     * Insert NoInformation into an any
     * @param a an any
     * @param t NoInformation value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.DsObservationValue.NoInformation t) {
        a.insert_Streamable(new org.omg.DsObservationValue.NoInformationHolder(t));
    }

    /**
     * Extract NoInformation from an any
     *
     * @param a an any
     * @return the extracted NoInformation value
     */
    public static org.omg.DsObservationValue.NoInformation extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        java.lang.reflect.Method meth = getExtract(a.getClass());
        if (meth != null) {
            try {
                org.omg.CORBA.portable.Streamable s = (org.omg.CORBA.portable.Streamable) meth.invoke(a, null);
                if (s instanceof org.omg.DsObservationValue.NoInformationHolder) return ((org.omg.DsObservationValue.NoInformationHolder) s).value;
            } catch (final IllegalAccessException e) {
                throw new org.omg.CORBA.INTERNAL(e.toString());
            } catch (final IllegalArgumentException e) {
                throw new org.omg.CORBA.INTERNAL(e.toString());
            } catch (final java.lang.reflect.InvocationTargetException e) {
                Throwable rex = e.getTargetException();
                if (rex instanceof org.omg.CORBA.BAD_INV_ORDER) {
                } else if (rex instanceof Error) {
                    throw (Error) rex;
                } else if (rex instanceof RuntimeException) {
                    throw (RuntimeException) rex;
                } else {
                }
            }
            org.omg.DsObservationValue.NoInformationHolder h = new org.omg.DsObservationValue.NoInformationHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    private static boolean _working = false;

    /**
     * Return the NoInformation TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (_tc != null) return _tc;
                if (_working) return org.omg.CORBA.ORB.init().create_recursive_tc(id());
                _working = true;
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[2];
                _members[0] = new org.omg.CORBA.StructMember();
                _members[0].name = "reason";
                _members[0].type = org.omg.DsObservationValue.QualifiedCodeStrHelper.type();
                _members[1] = new org.omg.CORBA.StructMember();
                _members[1].name = "text_description";
                _members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _tc = orb.create_struct_tc(id(), "NoInformation", _members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the NoInformation IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/DsObservationValue/NoInformation:1.0";

    /**
     * Read NoInformation from a marshalled stream
     * @param istream the input stream
     * @return the readed NoInformation value
     */
    public static org.omg.DsObservationValue.NoInformation read(org.omg.CORBA.portable.InputStream istream) {
        org.omg.DsObservationValue.NoInformation new_one = new org.omg.DsObservationValue.NoInformation();
        new_one.reason = org.omg.DsObservationValue.QualifiedCodeStrHelper.read(istream);
        new_one.text_description = istream.read_string();
        return new_one;
    }

    /**
     * Write NoInformation into a marshalled stream
     * @param ostream the output stream
     * @param value NoInformation value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.DsObservationValue.NoInformation value) {
        org.omg.DsObservationValue.QualifiedCodeStrHelper.write(ostream, value.reason);
        ostream.write_string(value.text_description);
    }
}
