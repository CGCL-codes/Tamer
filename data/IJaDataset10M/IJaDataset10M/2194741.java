package com.certesystems.swingforms.actions;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import com.certesystems.swingforms.Action;
import com.certesystems.swingforms.entity.Entity;
import com.certesystems.swingforms.tools.Messages;
import com.certesystems.swingforms.tools.SwingHelper;

/**
 * Form Window Close action. 
 * @author mludeiro
 *
 */
public class ActionClose extends ActionImpl<JComponent> {

    /**
	 * Instantiates a new action close.
	 */
    public ActionClose() {
        super(Messages.getString("ActionClose.text"), "images/shutdown.png", KeyEvent.VK_ESCAPE);
    }

    public void doAction(JComponent form) throws Exception {
        SwingHelper.closeParentFrame(form);
    }

    public boolean isAllowed(JComponent source) {
        return true;
    }

    public int getSecurityLevel(JComponent source) {
        return Action.LEVEL_NONE;
    }

    public Entity getEntity(JComponent source) {
        return null;
    }

    public int getActionGroup() {
        return Action.GROUP_WINDOW;
    }
}
