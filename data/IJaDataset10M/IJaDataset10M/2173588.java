package org.cubictest.ui.gef.command;

import org.cubictest.model.AbstractPage;
import org.eclipse.gef.commands.Command;

/**
 * @author Stein K�re Skytteren
 * A command that changes an <code>Common</code>'s name.
 */
public class ChangeAbstractPageNameCommand extends Command {

    private String name;

    private String oldName;

    private AbstractPage page;

    /**
	 * @param page
	 */
    public void setAbstractPage(AbstractPage page) {
        this.page = page;
    }

    /**
	 * @param name
	 */
    public void setOldName(String name) {
        this.oldName = name;
    }

    /**
	 * @param string
	 */
    public void setName(String name) {
        this.name = name;
    }

    public void execute() {
        page.setName(name);
    }

    public void undo() {
        page.setName(oldName);
    }
}
