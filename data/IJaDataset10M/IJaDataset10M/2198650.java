package engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import engine.error.CompileVExcexption;

public class V2M {

    /**
	 * Global object representing null. The normal null cannot be used as it
	 * cannot be added to ArrayDeques.
	 */
    public static final Object v2mNull = new Object() {

        @Override
        public String toString() {
            return "null";
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    };

    public static final String version = "0.1";

    public static String readable(final Object object) {
        final String str = object.toString();
        if (str.startsWith("class")) return "(" + str + ")";
        if (object instanceof Map<?, ?>) return "[[ " + str.substring(1, str.length() - 1).replace("=", " => ").replace(",", " ;") + " ]]";
        if (object instanceof List<?>) return "[ " + str.substring(1, str.length() - 1).replace(",", " ;") + " ]";
        return str;
    }

    /**
	 * Typus (Klasse) eines Datenobjekts aus Bezeichnung ermitteln.
	 * 
	 * @param name
	 *            Bezeichnung des Datenobjekts.
	 * @return Klasse des Objekts.
	 */
    private static Class<?> getType(String name) {
        Class<?> cl = null;
        int dim = 0;
        while (name.endsWith("[]")) {
            dim++;
            name = name.substring(0, name.lastIndexOf("[]"));
        }
        if (name.equals("boolean")) return boolean.class;
        if (name.equals("byte")) return byte.class;
        if (name.equals("short")) return short.class;
        if (name.equals("int")) return int.class;
        if (name.equals("long")) return long.class;
        if (name.equals("float")) return float.class;
        if (name.equals("double")) return double.class;
        if (name.equals("char")) return char.class;
        try {
            cl = Class.forName(name);
        } catch (final ClassNotFoundException cnf) {
            if (name.indexOf('.') < 0) name = "java.lang.".concat(name);
        }
        try {
            cl = Class.forName(name);
        } catch (final ClassNotFoundException cnf) {
            System.out.println("Klasse [" + name + "] nicht gefunden");
        }
        if (cl != null && dim > 0) cl = Array.newInstance(cl, new int[dim]).getClass();
        return cl;
    }

    /**
	 * Array mit Klassen { wichtig bei argument list} erstellen.
	 * 
	 * @param args
	 *            String mit Klassenbezeichnungen (Types) durch Leerzeichen
	 *            getrennt.
	 * @return Array mit Klassen.
	 */
    public static Class<?>[] argClasses(final String args) {
        if (args.length() < 1) return null;
        final java.util.StringTokenizer st = new java.util.StringTokenizer(args);
        final Class<?>[] classes = new Class[st.countTokens()];
        int idx = 0;
        while (st.hasMoreTokens()) classes[idx++] = getType(st.nextToken());
        return classes;
    }

    public static Executable getDotExec(final Object o, final Scanner s) throws CompileVExcexption {
        if (!s.hasNext()) return (Executable) o;
        return getDotExec(o, s.next(), s);
    }

    public static Executable getDotExec(final Object o, final String name, final Scanner s) throws CompileVExcexption {
        if (o instanceof VDot) {
            final Executable x = ((VDot) o).getField(s, name);
            if (x != null) return x;
        }
        Class<?> c;
        if (o instanceof Class<?>) c = (Class<?>) o; else c = o.getClass();
        for (final Method m : c.getMethods()) {
            if (!m.getName().equals(name)) continue;
            final MethodInvocation meth = new MethodInvocation(o, m);
            return meth;
        }
        for (final Field f : c.getFields()) try {
            if (f.getName().equals(name)) {
                if (s.hasNext()) return getDotExec(f.get(o), s.next(), s);
                return new Symbol(f.get(o));
            }
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            throw new CompileVExcexption("Operation not permitted");
        }
        return null;
    }

    public static void setDotExec(final Object o, final Scanner s, final Object value) throws CompileVExcexption {
        if (!s.hasNext()) throw new CompileVExcexption("Cannot change this.");
        final String name = s.next();
        if (o instanceof VDot) if (((VDot) o).setField(s, name, value)) return;
        for (final Field f : o.getClass().getFields()) try {
            if (f.getName().equals(name)) f.set(o, value);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            throw new CompileVExcexption("Operation not permitted");
        }
        throw new CompileVExcexption("field not found");
    }

    private static final URLClassLoader urlLoader = (URLClassLoader) V2M.class.getClassLoader();

    public static InputStream loadURL(final String resource) throws IOException {
        final URL url = urlLoader.findResource(resource);
        if (url == null) throw new FileNotFoundException();
        return url.openStream();
    }
}
