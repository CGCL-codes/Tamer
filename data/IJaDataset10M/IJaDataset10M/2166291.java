package org.jactr.core.module.declarative.four;

import org.jactr.core.chunk.IChunk;
import org.jactr.core.model.IModel;

/**
 * equation for the computation of base level activation. The Equation class of
 * objects should also implement the method static public XXXEquation
 * getInstance()
 * 
 * @author harrison
 * @created April 18, 2003
 */
public interface IBaseLevelActivationEquation {

    /**
   * compute the base level activation for the chunk c in the model model.
   * 
   * @param model
   *          Description of the Parameter
   * @param c
   *          Description of the Parameter
   * @return Description of the Return Value
   */
    public double computeBaseLevelActivation(IModel model, IChunk c);
}
