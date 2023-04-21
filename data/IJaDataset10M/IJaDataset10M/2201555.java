package org.omg.PersonIdService;

/** 
 * Helper class for : IdsExist
 *  
 * @author OpenORB Compiler
 */
public class IdsExistHelper {

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
     * Insert IdsExist into an any
     * @param a an any
     * @param t IdsExist value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.PersonIdService.IdsExist t) {
        a.insert_Streamable(new org.omg.PersonIdService.IdsExistHolder(t));
    }

    /**
     * Extract IdsExist from an any
     *
     * @param a an any
     * @return the extracted IdsExist value
     */
    public static org.omg.PersonIdService.IdsExist extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        java.lang.reflect.Method meth = getExtract(a.getClass());
        if (meth != null) {
            try {
                org.omg.CORBA.portable.Streamable s = (org.omg.CORBA.portable.Streamable) meth.invoke(a, null);
                if (s instanceof org.omg.PersonIdService.IdsExistHolder) return ((org.omg.PersonIdService.IdsExistHolder) s).value;
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
            org.omg.PersonIdService.IdsExistHolder h = new org.omg.PersonIdService.IdsExistHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    private static boolean _working = false;

    /**
     * Return the IdsExist TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (_tc != null) return _tc;
                if (_working) return org.omg.CORBA.ORB.init().create_recursive_tc(id());
                _working = true;
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[1];
                _members[0] = new org.omg.CORBA.StructMember();
                _members[0].name = "indices";
                _members[0].type = org.omg.PersonIdService.IndexSeqHelper.type();
                _tc = orb.create_exception_tc(id(), "IdsExist", _members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the IdsExist IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/PersonIdService/IdsExist:1.0";

    /**
     * Read IdsExist from a marshalled stream
     * @param istream the input stream
     * @return the readed IdsExist value
     */
    public static org.omg.PersonIdService.IdsExist read(org.omg.CORBA.portable.InputStream istream) {
        org.omg.PersonIdService.IdsExist new_one = new org.omg.PersonIdService.IdsExist();
        if (!istream.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL();
        new_one.indices = org.omg.PersonIdService.IndexSeqHelper.read(istream);
        return new_one;
    }

    /**
     * Write IdsExist into a marshalled stream
     * @param ostream the output stream
     * @param value IdsExist value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.PersonIdService.IdsExist value) {
        ostream.write_string(id());
        org.omg.PersonIdService.IndexSeqHelper.write(ostream, value.indices);
    }
}
