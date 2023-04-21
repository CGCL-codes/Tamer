package joeq.Runtime;

/**
 * @author  John Whaley <jwhaley@alum.mit.edu>
 * @version $Id: DebugInterface.java 1457 2004-03-09 22:38:33Z jwhaley $
 */
public abstract class DebugInterface {

    public static void debugwrite(String msg) {
        System.err.println(msg);
        return;
    }

    public static void debugwriteln(String msg) {
        System.err.println(msg);
        return;
    }

    public static void die(int code) {
        new InternalError().printStackTrace();
        System.exit(code);
    }
}
