package org.nomadpim.core.util.threading;

import org.eclipse.core.runtime.ISafeRunnable;

public final class DirectExecutor implements IExecutor {

    public void execute(ISafeRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            runnable.handleException(t);
        }
    }
}
