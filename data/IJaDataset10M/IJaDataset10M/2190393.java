package org.wijiscommons.ssaf.drop_off.restServiceImpl;

public class TestException extends Exception {

    public TestException(String message) {
        super();
        System.out.println("Exception riased : " + message);
    }
}
