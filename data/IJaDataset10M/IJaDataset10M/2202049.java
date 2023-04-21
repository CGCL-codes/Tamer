package org.atlantal.api.workflow;

import java.util.Map;
import org.atlantal.api.app.rundata.AtlantalRequest;

/**
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 */
public interface WorkflowState {

    /**
     * @return id
     */
    Integer getId();

    /**
     * @return label
     */
    String getLabel();

    /**
     * @return script
     */
    String getScript();

    /**
     * @return transitions
     * @throws WorkflowException TODO
     */
    Map getTransitions() throws WorkflowException;

    /**
     * @param request rundata
     * @param context context
     * @throws WorkflowException TODO
     */
    void process(AtlantalRequest request, WorkflowContext context) throws WorkflowException;
}
