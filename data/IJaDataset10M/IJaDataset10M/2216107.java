package jde.util;

import java.util.StringTokenizer;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * CompileServer.java provides an interface between 
 * the command line(or from Emacs) and the javac compiler.
 * @see com.sun.tools.javac.main
 * Calling the compile server instead of doing javac will 
 * avoid the start up time that occur with every incovation
 * of javac.exe
 *
 * Created: Sun Aug 12 21:56:50 2001
 *
 * @author <a href="mailto:jslopez@alum.mit.edu"></a>
 * @version 1.0
 * @since jde-2.2.8beta5
 */
public class CompileServer {

    private static Class compiler;

    static {
        try {
            compiler = Class.forName("com.sun.tools.javac.Main");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Method compile;

    static {
        try {
            if (compiler != null) {
                Class[] params = new Class[] { String[].class };
                compile = compiler.getMethod("compile", params);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param args a <code>String[]</code> with
     * the arguments to passed to compiler.
     * @see com.sun.tools.javac.Main#compiler(String[] args)
     */
    public static void compile(String[] args) {
        try {
            if (compile != null) {
                Object[] arguments = new Object[] { args };
                System.out.println(compile.invoke(compiler.newInstance(), arguments));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param commands a <code>String[]</code> with
     * the arguments to passed to compiler.
     * @see com.sun.tools.javac.Main#compiler(String[] args)
     */
    public static void compile(String commands) {
        StringTokenizer st = new StringTokenizer(commands);
        String[] args = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            args[i] = st.nextToken();
        }
        compile(args);
    }

    public static void main(String[] args) {
        CompileServer.compile("CompileServer.java");
    }
}
