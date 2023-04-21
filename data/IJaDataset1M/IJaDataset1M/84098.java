package com.joe.common.exception;

public class ExceptionTest {

    public void testException() throws Exception {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            throw new Exception("you can't divied by zero!");
        }
    }

    public static void main(String[] args) throws Exception {
        new ExceptionTest().testException();
    }
}
