package com.sshtools.common.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class ExitAction extends StandardAction {

    private SshToolsApplication application;

    private SshToolsApplicationContainer container;

    /**
* Creates a new ExitAction object.
*
* @param application
* @param container
*/
    public ExitAction(SshToolsApplication application, SshToolsApplicationContainer container) {
        this.application = application;
        this.container = container;
        putValue(Action.NAME, "Exit");
        putValue(Action.SMALL_ICON, getIcon("exit.png"));
        putValue(Action.SHORT_DESCRIPTION, "Exit");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_MASK));
        putValue(Action.LONG_DESCRIPTION, "Exit this window");
        putValue(Action.MNEMONIC_KEY, new Integer('x'));
        putValue(StandardAction.ON_MENUBAR, new Boolean(true));
        putValue(StandardAction.MENU_NAME, "File");
        putValue(StandardAction.MENU_ITEM_GROUP, new Integer(90));
        putValue(StandardAction.MENU_ITEM_WEIGHT, new Integer(90));
        putValue(StandardAction.ON_TOOLBAR, new Boolean(false));
    }

    /**
*
*
* @param evt
*/
    public void actionPerformed(ActionEvent evt) {
        application.closeContainer(container);
    }
}
