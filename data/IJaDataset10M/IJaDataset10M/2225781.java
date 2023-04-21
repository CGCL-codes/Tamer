package slice;

public class TestMultiTarget {

    static void doNothing(Object o) {
    }

    /**
   * test a virtual call with multiple targets. slice should include statements
   * assigning to a
   * 
   * @param args
   */
    public static void main(String[] args) {
        A a = null;
        if (args[0] == null) {
            a = new A();
        } else {
            a = new B();
        }
        Object x = a.foo();
        doNothing(x);
    }
}
