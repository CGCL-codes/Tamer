package uut.makeempty;

import net.sf.jour.test.MethodsCalledMonitor;

public class MakeEmptyMethodCase extends MethodsCalledMonitor {

    public MakeEmptyMethodCase() {
        debug("1");
        debug("2", 2);
        error("e", 3);
        debug("4", true);
    }

    public void debug(String message) {
        called();
        System.out.println(message);
    }

    public void debug(String message, String v) {
        called();
        System.out.println(message + " " + v);
    }

    public void debug(String message, String v, String v2) {
        called();
        System.out.println(message + " " + v + " " + v2);
    }

    public void debug(String message, int v) {
        called();
        System.out.println(message + " " + String.valueOf(v));
    }

    public void debug(String message, boolean v) {
        called();
        System.out.println(message + " " + v);
    }

    public void error(String message, long v) {
        called();
    }
}
