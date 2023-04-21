package org.nakedobjects.runtime.transaction;

/**
 * Convenience adapter providing no-op implementations of {@link #onSuccess()} and 
 * {@link #onFailure()}.
 */
public abstract class TransactionalClosureAbstract implements TransactionalClosure {

    /**
	 * No-op implementation; does nothing.
	 */
    public void preExecute() {
    }

    /**
	 * No-op implementation; does nothing.
	 */
    public void onSuccess() {
    }

    /**
	 * No-op implementation; does nothing.
	 */
    public void onFailure() {
    }
}
