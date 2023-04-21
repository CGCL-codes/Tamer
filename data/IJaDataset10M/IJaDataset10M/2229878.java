package org.isistan.flabot.edit.editor.commands.paste;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.isistan.flabot.coremodel.CoremodelFactory;
import org.isistan.flabot.edit.componenteditor.commands.visual.AddInterfaceConnectionCommand;
import org.isistan.flabot.edit.componentmodel.ComponentDiagram;
import org.isistan.flabot.edit.editormodel.ConnectionVisualModel;
import org.isistan.flabot.edit.editormodel.EditormodelFactory;
import org.isistan.flabot.edit.editormodel.NodeVisualModel;
import org.isistan.flabot.edit.editormodel.VisualModel;
import org.isistan.flabot.messages.Messages;

/**
 * @author $Author: franco $
 *
 */
public class AddInterfaceConnectionPasteCommand extends AddInterfaceConnectionCommand implements RetargetConnectionPasteCommand {

    private RetargetParentPasteCommand commandSource;

    private RetargetParentPasteCommand commandTarget;

    private ConnectionVisualModel copyConnectionModel;

    public AddInterfaceConnectionPasteCommand(ConnectionVisualModel copyConnectionModel) {
        super(null, null, null);
        this.copyConnectionModel = copyConnectionModel;
        setLabel(Messages.getString("org.isistan.flabot.edit.editor.commands.paste.AddInterfaceConnectionPasteCommand.label"));
    }

    AddInterfaceConnectionPasteCommand(ConnectionVisualModel copyConnectionModel, RetargetParentPasteCommand commandSource, RetargetParentPasteCommand commandTarget, ComponentDiagram diagram) {
        this(copyConnectionModel);
        this.commandSource = commandSource;
        this.commandTarget = commandTarget;
        this.diagram = diagram;
    }

    public void setParent(Object object) {
        diagram = (ComponentDiagram) object;
    }

    public boolean isValidParent(Object object) {
        return (object instanceof ComponentDiagram);
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

    public void execute() {
        source = (NodeVisualModel) commandSource.getNewVisualModel();
        target = (NodeVisualModel) commandTarget.getNewVisualModel();
        connection = EditormodelFactory.eINSTANCE.createConnectionVisualModel(copyConnectionModel);
        connection.setSemanticModel(CoremodelFactory.eINSTANCE.createInterfaceLink());
        super.execute();
    }

    public Command clone() {
        return new AddInterfaceConnectionPasteCommand(copyConnectionModel, commandSource, commandTarget, diagram);
    }

    public void addConnectionDependantCommand(boolean source, RetargetConnectionPasteCommand command) {
    }

    public List getDependantCommands() {
        return new ArrayList();
    }
}
