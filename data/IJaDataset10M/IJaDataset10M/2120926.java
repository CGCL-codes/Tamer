package oxygen.logging;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TestOxyLogger extends Logger {

    private int i = 0;

    private String s = "";

    private boolean b = false;

    private char c = '0';

    private Object o = null;

    public TestOxyLogger(String name) {
        super(name, null);
    }

    public void setInt1(int i) {
        this.i = i;
    }

    public void setString2(String s) {
        this.s = s;
    }

    public void setBoolean3(boolean b) {
        this.b = b;
    }

    public void setChar4(char c) {
        this.c = c;
    }

    public void setObject5(Object o) {
        this.o = o;
    }

    public String toString() {
        return "i = " + i + " s = " + s + " b = " + b + " c = " + c + " o = " + o;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.manager", "oxygen.logging.OxyLogManager");
        Logger.getLogger("ab").log(Level.SEVERE, "hi");
        LogManager lm = LogManager.getLogManager();
        System.out.println("LogManager.getLogManager.getClass.getName: " + lm.getClass().getName());
        System.out.println("LogManager.getLogManager: " + lm);
        for (Enumeration enum0 = lm.getLoggerNames(); enum0.hasMoreElements(); ) {
            String lgname = (String) enum0.nextElement();
            Logger lg = lm.getLogger(lgname);
            System.out.println("'" + lgname + "': " + lg);
        }
        System.out.println();
    }
}
