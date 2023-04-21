package consciouscode.seedling.config.properties;

import consciouscode.seedling.BranchNode;
import consciouscode.seedling.config.ConfigEvaluatorContext;
import consciouscode.seedling.config.ConstructionContext;
import consciouscode.seedling.config.EvaluationException;
import consciouscode.seedling.config.EvaluationResult;
import consciouscode.seedling.config.PropertyEvaluationException;
import consciouscode.seedling.config.SyntaxException;

/**
 *
 */
abstract class BaseEvaluationContext implements EvaluationContext {

    protected final ConfigEvaluatorContext myEvaluatorContext;

    BaseEvaluationContext(ConfigEvaluatorContext outerContext) {
        myEvaluatorContext = outerContext;
    }

    BaseEvaluationContext(ConstructionContext outerContext) {
        myEvaluatorContext = outerContext;
    }

    public ConfigEvaluatorContext getEvaluatorContext() {
        return myEvaluatorContext;
    }

    public ConstructionContext getConstructionContext() {
        return null;
    }

    public BranchNode getBaseBranch() {
        return myEvaluatorContext.getLocation().getBaseBranch();
    }

    public EvaluationResult superEvaluate() throws EvaluationException {
        throw new EvaluationException("'super' isn't supported here");
    }

    public EvaluationResult evaluateThisProperty(String name) throws SyntaxException, EvaluationException, PropertyEvaluationException {
        throw new EvaluationException("'this' isn't supported here");
    }
}
