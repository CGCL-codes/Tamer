package uk.org.ogsadai.service.gt.execution.workflow;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if a request is invalid because a GT-compliant
 * <tt>WorkflowType</tt> bean does not contain any children. 
 *
 * @author The OGSA-DAI Project Team.
 */
public class EmptyWorkflowException extends DAIException {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Constructs a new exception.
     */
    public EmptyWorkflowException() {
        super(ErrorID.WORKFLOW_ELEMENT_HAS_NO_CHILDREN);
    }
}
