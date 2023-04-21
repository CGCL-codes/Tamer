package net.sourceforge.purrpackage.test.demo.ideal;

/**
 * Ideally, a class is covered by unit tests in the same package, like this one.
 */
public class IdealExample {

    String x;

    String y = "something";

    public IdealExample(String x) {
        this.x = x;
    }

    /**
	 * Comments occur
	 */
    public void before() {
        x += " ";
    }

    public String after() {
        String result = "postfix";
        return x + result;
    }

    /** Note the implicit default constructor. */
    public class Brancher {

        public String branch(boolean a, boolean b) {
            String y;
            y = a ? "foo" : b ? "bar" : "baz";
            return y;
        }
    }
}
