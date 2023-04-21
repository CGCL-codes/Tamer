package org.isistan.flabot.edit.editor.commands;

import org.eclipse.gef.commands.Command;
import org.isistan.flabot.edit.editormodel.Diagram;
import org.isistan.flabot.edit.editormodel.FlabotFileModel;
import org.isistan.flabot.edit.editormodel.Folder;
import org.isistan.flabot.messages.Messages;

/**
 * DeleteDiagramCommand
 * -	Deletes a diagram from the FlabotFileModel and also dessasing its folder.
 * 
 * @author $Author: franco $
 *
 */
public class DeleteDiagramCommand extends Command {

    private FlabotFileModel file;

    private Diagram diagram;

    private Folder oldFolder;

    /**
	 * Instantiates a command that can delete a diagram from FlabotFileModel.
	 * @param file the FlabotFileModel
	 * @param diagram the diagram to delete
	 */
    public DeleteDiagramCommand(FlabotFileModel file, Diagram diagram) {
        this.file = file;
        this.diagram = diagram;
        setLabel(Messages.getString("org.isistan.flabot.edit.editor.commands.DeleteDiagramCommand.label"));
    }

    /**
	 * Verifies that the command can be executed.
	 * @return <code>true</code> if the command can be executed	
	 */
    public boolean canExecute() {
        return (file != null);
    }

    /**
	 * Executes the Command. This method should not be called if the Command is not
	 * executable.
	 * 
	 *  @see redo()
	 */
    public void execute() {
        oldFolder = diagram.getFolder();
        redo();
    }

    /**
	 * Removes the diagram from the list of diagrams of the FlabotFileModel.
	 * Unsets its the folder.
	 */
    public void redo() {
        diagram.setFolder(null);
        file.getDiagrams().remove(diagram);
    }

    /**
	 * Adds the diagram to the list of diagrams of the FlabotFileModel.
	 * Sets its folder to the old one.
	 */
    public void undo() {
        diagram.setFolder(oldFolder);
        file.getDiagrams().add(diagram);
        ;
    }
}
