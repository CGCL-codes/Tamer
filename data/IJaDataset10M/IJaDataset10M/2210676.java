package com.ibm.realtime.flexotask.tuningfork;

import com.ibm.realtime.flexotask.tracing.FlexotaskTracer;
import com.ibm.tuningfork.tracegen.ILogger;
import com.ibm.tuningfork.tracegen.ITimerEvent;
import com.ibm.tuningfork.tracegen.vianative.NativeLoggerFactory;

/**
 * Implement FlexotaskTracer using the TuningFork "vianative" library
 */
class TFTracer implements FlexotaskTracer {

    /** The timer event for running the exotask or connection */
    private ITimerEvent run;

    /**
   * Make a new TFTracer
   * @param logger the native logger to use in generating the events
   * @param taskName the name to use in generating the event names
   */
    TFTracer(ILogger logger, String taskName) {
        run = logger.makeTimerEvent(taskName + "_run");
    }

    /**
   * Copy constructor used to clone a TFTracer
   */
    TFTracer(TFTracer toCopy) {
        run = (ITimerEvent) NativeLoggerFactory.cloneEvent(toCopy.run);
    }

    public FlexotaskTracer deepClone() {
        return new TFTracer(this);
    }

    public void endRun() {
        run.stop();
    }

    public void startRun() {
        run.start();
    }
}
