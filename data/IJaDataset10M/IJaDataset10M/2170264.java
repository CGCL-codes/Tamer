package dxc.junit.opcodes.lreturn.jm;

public class T_lreturn_8 {

    private synchronized long test() {
        return 0l;
    }

    public boolean run() {
        test();
        return true;
    }
}
