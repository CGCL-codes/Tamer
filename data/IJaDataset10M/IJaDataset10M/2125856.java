package dxc.junit.opcodes.invokevirtual.jm;

import dxc.junit.opcodes.invokevirtual.jm.TSuper;

public class T_invokevirtual_14 extends TSuper {

    public boolean run() {
        int a = 123;
        int b = 659;
        if (testArgsOrder(300, 3) == 100) if (a == 123) if (b == 659) return true;
        return false;
    }
}
