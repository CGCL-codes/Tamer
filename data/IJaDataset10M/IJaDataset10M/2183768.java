package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.edit.TextEditPart;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class NestElementCommand extends ExampleTextCommand {

    private TextRun run;

    private Container newParent;

    int index;

    private int caretOffset;

    public NestElementCommand(TextEditPart part, int caretOffset) {
        super("indent");
        this.caretOffset = caretOffset;
        run = (TextRun) part.getModel();
        index = run.getContainer().getChildren().indexOf(run);
    }

    public void execute() {
        Container container = run.getContainer();
        newParent = (Container) container.getChildren().get(index - 1);
        container.remove(run);
        run.setType(newParent.getChildType());
        newParent.add(run);
    }

    public boolean canExecute() {
        if (index == 0) return false;
        return run.getContainer().getChildren().get(index - 1) instanceof Container;
    }

    public SelectionRange getExecuteSelectionRange(GraphicalTextViewer viewer) {
        return new SelectionRange(lookupModel(viewer, run), caretOffset);
    }

    public SelectionRange getRedoSelectionRange(GraphicalTextViewer viewer) {
        return getExecuteSelectionRange(viewer);
    }

    public SelectionRange getUndoSelectionRange(GraphicalTextViewer viewer) {
        return getExecuteSelectionRange(viewer);
    }
}
