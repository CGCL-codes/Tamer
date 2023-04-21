package jtestdriver.exception;

public class TestDriverInitialisationException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public TestDriverInitialisationException(String name) {
        super(name);
    }
}
