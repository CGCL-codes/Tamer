package org.retro.scheme;

import java.io.PrintWriter;

public class JS {

    public JS() {
        ;
    }

    public static Object getGlobalValue(String s) {
        return Symbol.intern(s).getGlobalValue();
    }

    public static void setGlobalValue(String s, Object v) {
        Symbol.intern(s).setGlobalValue(v);
    }

    /** Returns the global procedure named s. **/
    public static SchemeProcedure getGlobalSchemeProcedure(String s) {
        return U.toProc(getGlobalValue(s));
    }

    /** Load Scheme expressions from a  Reader, or String. **/
    public static Object load(java.io.Reader in) {
        return Scheme.load(new InputPort(in));
    }

    public static Object load(String in) {
        return load(new java.io.StringReader(in));
    }

    public static void evalOrLoad(String it) {
        if (it.startsWith("(")) load(new java.io.StringReader(it)); else if (!it.startsWith("-")) Scheme.load(it);
    }

    /** Call a procedure with 0 to 20 arguments **/
    public static Object call(SchemeProcedure p) {
        return p.apply(list());
    }

    public static Object call(SchemeProcedure p, Object a1) {
        return p.apply(list(a1));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2) {
        return p.apply(list(a1, a2));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3) {
        return p.apply(list(a1, a2, a3));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4) {
        return p.apply(list(a1, a2, a3, a4));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5) {
        return p.apply(list(a1, a2, a3, a4, a5));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6) {
        return p.apply(list(a1, a2, a3, a4, a5, a6));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18, Object a19) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19));
    }

    public static Object call(SchemeProcedure p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18, Object a19, Object a20) {
        return p.apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20));
    }

    /** Apply a procedure to a list of arguments. **/
    public static Object apply(SchemeProcedure p, SchemePair as) {
        return p.apply(as);
    }

    /** Call a procedure named p with from 0 to 20 arguments. **/
    public static Object call(String p) {
        return getGlobalSchemeProcedure(p).apply(list());
    }

    public static Object call(String p, Object a1) {
        return getGlobalSchemeProcedure(p).apply(list(a1));
    }

    public static Object call(String p, Object a1, Object a2) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2));
    }

    public static Object call(String p, Object a1, Object a2, Object a3) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18, Object a19) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19));
    }

    public static Object call(String p, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18, Object a19, Object a20) {
        return getGlobalSchemeProcedure(p).apply(list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20));
    }

    public static Object apply(String p, SchemePair as) {
        return getGlobalSchemeProcedure(p).apply(as);
    }

    public static Object eval(String s) {
        Object it = read(s);
        return it == InputPort.EOF ? it : eval(it);
    }

    public static Object eval(Object it) {
        return Scheme.evalToplevel(it, Scheme.INTERACTION_ENVIRONMENT);
    }

    public static Object read(String s) {
        return (new InputPort(new java.io.StringReader(s))).read();
    }

    public static SchemePair list() {
        return Pair.EMPTY;
    }

    public static SchemePair list(Object a0) {
        return new Pair(a0, list());
    }

    public static SchemePair list(Object a0, Object a1) {
        return new Pair(a0, list(a1));
    }

    public static SchemePair list(Object a0, Object a1, Object a2) {
        return new Pair(a0, list(a1, a2));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3) {
        return new Pair(a0, list(a1, a2, a3));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4) {
        return new Pair(a0, list(a1, a2, a3, a4));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5) {
        return new Pair(a0, list(a1, a2, a3, a4, a5));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18, Object a19) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19));
    }

    public static SchemePair list(Object a0, Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10, Object a11, Object a12, Object a13, Object a14, Object a15, Object a16, Object a17, Object a18, Object a19, Object a20) {
        return new Pair(a0, list(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20));
    }

    public static void write(Object x, PrintWriter port, boolean quoted) {
        U.write(x, port, quoted);
    }

    public static void write(Object x, PrintWriter port) {
        U.write(x, port, true);
    }

    public static void display(Object x, PrintWriter port) {
        U.write(x, port, false);
    }

    /** Convert from an Object to a primitive type. **/
    public static boolean booleanValue(Object o) {
        return o != Boolean.FALSE;
    }

    public static byte byteValue(Object o) {
        return ((Number) o).byteValue();
    }

    public static char charValue(Object o) {
        return (o instanceof Number) ? ((char) ((Number) o).shortValue()) : ((Character) o).charValue();
    }

    public static short shortValue(Object o) {
        return ((Number) o).shortValue();
    }

    public static int intValue(Object o) {
        return ((Number) o).intValue();
    }

    public static long longValue(Object o) {
        return ((Number) o).longValue();
    }

    public static float floatValue(Object o) {
        return ((Number) o).floatValue();
    }

    public static double doubleValue(Object o) {
        return ((Number) o).doubleValue();
    }

    /** Convert from primitive type to Object. **/
    public static Boolean toObject(boolean x) {
        return x ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Object toObject(byte x) {
        return new Byte(x);
    }

    public static Object toObject(char x) {
        return new Character(x);
    }

    public static Object toObject(short x) {
        return new Short(x);
    }

    public static Object toObject(int x) {
        return U.toNum(x);
    }

    public static Object toObject(long x) {
        return new Long(x);
    }

    public static Object toObject(float x) {
        return new Float(x);
    }

    public static Object toObject(double x) {
        return new Double(x);
    }

    public static Object toObject(Object x) {
        return x;
    }
}
