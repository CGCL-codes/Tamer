package eu.irreality.age;

public class BSHAssertionFailedException extends Exception {

    public BSHAssertionFailedException() {
        super("Assertion failed (no assertion message)");
    }

    public BSHAssertionFailedException(String s) {
        super("Assertion failed: " + s);
    }
}
