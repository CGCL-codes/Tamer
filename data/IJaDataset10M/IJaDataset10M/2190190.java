package dxc.junit.opcodes.invokespecial.jm;

/**
 * @author fjost
 *
 */
public class TSuper {

    public int toInt() {
        return 5;
    }

    public int toInt(float v) {
        return (int) v;
    }

    public int testArgsOrder(int a, int b) {
        return a / b;
    }

    public native int toIntNative();

    public static int toIntStatic() {
        return 5;
    }

    protected int toIntP() {
        return 5;
    }

    private int toIntPvt() {
        return 5;
    }
}
