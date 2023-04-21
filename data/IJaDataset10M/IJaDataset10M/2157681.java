package org.jmlspecs.ajmlrac.testcase.racrun;

public class old_exp {

    int x = 10;

    void m1() {
        x = x + 10;
    }

    void m2() {
        x = x + 10;
    }

    void m3() {
        x = x + 10;
    }

    void m4() {
        x = x + x + 10;
    }

    public static void main(String args[]) {
        old_exp exp = new old_exp();
        int fcnt = 0;
        try {
            exp.m1();
            exp.m3();
            exp.m4();
        } catch (Throwable e) {
            fcnt++;
        }
        try {
            exp.m2();
            fcnt++;
        } catch (Throwable e) {
        }
        if (fcnt > 0) System.out.println(fcnt + "F(old_exp.java)");
    }
}
