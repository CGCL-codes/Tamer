package NonStaticReference;

public class B {

    /**
   */
    public void test1() {
        A.COUNTER += 1;
    }

    /**
   * @audit NonStaticFieldAccess
   */
    public void test2() {
        A interim = new A();
        interim.COUNTER += 1;
    }

    /**
   */
    public void test3() {
        A.COUNTER++;
    }

    /**
   * @audit NonStaticFieldAccess
   */
    public void test4() {
        A interim = new A();
        interim.COUNTER++;
    }
}
