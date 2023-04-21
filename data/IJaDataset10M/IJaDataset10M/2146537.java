package net.sourceforge.purrpackage.test.specialCases.staticinit;

public class StaticInit {

    static {
        if (System.getProperty("foo") != null) {
            wasFoo = "yes";
        } else {
            wasFoo = "no";
        }
    }

    public static final String wasFoo;

    static final String something = "Foo" + "bar" + "baz";

    public void coveredHere() {
        this.getClass().getName();
    }
}
