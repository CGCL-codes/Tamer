package org.jcyclone.core.rtc;

import org.jcyclone.core.cfg.ISystemConfig;
import org.jcyclone.core.internal.IStageWrapper;
import org.jcyclone.core.stage.IStageManager;
import java.util.List;

/**
 * The ResponseTimeController attempts to keep the response time of
 * a given stage below a given target by adjusting admission control
 * parameters for a stage.
 *
 * @author Matt Welsh
 */
public abstract class ResponseTimeController implements IResponseTimeController {

    protected static final int INIT_THRESHOLD = 1;

    protected static final int MIN_THRESHOLD = 1;

    protected static final int MAX_THRESHOLD = 1024;

    protected IStageWrapper stage;

    protected IEnqueuePredicate pred;

    protected double targetRT;

    protected ResponseTimeController(IStageManager mgr, IStageWrapper stage) throws IllegalArgumentException {
        this.stage = stage;
        ISystemConfig config = mgr.getConfig();
        this.targetRT = config.getDouble("stages." + stage.getStage().getName() + ".rtController.targetResponseTime");
        if (this.targetRT == -1) {
            this.targetRT = config.getDouble("global.rtController.targetResponseTime");
            if (this.targetRT == -1) {
                throw new IllegalArgumentException("ResponseTimeController: Must specify targetResponseTime");
            }
        }
    }

    public void setTarget(double target) {
        this.targetRT = target;
    }

    public double getTarget() {
        return targetRT;
    }

    public abstract void adjustThreshold(List fetched, long serviceTime);

    public abstract void enable();

    public abstract void disable();
}
