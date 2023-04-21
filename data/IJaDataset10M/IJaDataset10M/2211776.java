package net.sf.openks.strategy;

import net.sf.openks.engine.InferenceEngine;
import net.sf.openks.engine.State;
import net.sf.openks.engine.Treenode;

/**
 * @author Alle Veenstra, alle.veenstra@gmail.com
 *
 */
public interface InferenceStrategy {

    public abstract Treenode findBest(InferenceEngine inferenceEngine, State currentState) throws Exception;
}
