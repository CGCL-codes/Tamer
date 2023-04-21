package org.slasoi.gslam.core.pac;

import org.slasoi.gslam.core.control.Policy;

/**
 * Interface that defines the methods to be implemented by any Provisioning and Adjustment Component.
 * 
 * @author Beatriz Fuentes
 * 
 */
public interface ProvisioningAdjustment {

    /**
     * Exception message when the plan is already being executed.
     */
    String PLAN_FOUND_EXCEPTION = "Plan already in execution";

    /**
     * Exception message to be used when there is an error in the incoming Plan.
     */
    String PLAN_FORMAT_EXCEPTION = "Error in the format of the plan";

    /**
     * Triggers the execution of a provisioning plan.
     * 
     * @param plan
     *            the plan to be executed
     * @throws PlanFoundException
     *             if the plan has been already sent for execution
     * @throws PlanFormatException
     *             if the plan is not corrected built
     */
    void executePlan(Plan plan) throws PlanFoundException, PlanFormatException;

    /**
     * Cancels the execution of a plan.
     * 
     * @param planId
     *            the identifier of the plan to be cancelled
     * @throws PlanNotFoundException
     *             if the plan is not being executed
     */
    void cancelExecution(String planId) throws PlanNotFoundException;

    /**
     * POC informs the PAC that a given action is being executed.
     * 
     * @param planId
     *            identifier of the plan affected by the action
     * @param action
     *            action being executed
     * @param estimatedTime
     *            estimation of time for the action to finish
     * @throws PlanNotFoundException
     *             if the plan is not under PAC's control
     */
    void ongoingAction(String planId, Task action, long estimatedTime) throws PlanNotFoundException;

    /**
     * Method to inform about the status of a given plan.
     * 
     * @param planId
     *            the identifier of the plan
     * @return the plan status.
     * @throws PlanNotFoundException
     */
    Status getPlanStatus(String planId) throws PlanNotFoundException;

    String queryMonitoringDatabase(String ServiceManagerId, String query);

    /**
     * Set policies to the PAC. To be used from the business layer.
     * 
     * @param policyClassType
     *            type of policy (Adjustment/Negotiation)
     * @param policies
     *            new policies
     * @return result of the action
     */
    public int setPolicies(String policyClassType, Policy[] policies);

    /**
     * Get the policies used by the PAC. To be used from the business layer.
     * 
     * @param policyClassType
     *            type of policy (Adjustment/Negotiation)
     * @return policies the PAC policies
     */
    public Policy[] getPolicies(String policyClassType);

    /**
     * A structure indicating the actions that need to be taken to provision a given SLA. A Plan is a set of Tasks (or
     * actions), with the form of Directed Acyclic Graph (DAG). Children of the same parent may be executed in parallel.
     * A child node can only be executed when all their parents have finished.
     * 
     * @author fuentes
     * 
     */
    public interface Plan {

        /**
         * sets the identifier of the plan.
         * 
         * @param planId
         *            the identifier of the plan
         */
        public void setPlanId(String planId);

        /**
         * gets the planId.
         * 
         * @return the plan identifier
         */
        public String getPlanId();
    }

    /**
     * Node of a plan. Each Task represents an Action to be executed.
     * 
     * @author fuentes
     * 
     */
    public interface Task {

        /**
         * Retrieves the name of the action.
         * 
         * @return the name of the action
         */
        public String getActionName();
    }

    /**
     * Exception that will be thrown when a request arrives for a plan that is already under PAC's control.
     * 
     * @author fuentes
     * 
     */
    public class PlanFoundException extends Exception {

        /**
         * serial version uid.
         */
        private static final long serialVersionUID = 8931201790315877272L;

        /**
         * Default constructor.
         */
        public PlanFoundException() {
            super(PLAN_FOUND_EXCEPTION);
        }

        /**
         * Constructor.
         * 
         * @param cause
         *            message to be included in the exception information.
         */
        public PlanFoundException(String cause) {
            super(cause);
        }
    }

    /**
     * Exception to be thrown when the plan is malformed.
     * 
     * @author fuentes
     * 
     */
    public class PlanFormatException extends Exception {

        /**
         * serial versioin uuid.
         */
        private static final long serialVersionUID = -474568664267581161L;

        /**
         * Constructor.
         */
        public PlanFormatException() {
            super(PLAN_FORMAT_EXCEPTION);
        }
    }

    /**
     * Exception to be triggered when a plan that should be under PAC's control, is not found.
     * 
     * @author fuentes
     * 
     */
    public class PlanNotFoundException extends Exception {

        /**
         * serial version uuid.
         */
        private static final long serialVersionUID = -7164653700760652771L;

        /**
         * Constructor.
         * 
         * @param cause
         *            message to be included in the exception information.
         */
        public PlanNotFoundException(String cause) {
            super(cause);
        }
    }

    /**
     * Possible status of a plan.
     * 
     * @author fuentes
     * 
     */
    public enum Status {

        CREATED, PROVISIONING, PROVISIONED, PROVISION_FAILED, VIOLATED, EXPIRED
    }
}
