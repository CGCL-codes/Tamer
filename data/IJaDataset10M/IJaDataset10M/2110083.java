package org.activebpel.rt.bpel.impl.activity.assign;

import org.activebpel.rt.bpel.impl.AeBpelException;

/**
 * An interface for copy operation strategy factories.
 */
public interface IAeCopyStrategyFactory {

    /**
    * Called to create a strategy.
    * 
    * @param aCopyOperation
    * @param aFromData
    * @param aToData
    * @throws AeBpelException
    */
    public IAeCopyStrategy createStrategy(IAeCopyOperation aCopyOperation, Object aFromData, Object aToData) throws AeBpelException;
}
