package com.plasticcode.performance;

public interface MeasuredOperation {

    void setup(int children) throws Exception;

    void run(int children, int samples) throws Exception;
}
