package uk.ac.lkl.migen.system.ai.feedback.strategy;

import uk.ac.lkl.migen.system.ai.feedback.intervention.FeedbackMessageType;
import uk.ac.lkl.migen.system.ai.feedback.intervention.Intervention;
import uk.ac.lkl.migen.system.ai.reasoning.request.FeedbackRequest;
import uk.ac.lkl.migen.system.ai.um.ShortTermLearnerModel;

/**
 * A feedback strategy in MiGen. This is produced by the reasoning layer and is
 * used by the feedback layer.
 * 
 * @author sergut
 * 
 */
public abstract class FeedbackStrategy {

    private FeedbackStrategyType feedbackStrategyType;

    protected FeedbackStrategy(FeedbackStrategyType type) {
        this.feedbackStrategyType = type;
    }

    public FeedbackStrategyType getType() {
        return feedbackStrategyType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FeedbackStrategy)) return super.equals(o);
        FeedbackStrategy other = (FeedbackStrategy) o;
        if (this.feedbackStrategyType != other.feedbackStrategyType) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return feedbackStrategyType.hashCode();
    }

    /**
     * By default, feedback strategies are not always interruptive, and the 
     * decision is taken at the feedback generator depending on several 
     * factors at the moment they are fired. 
     * 
     * Some feedback strategies are designed to be always interruptive, 
     * however, and should override this method.  
     * 
     * @return false
     */
    @Deprecated
    public boolean isAlwaysInterruptive() {
        return false;
    }

    /**
     * By default, feedback strategies are sometimes interruptive. The 
     * decision is taken at the feedback generator depending on several 
     * factors at the moment they are fired. 
     * 
     * Some feedback strategies are designed not to be interruptive ever, 
     * however, and should override this method.  
     * 
     * @return false
     */
    @Deprecated
    public boolean isNeverInterruptive() {
        return false;
    }

    /**
     * Returns the message to show given a certain message type
     *  
     * @return the message to show given a certain message type
     */
    @Deprecated
    public abstract String getMessage(FeedbackMessageType messageType);

    /**
     * Generates an intervention given a feedback request (maybe null), the learner 
     * model, and a feedback loop counter. 
     * 
     * @param request 	the feedback request that resulted in this feedback (null, if this is system-driven)
     * @param um 	the learner model
     * @param counter 	the feedback loop counter
     * 
     * @return an {@link Intervention}
     */
    public Intervention getIntervention(FeedbackRequest request, ShortTermLearnerModel um) {
        throw new RuntimeException("This method must be abstract! Make it abstract as soon the subclasses have implemented it!");
    }
}
