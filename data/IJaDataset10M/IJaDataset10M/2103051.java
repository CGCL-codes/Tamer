package org.amplafi.flow.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.amplafi.flow.FlowValidationResult;
import org.amplafi.flow.FlowValidationTracking;
import static com.sworddance.util.CUtilities.*;

/**
 * A {@link org.amplafi.flow.FlowValidationResult} that considers
 * all trackings as problematic. This should currently be the desired behavior.
 */
public class ReportAllValidationResult implements FlowValidationResult {

    private List<FlowValidationTracking> trackings;

    public ReportAllValidationResult(FlowValidationTracking... flowValidationTrackings) {
        addTracking(flowValidationTrackings);
    }

    @Override
    public boolean isValid() {
        return isEmpty(trackings);
    }

    /**
     * @param flowValidationTrackings nulls are filtered out.
     */
    @Override
    public FlowValidationResult addTracking(FlowValidationTracking... flowValidationTrackings) {
        if (flowValidationTrackings != null) {
            if (trackings == null) {
                trackings = new ArrayList<FlowValidationTracking>();
            }
            addTracking(Arrays.asList(flowValidationTrackings));
        }
        return this;
    }

    /**
     * @param flowValidationTrackings
     */
    public void addTracking(Collection<FlowValidationTracking> flowValidationTrackings) {
        for (FlowValidationTracking flowValidationTracking : flowValidationTrackings) {
            if (flowValidationTracking != null) {
                trackings.add(flowValidationTracking);
            }
        }
    }

    /**
     * add a {@link SimpleValidationTracking} if valid is false.
     * @param valid add if false
     * @param messageKey
     * @param messageParams
     */
    @Override
    public ReportAllValidationResult addTracking(boolean valid, String activityKey, String messageKey, Object... messageParams) {
        if (!valid) {
            addTracking(new SimpleValidationTracking(messageKey, messageParams));
        }
        return this;
    }

    @Override
    public List<FlowValidationTracking> getTrackings() {
        return trackings;
    }

    public void merge(FlowValidationResult addedFlowValidationResult) {
        if (!addedFlowValidationResult.isValid()) {
            this.addTracking(addedFlowValidationResult.getTrackings());
        }
    }

    @Override
    public String toString() {
        if (isValid()) {
            return this.getClass().getSimpleName() + " no problems.";
        } else {
            return this.getClass().getSimpleName() + " failed. Trackings: " + StringUtils.join(this.getTrackings(), ';');
        }
    }
}
