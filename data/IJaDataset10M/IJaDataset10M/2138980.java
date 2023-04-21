package org.oobench.exceptions;

import java.util.*;

public class WithoutExceptionsPerformance extends ExceptionsBenchmark {

    public int getMinorNumber() {
        return 1;
    }

    public int simpleWithoutException() {
        return 1;
    }

    public int deepWithoutException2() {
        return 1;
    }

    public int deepWithoutException() {
        int result = deepWithoutException2();
        if (result == 2) {
        } else if (result == 1) {
        }
        return 1;
    }

    public void except(int count) {
        reset();
        for (int i = 0; i < count; i++) {
            proceed();
            if (simpleWithoutException() > 0) {
            }
        }
        reset();
    }

    public void exceptDeep(int count) {
        reset();
        for (int i = 0; i < count; i++) {
            proceed();
            if (deepWithoutException() > 0) {
            }
        }
        reset();
    }

    public static void main(String[] args) {
        showLocation();
        testExceptions(WithoutExceptionsPerformance.class, 500000, "without exceptions", args);
    }
}
