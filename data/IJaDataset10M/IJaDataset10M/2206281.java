package com.example.throwing;

import com.seitenbau.testing.shared.config.TracerConfiguration;
import com.seitenbau.testing.shared.tracer.RmiTracer;

public class ThrowingClass {

    String value = "Rainer";

    TracerConfiguration cfg = new TracerConfiguration(RmiTracer.class);

    public int calculate(int value1, int value2) {
        int result = value1 + value2;
        if (result == 23) {
            throw new RuntimeException("Was 23");
        }
        return result;
    }

    public static int calculateStatic(int value1, int value2) {
        int result = value1 + value2;
        if (result == 23) {
            throw new RuntimeException("Was 23");
        }
        return result;
    }
}
