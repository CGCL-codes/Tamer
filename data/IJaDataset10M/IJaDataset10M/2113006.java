package com.bbn.vessel.core.runtime.feedback;

import java.util.HashSet;
import java.util.Set;
import com.bbn.vessel.core.model.ocm.BinaryThreshold;
import com.bbn.vessel.core.model.ocm.CountThreshold;
import com.bbn.vessel.core.model.ocm.FeedbackRule;
import com.bbn.vessel.core.model.ocm.MeasureThreshold;
import com.bbn.vessel.core.runtime.trigger.Trigger;

/**
 * This class provides runtime functionality for a graph node.
 * <p>
 * Provide feedback based on the number of times a particular event has
 * occurred.
 * 
 * this should not be confused with MeasureCount, which provides debreif based on the same criteria
 * 
 */
public class CountFeedbackComponent extends FeedbackComponent {

    private Set<String> incrementInTrs;

    private MeasureThreshold<?> previousThreshold = null;

    private int count = -1;

    private int withinCount = -1;

    private boolean enabled = false;

    private boolean paused = false;

    @Override
    protected void configure() {
        super.configure();
        incrementInTrs = new HashSet<String>(getSignalNamesByPrefix("increment.in_tr"));
    }

    @Override
    protected void doReset() {
        count = -1;
        withinCount = -1;
        previousThreshold = null;
        doIncrement();
    }

    @Override
    protected void doDisable() {
        enabled = false;
    }

    @Override
    protected void doEnable() {
        enabled = true;
        if (count < 0) {
            doIncrement();
        }
    }

    @Override
    protected void doPause() {
        paused = true;
    }

    @Override
    protected void doResume() {
        paused = false;
        if (count < 0) {
            doIncrement();
        }
    }

    @Override
    protected void receive(Object o) {
        super.receive(o);
        if ((o instanceof Trigger) && incrementInTrs.contains(((Trigger) o).getName())) {
            doIncrement();
        }
    }

    private void doIncrement() {
        if (!enabled || paused) {
            return;
        }
        count++;
        withinCount++;
        MeasureThreshold<?> currentThreshold = null;
        for (MeasureThreshold<?> thresh : measure.getThresholds()) {
            if (thresh instanceof CountThreshold) {
                int c = ((CountThreshold) thresh).getThresholdValue();
                if (c > count) {
                    break;
                }
            } else if (thresh instanceof BinaryThreshold) {
                boolean b = ((BinaryThreshold) thresh).getThresholdValue();
                int c = (b ? 1 : 0);
                if (c > count) {
                    break;
                }
            }
            currentThreshold = thresh;
        }
        if (currentThreshold != null) {
            if (!currentThreshold.equals(previousThreshold)) {
                for (FeedbackRule rule : currentThreshold.getFeedbackRules()) {
                    if (rule.getScope() == FeedbackRule.Scope.ENTERING) {
                        publish(buildCommandForRule(rule));
                    }
                }
                withinCount = -1;
            } else {
                for (FeedbackRule rule : currentThreshold.getFeedbackRules()) {
                    if (rule.getScope() == FeedbackRule.Scope.WITHIN && (withinCount + 1) >= rule.getFrequency()) {
                        withinCount = -1;
                        publish(buildCommandForRule(rule));
                    }
                }
            }
        }
        previousThreshold = currentThreshold;
    }
}
