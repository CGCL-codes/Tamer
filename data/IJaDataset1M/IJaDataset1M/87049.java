package org.isistan.flabot.edit.ucmeditor.commands.model;

import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.commands.CompoundCommand;
import org.isistan.flabot.coremodel.ComponentRole;
import org.isistan.flabot.coremodel.CoreModel;
import org.isistan.flabot.coremodel.Family;
import org.isistan.flabot.coremodel.FamilyElement;
import org.isistan.flabot.coremodel.UseCaseMap;
import org.isistan.flabot.messages.Messages;

/**
 * @author $Author: franco $
 *
 */
public class DeleteComponentRoleCommand extends CompoundCommand {

    private CoreModel coreModel;

    private ComponentRole componentRole;

    private UseCaseMap map;

    public DeleteComponentRoleCommand(CoreModel coreModel, ComponentRole componentRole) {
        this.coreModel = coreModel;
        this.componentRole = componentRole;
        this.map = componentRole.getMap();
        setLabel(Messages.getString("org.isistan.flabot.edit.ucmeditor.commands.model.DeleteComponentRoleCommand.label"));
    }

    public boolean canExecute() {
        return (map != null);
    }

    public boolean canUndo() {
        return true;
    }

    public void execute() {
        List families = coreModel.getFamilies();
        for (Iterator iter = families.iterator(); iter.hasNext(); ) {
            Family f = (Family) iter.next();
            for (Iterator iterFes = f.getFamilyElement().iterator(); iterFes.hasNext(); ) {
                FamilyElement fe = (FamilyElement) iterFes.next();
                if (fe.getArchitecturalComponent() == componentRole || fe.getFunctionalComponent() == componentRole) add(new DeleteFamilyElementCommand(f, fe));
            }
        }
        super.execute();
        doDeleteComponentRole();
    }

    public void redo() {
        doDeleteComponentRole();
        super.redo();
    }

    public void undo() {
        super.undo();
        undoDeleteComponentRole();
    }

    public void doDeleteComponentRole() {
        map.getComponentRoles().remove(componentRole);
    }

    public void undoDeleteComponentRole() {
        map.getComponentRoles().add(componentRole);
    }
}
