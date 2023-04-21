package com.thoughtworks.tools.profit;

import java.io.PrintStream;

public interface IErrorHandler {

    void logException(Exception e);

    void logException(Exception e, String message, Object... args);

    void print(PrintStream out);
}
