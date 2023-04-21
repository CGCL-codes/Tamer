package com.qasystems.qstudio.java.classloader;

import com.qasystems.qstudio.java.QStudioGlobal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * Disassembles class files in a javap-like fashion
 */
public class Printer {

    private static class Modifier {

        int mask;

        String modifier;

        Modifier(int maskValue, String modifierValue) {
            mask = maskValue;
            modifier = modifierValue;
        }
    }

    /** Home of cache files */
    private static final File CACHE = new File(new QStudioGlobal().getQsjavaHomePath(), "cache");

    /**
   * Definition of Java modifiers in canonical order
   */
    private static final Modifier[] MODIFIERS = new Modifier[] { new Modifier(java.lang.reflect.Modifier.PUBLIC, "public"), new Modifier(java.lang.reflect.Modifier.PROTECTED, "protected"), new Modifier(java.lang.reflect.Modifier.PRIVATE, "private"), new Modifier(java.lang.reflect.Modifier.STATIC, "static"), new Modifier(java.lang.reflect.Modifier.FINAL, "final"), new Modifier(java.lang.reflect.Modifier.STRICT, "strictfp"), new Modifier(java.lang.reflect.Modifier.SYNCHRONIZED, "synchronized"), new Modifier(java.lang.reflect.Modifier.VOLATILE, "volatile"), new Modifier(java.lang.reflect.Modifier.TRANSIENT, "transient"), new Modifier(java.lang.reflect.Modifier.NATIVE, "native"), new Modifier(java.lang.reflect.Modifier.ABSTRACT, "abstract"), new Modifier(java.lang.reflect.Modifier.INTERFACE, "interface") };

    /**
   * Constructs a Printer.
   *
   * @param errWriter writer for error output
   */
    public Printer() {
        super();
    }

    /**
   * Returns cache file of the given class.
   *
   * @param c - the class
   * @return cache file of the class
   */
    public File cacheFile(Class c) {
        return (new File(cacheDir(getPackage(c)), stripQual(c.getName())));
    }

    /**
   * Returns cache directory of the given package and creates it.
   *
   * @param p - the package
   * @return cache direcotry of the package
   */
    public File cacheDir(String name) {
        File result = CACHE;
        int dot = name.indexOf('.');
        while (dot > 0) {
            result = new File(result, name.substring(0, dot));
            name = name.substring(dot + 1);
            dot = name.indexOf('.');
        }
        result = new File(result, name);
        if (!result.exists()) {
            if (!result.mkdirs()) {
                Trier.println("Could not create cache directory " + result);
            }
        }
        return (result);
    }

    /**
   * Removes package from given name.
   *
   * @param s - the complete name
   * @return the last component of the name
   */
    private String stripQual(String s) {
        return (check(s.substring(s.lastIndexOf('.') + 1)));
    }

    /**
   * Print code
   */
    private void printPackage(String p, final PrintWriter pw) {
        if (p != null) {
            pw.print("package ");
            pw.print(p);
            pw.println(";");
        }
    }

    private void printModifiers(int m, final PrintWriter pw) {
        pw.print(decodeModifiers(m));
    }

    private String decodeModifiers(int m) {
        String modifiers = "";
        for (int i = 0; i < MODIFIERS.length; i++) {
            if ((m & MODIFIERS[i].mask) == MODIFIERS[i].mask) {
                modifiers += (MODIFIERS[i].modifier + " ");
            }
        }
        return (modifiers);
    }

    private void printClass(final Class c, final PrintWriter pw, String header) {
        if (!isPrivate(c.getModifiers())) {
            if (header != null) {
                pw.println(header);
            }
            new Trier() {

                public void tryIt(Object obj) throws IOException {
                    final PrintWriter p = (pw != null) ? pw : new PrintWriter(new BufferedWriter(new FileWriter(cacheFile((Class) obj))));
                    printNonPrivateClass((Class) obj, p);
                    if (pw == null) {
                        p.close();
                    }
                }
            }.doItSafe(c);
        }
    }

    private void printNonPrivateClass(final Class c, final PrintWriter pw) {
        if (c.getDeclaringClass() == null) {
            printPackage(getPackage(c), pw);
        }
        printModifiers(c.getModifiers(), pw);
        if (!c.isInterface()) {
            pw.print("class ");
        }
        pw.print(stripQual(decodeType(c)));
        printSuper(c.getSuperclass(), pw);
        printInterfaces(c.isInterface(), c.getInterfaces(), pw);
        pw.println(" {");
        new Trier() {

            public void tryIt(Object obj) {
                printFields(((Class) obj).getDeclaredFields(), pw);
            }
        }.doItSafe(c);
        new Trier() {

            public void tryIt(Object obj) {
                printConstructors(((Class) obj).getDeclaredConstructors(), pw);
            }
        }.doItSafe(c);
        new Trier() {

            public void tryIt(Object obj) {
                printMethods(((Class) obj).getDeclaredMethods(), pw);
            }
        }.doItSafe(c);
        new Trier() {

            public void tryIt(Object obj) {
                printClasses(((Class) obj).getDeclaredClasses(), pw, null);
            }
        }.doItSafe(c);
        pw.println("}");
    }

    /**
   * Gets package name of the class by stripping everything
   * after and including the last dot.
   */
    private String getPackage(Class c) {
        return (check(getPackage(c, c.getName())));
    }

    /**
   * Contains loop to strip of inner classes also.
   */
    private String getPackage(Class cl, String c) {
        final int lastdot = c.lastIndexOf('.');
        final String prefix = (lastdot < 0) ? null : c.substring(0, lastdot);
        return (((prefix == null) || (cl.getDeclaringClass() == null)) ? prefix : getPackage(cl, prefix));
    }

    private void printSuper(Class s, final PrintWriter pw) {
        if (s != null) {
            pw.print(" extends ");
            pw.print(decodeType(s));
        }
    }

    private void printInterfaces(boolean isInterface, Class[] ifs, final PrintWriter pw) {
        if (ifs.length > 0) {
            pw.print(isInterface ? " extends " : " implements ");
            pw.print(decodeType(ifs[0]));
            for (int i = 1; i < ifs.length; i++) {
                pw.print(",");
                pw.print(decodeType(ifs[i]));
            }
        }
    }

    private void printFields(Field[] dfs, final PrintWriter pw) {
        for (int i = 0; i < dfs.length; i++) {
            new Trier() {

                public void tryIt(Object obj) {
                    final Field f = (Field) obj;
                    final int mods = f.getModifiers();
                    if (!isPrivate(mods)) {
                        printModifiers(mods, pw);
                        pw.print(decodeType(f.getType()));
                        pw.print(" ");
                        pw.print(check(f.getName()));
                        pw.println(";");
                    }
                }
            }.doItSafe(dfs[i]);
        }
    }

    /**
   * Type -> Class decode
   */
    private String decodeType(Class cp) {
        String decodeResult = null;
        ResultTrier trier = new ResultTrier() {

            public void tryIt(Object obj) {
                Class c = (Class) obj;
                int depth = 0;
                while (c.isArray()) {
                    depth++;
                    c = c.getComponentType();
                }
                String inner = c.getName();
                final int len = inner.length();
                Class dc = c.getDeclaringClass();
                while (dc != null) {
                    final String outer = dc.getName();
                    final int i = outer.length() + 1;
                    dc = dc.getDeclaringClass();
                    if ((i < len) && !Character.isDigit(inner.charAt(i))) {
                        inner = outer + '.' + inner.substring(i);
                    } else if (dc == null) {
                        inner = outer;
                    } else {
                    }
                }
                setResult(inner + arrayDepth(depth));
            }
        };
        Throwable err = trier.tryItSafe(cp);
        if ((err != null) || !(trier.getResult() instanceof String)) {
            decodeResult = "Object";
        } else {
            decodeResult = (String) trier.getResult();
        }
        return (check(decodeResult));
    }

    private String arrayDepth(int m) {
        String s = "";
        for (int i = 0; i < m; i++) {
            s += "[]";
        }
        return (s);
    }

    /**
   * Print code (continued)
   */
    private void printConstructors(final Constructor[] dcs, final PrintWriter pw) {
        for (int i = 0; i < dcs.length; i++) {
            new Trier() {

                public void tryIt(Object obj) {
                    final Constructor cons = (Constructor) obj;
                    final int mods = cons.getModifiers();
                    if (!isPrivate(mods)) {
                        printModifiers(mods, pw);
                        pw.print(check(stripQual(decodeType(cons.getDeclaringClass()))));
                        pw.print("(");
                        new Trier() {

                            public void tryIt(Object c) {
                                printParameters(((Constructor) c).getParameterTypes(), pw);
                            }
                        }.doItSafe(cons);
                        pw.print(")");
                        new Trier() {

                            public void tryIt(Object c) {
                                printExceptions(((Constructor) c).getExceptionTypes(), pw);
                            }
                        }.doItSafe(cons);
                        pw.println("{}");
                    }
                }
            }.doItSafe(dcs[i]);
        }
    }

    private void printParameters(Class[] c, final PrintWriter pw) {
        if (c.length > 0) {
            pw.print(decodeType(c[0]));
            pw.print(" a0");
            for (int i = 1; i < c.length; i++) {
                pw.print(",");
                pw.print(decodeType(c[i]));
                pw.print(" a");
                pw.print(i);
            }
        }
    }

    private void printExceptions(Class[] c, final PrintWriter pw) {
        if (c.length > 0) {
            pw.print(" throws ");
            pw.print(decodeType(c[0]));
            for (int i = 1; i < c.length; i++) {
                pw.print(",");
                pw.print(decodeType(c[i]));
            }
        }
    }

    private void printMethods(Method[] dms, final PrintWriter pw) {
        for (int i = 0; i < dms.length; i++) {
            new Trier() {

                public void tryIt(Object obj) {
                    final Method meth = (Method) obj;
                    final int mods = meth.getModifiers();
                    if (!isPrivate(mods)) {
                        printModifiers(mods, pw);
                        pw.print(decodeType(meth.getReturnType()));
                        pw.print(" ");
                        pw.print(check(stripQual(meth.getName())));
                        pw.print("(");
                        new Trier() {

                            public void tryIt(Object obj) {
                                printParameters(((Method) obj).getParameterTypes(), pw);
                            }
                        }.doItSafe(meth);
                        pw.print(")");
                        new Trier() {

                            public void tryIt(Object obj) {
                                printExceptions(((Method) obj).getExceptionTypes(), pw);
                            }
                        }.doItSafe(meth);
                        pw.println(";");
                    }
                }
            }.doItSafe(dms[i]);
        }
    }

    /**
   * Only enable this one if we have problems with the tmp files in which
   * the method names and field names contain a '+' character instead of
   * a '$'. At this point it is only reported once so we can build a jar 
   * file with the right characters, however I see no need to integrate
   * this in the normal stream of QJ-Pro.
   * 
   * @param string - Field name or method name which may contains a '+' symbol
   * @return String without the '+' symbol replaced with a '$'
   */
    protected String check(String string) {
        return (string);
    }

    /**
   * DOCUMENT ME!
   *
   * @param cs DOCUMENT ME!
   * @param pw DOCUMENT ME!
   * @param header DOCUMENT ME!
   */
    public void printClasses(Vector cs, PrintWriter pw, String header) {
        printClasses((Class[]) cs.toArray(new Class[cs.size()]), pw, header);
    }

    private void printClasses(Class[] dcs, final PrintWriter pw, String header) {
        for (int i = 0; i < dcs.length; i++) {
            printClass(dcs[i], pw, header);
        }
    }

    private static boolean isPrivate(int mods) {
        final int PRIVATE = java.lang.reflect.Modifier.PRIVATE;
        return ((mods & PRIVATE) == PRIVATE);
    }
}
