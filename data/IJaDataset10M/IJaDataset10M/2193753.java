package org.henkels.drawcode.editors.classdiagram.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.henkels.drawcode.editors.classdiagram.ClassDiagramEditor;
import org.henkels.drawcode.editors.classdiagram.model.nodes.ICDElementVisitable;
import org.henkels.drawcode.editors.classdiagram.parts.gefcommands.CDNodeDeleteCommand;

public class CDComponentEditPolicy extends org.eclipse.gef.editpolicies.ComponentEditPolicy {

    private ClassDiagramEditor editor;

    public CDComponentEditPolicy(ClassDiagramEditor editor) {
        this.editor = editor;
    }

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        System.out.println("NSComponentEditPolicy.createDeleteCommand");
        Object child = getHost().getModel();
        if (child instanceof ICDElementVisitable) {
            return new CDNodeDeleteCommand(editor, (ICDElementVisitable) child);
        }
        return super.createDeleteCommand(deleteRequest);
    }
}
