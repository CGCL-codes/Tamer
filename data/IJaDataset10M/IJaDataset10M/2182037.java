package dawnland03.model.action;

import org.apache.log4j.Logger;

/**
 * This class will do nothing. Its scope is to be scheduled and to wait for the duration of the action. At the end,
 *  it will signal the action's end. 
 *
 * User: Petru Obreja (obrejap@yahoo.com)
 * Date: Jan 19, 2010
 * Time: 10:11:33 PM
 */
public class ActionRunnable implements Runnable {

    private ActionImpl action;

    private static Logger logger = Logger.getLogger(ActionRunnable.class.getName());

    ActionRunnable(ActionImpl action) {
        this.action = action;
    }

    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug(action + " finished.");
        }
        action.endAction();
    }
}
