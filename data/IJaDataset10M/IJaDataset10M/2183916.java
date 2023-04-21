package org.springframework.webflow.execution;

import org.springframework.core.style.StylerUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.Flow;

/**
 * Static factory for creating commonly used flow execution listener criteria.
 * 
 * @see org.springframework.webflow.execution.FlowExecutionListenerCriteria
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowExecutionListenerCriteriaFactory {

    /**
	 * Returns a wild card criteria that matches all flows.
	 */
    public static FlowExecutionListenerCriteria allFlows() {
        return new WildcardFlowExecutionListenerCriteria();
    }

    /**
	 * Returns a criteria that just matches a flow with the specified id.
	 * @param flowId the flow id to match
	 */
    public static FlowExecutionListenerCriteria flow(String flowId) {
        return new FlowIdFlowExecutionListenerCriteria(flowId);
    }

    /**
	 * Returns a criteria that just matches a flow if it is identified by one of
	 * the specified ids.
	 * @param flowIds the flow id to match
	 */
    public static FlowExecutionListenerCriteria flows(String[] flowIds) {
        return new FlowIdFlowExecutionListenerCriteria(flowIds);
    }

    /**
	 * A flow execution listener criteria implementation that matches for all
	 * flows.
	 */
    public static class WildcardFlowExecutionListenerCriteria implements FlowExecutionListenerCriteria {

        /**
		 * The string representation of the wildcard flow id.
		 */
        public static final String WILDCARD_FLOW_ID = "*";

        public boolean appliesTo(Flow flow) {
            return true;
        }

        public String toString() {
            return WILDCARD_FLOW_ID;
        }
    }

    /**
	 * A flow execution listener criteria implementation that matches flows with
	 * a specified id.
	 */
    public static class FlowIdFlowExecutionListenerCriteria implements FlowExecutionListenerCriteria {

        /**
		 * The flow ids that apply for this criteria.
		 */
        private String[] flowIds;

        /**
		 * Create a new flow id matching flow execution listener criteria
		 * implemenation.
		 * @param flowId the flow id to match
		 */
        public FlowIdFlowExecutionListenerCriteria(String flowId) {
            Assert.notNull(flowId, "The flow id is required");
            this.flowIds = new String[] { flowId };
        }

        /**
		 * Create a new flow id matching flow execution listener criteria
		 * implemenation.
		 * @param flowIds the flow ids to match
		 */
        public FlowIdFlowExecutionListenerCriteria(String[] flowIds) {
            Assert.notEmpty(flowIds, "The flow id is required");
            this.flowIds = flowIds;
        }

        public boolean appliesTo(Flow flow) {
            for (int i = 0; i < flowIds.length; i++) {
                if (flowIds[i].equals(flow.getId())) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return new ToStringCreator(this).append("flowIds", StylerUtils.style(flowIds)).toString();
        }
    }
}
