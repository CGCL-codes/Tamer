package CheckPossibleAccessRights3.com.p1;

public class Class1 {

    /**
   * @audit MinimizeAccessViolation
   */
    protected void f() {
    }
}

class Class2 extends Class1 {

    /**
   * @audit MinimizeAccessViolation
   */
    protected void f() {
    }

    private void foo() {
        f();
    }
}
