package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * A test step in a test run. Extends IStep.
 */
@IID("{5FF530DD-245E-4F97-A59F-3DE69FFCC55E}")
public interface IStep2 extends com.serena.xmlbridge.adapter.qc9.gen.IStep {

    /**
     * The BPStepParamFactory for the current step.
     */
    @VTID(30)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject bpStepParamFactory();

    /**
     * Returns the Run object handling this step.
     */
    @VTID(31)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject run();
}
