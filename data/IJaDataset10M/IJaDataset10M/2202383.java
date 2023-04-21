package uk.org.ogsadai.dqp.presentation.common;

import uk.org.ogsadai.client.toolkit.Workflow;

/**
 * Transforms a workflow into another workflow.
 *
 * @author The OGSA-DAI Project Team
 */
public interface WorkflowTransformer {

    /**
     * Transforms a workflow.
     * 
     * @param workflow input workflow
     * 
     * @return the transformed workflow
     */
    Workflow transform(Workflow workflow);
}
