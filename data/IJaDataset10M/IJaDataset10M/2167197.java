package com.garmin.fit;

import java.io.IOException;

public class FitRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 970213851171816735L;

    public FitRuntimeException() {
        super();
    }

    public FitRuntimeException(IOException e) {
        super(e);
    }

    public FitRuntimeException(String string) {
        super(string);
    }
}
