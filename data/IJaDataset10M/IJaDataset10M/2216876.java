package org.ouatodo.model.state;

import org.apache.log4j.Logger;

public abstract class AbstractTaskState implements TaskState {

    protected Logger log;

    /**
	 * constructeur privé. (Singleton)
	 */
    protected AbstractTaskState() {
        super();
        Logger.getLogger(AbstractTaskState.class);
    }
}
