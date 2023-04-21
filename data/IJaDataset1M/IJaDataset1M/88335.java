package org.dengues.designer.ui.process.policies;

import java.util.ArrayList;
import java.util.List;
import org.dengues.designer.ui.process.commands.CompNoteDeleteCommand;
import org.dengues.designer.ui.process.models.CompNote;
import org.dengues.designer.ui.process.models.CompProcess;
import org.dengues.designer.ui.process.parts.CompNotePart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public class CompNoteEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        Object parent = getHost().getParent().getModel();
        if (!(parent instanceof CompProcess)) {
            return null;
        }
        List<CompNote> notes = new ArrayList<CompNote>();
        for (int i = 0; i < deleteRequest.getEditParts().size(); i++) {
            if (deleteRequest.getEditParts().get(i) instanceof CompNotePart) {
                notes.add((CompNote) ((CompNotePart) deleteRequest.getEditParts().get(i)).getModel());
            }
        }
        return new CompNoteDeleteCommand(notes, (CompProcess) parent);
    }
}
