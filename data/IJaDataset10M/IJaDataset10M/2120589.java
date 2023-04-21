package basic;

/**
 * References a anothers class static variable.
 *
 * @author Axel Terfloth
 */
public class RefForeignInstanceMethodSource {

    public void foo() {
        RefForeignInstanceMethodTarget target = null;
        target.foo();
    }
}
