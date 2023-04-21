package com.bluestone.action;

import com.bluestone.BaseAction;
import com.bluestone.context.IContext;
import com.bluestone.scripts.ActionScript;
import com.bluestone.util.Util;

/**
 * @author <a href="mailto:bluesotne.master@gmail.com">daniel.q</a>
 *
 */
public class ResetKeysAction extends BaseAction {

    public ResetKeysAction(ActionScript action) {
        super(action);
    }

    /**
	 * Release some special keys.
	 */
    public boolean execute(IContext context) {
        if (!super.execute(context)) {
            return false;
        }
        Util.clearPage();
        robot.mousePress(java.awt.event.KeyEvent.BUTTON3_MASK);
        robot.delay(Util.getDelayTime(Util.DELAY500));
        robot.mouseRelease(java.awt.event.KeyEvent.BUTTON3_MASK);
        robot.delay(Util.getDelayTime(Util.DELAY300));
        robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
        robot.delay(Util.getDelayTime(Util.DELAY500));
        robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
        robot.delay(Util.getDelayTime(Util.DELAY300));
        robot.keyPress(java.awt.event.KeyEvent.VK_ALT);
        robot.delay(Util.getDelayTime(Util.DELAY500));
        robot.keyRelease(java.awt.event.KeyEvent.VK_ALT);
        robot.delay(Util.getDelayTime(Util.DELAY300));
        robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        robot.delay(Util.getDelayTime(Util.DELAY500));
        robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
        robot.delay(Util.getDelayTime(Util.DELAY300));
        robot.keyPress(java.awt.event.KeyEvent.VK_WINDOWS);
        robot.delay(Util.getDelayTime(Util.DELAY500));
        robot.keyRelease(java.awt.event.KeyEvent.VK_WINDOWS);
        robot.delay(Util.getDelayTime(Util.DELAY300));
        return true;
    }
}
