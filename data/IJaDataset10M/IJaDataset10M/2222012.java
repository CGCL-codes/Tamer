package tests;

import tcl.lang.*;

public class JavaLoadTclBlend {

    public static void main(String[] args) throws Exception {
        Interp interp = null;
        try {
            interp = new Interp();
            interp.eval("expr {1 + 2}");
            if (!interp.getResult().toString().equals("3")) System.exit(-1);
        } finally {
            if (interp != null) interp.dispose();
        }
        System.exit(0);
    }
}
