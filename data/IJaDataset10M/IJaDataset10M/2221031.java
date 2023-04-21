package tests.test;

/**
 * @author "Michael Maaser"
 *
 */
class MyException extends Exception {

    MyException(String info) {
        super(info);
    }
}

public class ExceptionError {

    private ExceptionError() {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            doSomething();
        } catch (MyException e) {
        }
        System.out.println("Ready.");
    }

    public static void doSomething() throws MyException {
        System.out.println("In doSomeThing.");
    }
}
