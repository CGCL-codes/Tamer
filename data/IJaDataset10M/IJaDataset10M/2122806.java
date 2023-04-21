package org.dengues.designer.ui.database.policies;

import java.util.ArrayList;
import java.util.List;
import org.dengues.designer.ui.database.commands.DBNoteDeleteCommand;
import org.dengues.designer.ui.database.parts.DBNoteEditPart;
import org.dengues.model.database.DBNote;
import org.dengues.model.database.DatabaseDiagram;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public class DBNoteEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        Object parent = getHost().getParent().getModel();
        if (!(parent instanceof DatabaseDiagram)) {
            return null;
        }
        List<DBNote> notes = new ArrayList<DBNote>();
        for (int i = 0; i < deleteRequest.getEditParts().size(); i++) {
            if (deleteRequest.getEditParts().get(i) instanceof DBNoteEditPart) {
                notes.add((DBNote) ((DBNoteEditPart) deleteRequest.getEditParts().get(i)).getModel());
            }
        }
        return new DBNoteDeleteCommand(notes, (DatabaseDiagram) parent);
    }
}
