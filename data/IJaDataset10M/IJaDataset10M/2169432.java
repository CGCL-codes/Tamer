package qa.negative_tests;

public class FailTest extends Object {

    public FailTest() {
        System.out.println("FailTest object has been created");
        System.err.println("FailTest has failed for demonstration purposes");
        System.out.println("Exiting with non-zero exit value");
        System.exit(1);
    }

    public static void main(String args[]) {
        FailTest p = new FailTest();
    }
}
