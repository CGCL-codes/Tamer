package org.jmlspecs.ajmlrac.testcase.racrun;

public class SpecPublicConstructor3 extends SpecPublicConstructor3Super {

    void m1(boolean b) {
    }

    void m2(int x) {
    }

    public static void main(String args[]) {
        SpecPublicConstructor3 test = new SpecPublicConstructor3();
        int fcnt = 0;
        try {
            test.m1(true);
            fcnt++;
        } catch (Throwable e) {
            fcnt++;
        }
        try {
            test.m2(10);
        } catch (Throwable e) {
            fcnt++;
        }
        if (fcnt > 0) {
            System.out.println(fcnt + "F(SpecPublicConstructor3.java)");
        }
    }
}

class SpecPublicConstructor3Super {

    protected SpecPublicConstructor3Super() {
    }

    private SpecPublicConstructor3Super(boolean b) {
    }

    private SpecPublicConstructor3Super(int x) {
    }
}
