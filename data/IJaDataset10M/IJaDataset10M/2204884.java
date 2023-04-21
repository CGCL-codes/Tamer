package com.phloc.commons.state;

/**
 * Very simple interface for an object that has success/failure indication.
 * 
 * @author philip
 */
public interface ISuccessIndicator {

    /**
   * @return <code>true</code> on success and <code>false</code> on failure.
   */
    boolean isSuccess();

    /**
   * @return <code>true</code> on failure and <code>false</code> on success.
   */
    boolean isFailure();
}
