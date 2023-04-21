package org.isistan.flabot.edit.editor.commands.paste;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.isistan.flabot.edit.editor.commands.AddNoteConnectionCommand;
import org.isistan.flabot.edit.editormodel.ConnectionVisualModel;
import org.isistan.flabot.edit.editormodel.EditormodelFactory;
import org.isistan.flabot.edit.editormodel.NodeVisualModel;
import org.isistan.flabot.edit.editormodel.VisualModel;
import org.isistan.flabot.messages.Messages;

/**
 * @author $Author: franco $
 *
 */
public class AddNoteConnectionPasteCommand extends AddNoteConnectionCommand implements RetargetConnectionPasteCommand {

    private RetargetParentPasteCommand commandSource;

    private RetargetParentPasteCommand commandTarget;

    protected ConnectionVisualModel copyConnectionModel = null;

    protected boolean canUndo = false;

    public AddNoteConnectionPasteCommand(ConnectionVisualModel copyConnectionModel) {
        super(null, null);
        this.copyConnectionModel = copyConnectionModel;
        setLabel(Messages.getString("org.isistan.flabot.edit.editor.commands.paste.AddNoteConnectionPasteCommand.label"));
    }

    public AddNoteConnectionPasteCommand(ConnectionVisualModel copyConnectionModel, RetargetParentPasteCommand commandSource, RetargetParentPasteCommand commandTarget) {
        this(copyConnectionModel);
        this.commandSource = commandSource;
        this.commandTarget = commandTarget;
    }

    public void setParent(Object object) {
    }

    public boolean isValidParent(Object object) {
        return true;
    }

    public VisualModel getNewVisualModel() {
        return connection;
    }

    public VisualModel getCopyVisualModel() {
        return copyConnectionModel;
    }

    public void setCommandSource(RetargetParentPasteCommand commandSource) {
        this.commandSource = commandSource;
    }

    public void setCommandTarget(RetargetParentPasteCommand commandTarget) {
        this.commandTarget = commandTarget;
    }

    public boolean canExecute() {
        return true;
    }

    public boolean canUndo() {
        return canUndo;
    }

    public void execute() {
        source = (NodeVisualModel) commandSource.getNewVisualModel();
        target = (NodeVisualModel) commandTarget.getNewVisualModel();
        if (target != null && source != null) {
            canUndo = true;
            connection = EditormodelFactory.eINSTANCE.createConnectionVisualModel(copyConnectionModel);
            super.execute();
        }
    }

    public Command clone() {
        return new AddNoteConnectionPasteCommand(copyConnectionModel, commandSource, commandTarget);
    }

    public void addConnectionDependantCommand(boolean source, RetargetConnectionPasteCommand command) {
    }

    public List getDependantCommands() {
        return new ArrayList();
    }
}
