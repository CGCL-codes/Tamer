package org.eclipse.genforms.editor.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.genforms.editor.model.Form;
import org.eclipse.genforms.editor.model.Formelement;

/**
 * @author Steffen Gruen steffen.gruen (at) web.de
 *
 */
public class FormelementCreateCommand extends Command {

    private final Formelement formelement;

    private final Form form;

    int x;

    int y;

    /**
	 * 
	 */
    public FormelementCreateCommand(Form form, Formelement formelement, int x, int y) {
        super("Create Formelement");
        this.formelement = formelement;
        this.form = form;
        this.x = x;
        this.y = y;
    }

    public void execute() {
        formelement.setName("Name im Properties-View �ndern");
        formelement.setPositionX(x);
        formelement.setPositionY(y);
        redo();
    }

    public void redo() {
        form.addFormelement(formelement);
    }

    public void undo() {
        form.removeFormelement(formelement);
    }
}
