package edu.rice.cs.drjava.model.compiler;

/** A compiler interface to find Javac (1.5.0+) from the classpath, but to do so via a compiler proxy so that the 
 *  compiler classes can be fully unloaded/reloaded every time it is used.
 *
 *  @version $Id: Javac160FromClassPath.java 3573 2006-03-03 23:04:28Z rcartwright $
 */
public class Javac160FromClassPath extends CompilerProxy {

    public static final CompilerInterface ONLY = new Javac160FromClassPath();

    private static final String VERSION = System.getProperty("java.specification.version");

    /** Private constructor due to singleton. */
    private Javac160FromClassPath() {
        super("edu.rice.cs.drjava.model.compiler.Javac160Compiler", Javac160FromClassPath.class.getClassLoader());
    }

    public boolean isAvailable() {
        return VERSION.equals("1.6") && super.isAvailable();
    }

    public String getName() {
        return "javac 1.6.0";
    }
}
