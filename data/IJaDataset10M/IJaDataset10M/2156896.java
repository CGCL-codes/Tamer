package test.bwbunit.whitebox.privateproxy.testclasses;

/**
 * @author syshsp
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Bar implements Runnable {

    private static int aStaticInt = 40;

    private static Integer aStaticInteger = new Integer(4);

    private int[] intArray = new int[] { 1, 2, 3 };

    private boolean[] booArray = new boolean[] { true, false };

    public int someField;

    public String bar = "Y";

    private int privateMethod() {
        return 100;
    }

    private int privateMethod(int value) {
        return 105;
    }

    private long privateMethod(long value) {
        return 1000;
    }

    private int privateMethod(Integer value) {
        return 107;
    }

    private int privateMethod(String value) {
        return 110;
    }

    public String toString() {
        return bar + ", " + someField + " " + aStaticInt + " " + aStaticInteger + " " + intArray + " " + booArray + someField;
    }

    public void run() {
    }
}
