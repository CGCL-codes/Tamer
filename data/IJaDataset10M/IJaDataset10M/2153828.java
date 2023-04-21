package org.jmlspecs.ajmlrac.testcase.racrun;

interface invariant_spec_intf_I {

    int val();
}

public class invariant_spec_intf implements invariant_spec_intf_I {

    public int val() {
        return val;
    }

    void m() {
    }

    public int val = 10;

    public static void main(String args[]) {
        invariant_spec_intf o = new invariant_spec_intf();
        int fcnt = 0;
        try {
            o.val = 10;
            o.m();
        } catch (Throwable e) {
            fcnt++;
        }
        try {
            o.val = -10;
            o.m();
        } catch (Throwable e) {
            fcnt++;
            System.err.println(e.getMessage());
        }
        if (fcnt > 0) System.out.println(fcnt + "F(invariant_spec_intf.java)");
    }
}
