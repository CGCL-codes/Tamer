package uk.ac.bolton.archimate.editor.model.commands;

import org.eclipse.gef.commands.Command;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;

/**
 * Delete Archimate Element Command
 * 
 * @author Phillip Beauvoir
 */
public class DeleteElementCommand extends Command {

    private INameable fElement;

    private int fIndex;

    private IFolder fFolder;

    public DeleteElementCommand(INameable element) {
        fFolder = (IFolder) element.eContainer();
        fElement = element;
        setLabel(Messages.DeleteElementCommand_0 + " " + ArchimateLabelProvider.INSTANCE.getLabel(fElement));
    }

    @Override
    public void execute() {
        fIndex = fFolder.getElements().indexOf(fElement);
        if (fIndex != -1) {
            fFolder.getElements().remove(fElement);
        }
    }

    @Override
    public void undo() {
        if (fIndex != -1) {
            fFolder.getElements().add(fIndex, fElement);
        }
    }

    @Override
    public void dispose() {
        fElement = null;
        fFolder = null;
    }
}
