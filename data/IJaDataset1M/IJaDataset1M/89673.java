package consciouscode.seedling.config;

import consciouscode.seedling.BranchNode;
import org.apache.commons.logging.Log;

/**
 *
 */
public interface EvaluationContext {

    public BranchNode getBaseBranch();

    public Log getLog();

    public String describe();
}
