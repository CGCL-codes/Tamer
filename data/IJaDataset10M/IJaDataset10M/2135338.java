package sketch.specs.symbc;

import gov.nasa.jpf.symbc.Debug;

public class MyClassBoolean {

    public void myMethod(boolean x, boolean y) {
        if (x) {
            if (y) {
                System.out.println("reach here x y is true");
            } else {
                System.out.println("reach here x true y is false");
            }
        } else {
            if (y) {
                System.out.println("reach here x false y is true");
            } else {
                System.out.println("reach here x false y is false");
            }
        }
    }

    public static void main(String[] args) {
        MyClassBoolean mc = new MyClassBoolean();
        mc.myMethod(true, true);
        Debug.printPC("\nMyClassBoolean.myMethod Path Condition: ");
    }
}
