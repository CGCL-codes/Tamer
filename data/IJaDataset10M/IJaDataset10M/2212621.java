package uk.org.ogsadai.activity.request;

import uk.org.ogsadai.activity.workflow.Workflow;

/**
 * Validates a workflow.
 *
 * @author The OGSA-DAI Team.
 */
public interface WorkflowValidator {

    /**
     * Validate the specified workflow. If it cannot be validated an exception
     * is thrown.
     * 
     * @param workflow
     *            the workflow to validate
     * @throws ValidationException
     *             if the workflow was invalid
     */
    public void validate(Workflow workflow) throws ValidationException;
}
