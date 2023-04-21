package org.egedede.simulation.model;

/**
 * The Interface ModelEvaluator.
 * A model evaluator is used to evaluate models.
 */
public interface ModelEvaluator {

    /**
	 * Evaluate a model.
	 * 
	 * @param model the model
	 * 
	 * @return the result of evaluation. The greater the return is, the better the model is.
	 */
    public double evaluate(Model model);
}
