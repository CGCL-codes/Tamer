package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * Represents a test run.
 */
@IID("{34023178-4154-4B16-80A4-6C610096648A}")
public interface IRun extends com.serena.xmlbridge.adapter.qc9.gen.IBaseFieldEx {

    /**
     * The Run result location, a UNC path.
     */
    @VTID(23)
    java.lang.String resultLocation();

    /**
     * The Run name.
     */
    @VTID(24)
    java.lang.String name();

    /**
     * The Run name.
     */
    @VTID(25)
    void name(java.lang.String pVal);

    /**
     * The Run status.
     */
    @VTID(26)
    java.lang.String status();

    /**
     * The Run status.
     */
    @VTID(27)
    void status(java.lang.String pVal);

    /**
     * The Step Factory for the current run.
     */
    @VTID(28)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject stepFactory();

    /**
     * The test ID of the test that has been run.
     */
    @VTID(29)
    int testId();

    /**
     * The ExtendedStorage object for the current run.
     */
    @VTID(30)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject extendedStorage();

    /**
     * Copies design steps into the test run of an executed test.
     */
    @VTID(31)
    void copyDesignSteps();

    /**
     * Copies all run execution steps, including new added steps, into the design steps of the corresponding planning test.
     */
    @VTID(32)
    void copyStepsToTest();

    /**
     * The ID of the test set to which the run belongs.
     */
    @VTID(33)
    int testSetID();

    /**
     * The step parameters of this run.
     */
    @VTID(34)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject params(int sourceMode);

    /**
     * Updates the texts of the run's steps by resolving the parameter values at run time.
     */
    @VTID(35)
    void resolveStepsParameters(boolean updateLocalCache);

    /**
     * The number of this instance of the design test in the test set.
     */
    @VTID(36)
    int testInstance();

    /**
     * Cancels the run.
     */
    @VTID(37)
    void cancelRun();
}
