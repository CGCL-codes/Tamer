package org.aspentools.dormouse.vfs.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.Icon;
import org.jdesktop.swingx.action.AbstractActionExt;

/**
 * @author Mark
 *
 */
public class EmailFileAction extends AbstractActionExt {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5806730458324131713L;

    /**
	 * @param action
	 */
    public EmailFileAction(AbstractActionExt action) {
        super(action);
    }

    /**
	 * @param name
	 */
    public EmailFileAction() {
        super("Send Files As Email");
    }

    /**
	 * @param name
	 * @param icon
	 */
    public EmailFileAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
	 * @param name
	 * @param command
	 */
    public EmailFileAction(String name, String command) {
        super(name, command);
    }

    /**
	 * @param name
	 * @param command
	 * @param icon
	 */
    public EmailFileAction(String name, String command, Icon icon) {
        super(name, command, icon);
    }

    public void itemStateChanged(ItemEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
