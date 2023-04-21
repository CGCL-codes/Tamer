package net.sf.asyncobjects.util.timer;

import net.sf.asyncobjects.AsyncObject;
import net.sf.asyncobjects.util.callbacks.ACallable;

/**
 * A base interface for timer task. It contains accpepted method that allows
 * cancelling timer tasks.
 * 
 * @author const
 */
public interface ATimerTaskBase extends AsyncObject {

    /**
	 * This method is called when task were accepted to timer
	 * 
	 * @param cancelAction
	 */
    void accepted(ACallable<Boolean> cancelAction);
}
