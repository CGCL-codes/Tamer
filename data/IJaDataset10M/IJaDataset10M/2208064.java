package org.nomadpim.core.util.threading;

import org.eclipse.core.runtime.ISafeRunnable;

public interface IExecutor {

    void execute(ISafeRunnable code);
}
