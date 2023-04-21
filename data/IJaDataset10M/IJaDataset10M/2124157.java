package com.k_int.IR;

public class TimeoutExceededException extends Exception {

    public TimeoutExceededException() {
        super();
    }

    public TimeoutExceededException(String text) {
        super(text);
    }
}
