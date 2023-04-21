package genericAlgorithm.tsp.handlers;

import genericAlgorithm.framework.appcontroller.SequenceResolver;
import genericAlgorithm.ui.GAViewHelperHandler;

/**
 *
 * @author Oz
 */
public class ExecutionIterationResolver implements SequenceResolver {

    @Override
    public int getIterationNumber() {
        return GAViewHelperHandler.getExecutionSequenceSize();
    }
}
