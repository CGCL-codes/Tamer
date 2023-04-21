package org.isistan.flabot.edit.componenteditor.commands.visual;

import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.isistan.flabot.coremodel.ComponentModel;
import org.isistan.flabot.coremodel.CoremodelFactory;
import org.isistan.flabot.coremodel.PortModel;
import org.isistan.flabot.edit.componenteditor.figures.PortFigure;
import org.isistan.flabot.edit.editormodel.Util;
import org.isistan.flabot.edit.editormodel.VisualModel;
import org.isistan.flabot.messages.Messages;

/**
 * AddPortCommand
 * -	Adds a port model to core model.
 * -	Adds a visual model to the diagram and assigns its semantic model to the port model.
 * 
 * @author $Author: dacostae $
 *
 */
public class AddPortCommand extends Command {

    protected VisualModel parent;

    protected VisualModel visualModel;

    protected PortModel portModel;

    protected Point location;

    private boolean addedToCore;

    /**
	 *	Instantiates a command that can add a port to a component.
	 * @param visualModel the visual model of the port that will be added
	 * @param parent the visual model of the component where the port will be added  
	 * @param location the location of the port in the component (left side or right side)
	 */
    public AddPortCommand(VisualModel visualModel, VisualModel parent, Point location) {
        this.visualModel = visualModel;
        this.location = location;
        this.parent = parent;
        setLabel(Messages.getString("org.isistan.flabot.edit.componenteditor.commands.AddPortCommand.label"));
    }

    /**
	 * The command can be undone if the component contains the visual model of the port.
	 * @return <code>true</code> if the command can be undone
	 */
    public boolean canUndo() {
        return parent.getChildren().contains(visualModel);
    }

    /**
	 * Verifies that the command can be executed.
	 * The port must not be duplicated.
	 * @return <code>true</code> if the command can be executed	
	 */
    public boolean canExecute() {
        boolean valid = (parent != null && location != null && visualModel != null && parent.getDiagram() != null);
        if (visualModel != null && visualModel.getSemanticModel() != null) {
            PortModel port = (PortModel) visualModel.getSemanticModel();
            return (!isDuplicated(port) && valid);
        }
        return valid;
    }

    /**
	 * Verifies that the port does not exist in the component.
	 * @param inter the interface to check for duplication
	 */
    private boolean isDuplicated(PortModel port) {
        for (Iterator iter = parent.getChildren().iterator(); iter.hasNext(); ) {
            VisualModel visual = (VisualModel) iter.next();
            if (visual.getSemanticModel() == port) {
                MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(), Messages.getString("org.isistan.flabot.edit.componenteditor.commands.AddPortCommand.label"), null, Messages.getString("org.isistan.flabot.edit.componenteditor.commands.AddPortCommand.portAlreadyInComponent"), MessageDialog.ERROR, new String[] { Messages.getString("org.isistan.flabot.edit.editor.okButton") }, 0);
                dlg.open();
                return true;
            }
        }
        return false;
    }

    /**
	 * Executes the Command. This method should not be called if the command is not
	 * executable.
	 * 
	 * @see redo()
	 */
    public void execute() {
        portModel = (PortModel) visualModel.getSemanticModel();
        if (portModel == null) portModel = CoremodelFactory.eINSTANCE.createPortModel();
        updateBounds();
        redo();
    }

    /**
	 * Sets the correct location and size of the port in the component according to the other existing ports.
	 */
    protected void updateBounds() {
        visualModel.setSize(Util.getDimension(PortFigure.defaultsize));
        boolean leftSide = true;
        int middle = parent.getLocation().getX() + parent.getSize().getWidth() / 2;
        if (location.x > middle) leftSide = false;
        int leftOffset = 0;
        int rightOffset = parent.getSize().getWidth() - visualModel.getSize().getWidth();
        List children = parent.getChildren();
        int verticalOffset = 5;
        for (int i = 0; i < children.size(); i++) {
            VisualModel m = (VisualModel) children.get(i);
            if ((m.getLocation().getX() == leftOffset && leftSide) || (m.getLocation().getX() == rightOffset && !leftSide)) verticalOffset = m.getLocation().getY() + m.getSize().getHeight() + 5;
        }
        int horizontalOffset = (leftSide) ? leftOffset : rightOffset;
        visualModel.setLocation(Util.getPoint(horizontalOffset, verticalOffset));
    }

    /**
	 * Sets the component of the semantic port.
	 * Adds the visual port to the visual component.
	 */
    public void redo() {
        addedToCore = (portModel.getComponent() == null);
        portModel.setComponent((ComponentModel) parent.getSemanticModel());
        parent.getChildren().add(visualModel);
    }

    /**
	 * Unsets the component of the semantic port.
	 * Removes the visual port from the visual component.
	 */
    public void undo() {
        parent.getChildren().remove(visualModel);
        if (addedToCore) portModel.setComponent(null);
    }
}
