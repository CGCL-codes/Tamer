package ru.amse.soultakov.ereditor.controller.undo.commands;

import ru.amse.soultakov.ereditor.controller.DiagramEditor;
import ru.amse.soultakov.ereditor.controller.undo.ICommand;
import ru.amse.soultakov.ereditor.view.EntityView;

public class EditEntityNameCommand implements ICommand {

    private final DiagramEditor diagramEditor;

    private final EntityView entityView;

    private final String oldName;

    private final String newName;

    public EditEntityNameCommand(DiagramEditor diagramEditor, EntityView entityView, String newName) {
        this.diagramEditor = diagramEditor;
        this.entityView = entityView;
        this.oldName = this.entityView.getEntity().getName();
        this.newName = newName;
    }

    public void doIt() {
        entityView.getEntity().setName(newName);
        diagramEditor.repaint();
    }

    public void undoIt() {
        entityView.getEntity().setName(oldName);
        diagramEditor.repaint();
    }
}
