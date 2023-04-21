package org.ucdetector.mini_cycle;

public class MiniA {

    static final MiniC MINI_C = new MiniC();

    MiniB getB() {
        return new MiniB();
    }
}
