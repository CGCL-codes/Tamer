package org.jhotdraw.draw.action;

import java.util.Collection;
import java.util.LinkedList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.CompositeFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.undo.CompositeEdit;

/**
 * GroupAction.
 *
 * @author  Werner Randelshofer
 * @version 1.1 2006-07-12 Changed to support any CompositeFigure.
 * <br>1.0.1 2006-07-09 Fixed enabled state.
 * <br>1.0 24. November 2003  Created.
 */
public class GroupAction extends AbstractSelectedAction {

    public static final String ID = "selectionGroup";

    private CompositeFigure prototype;

    /** Creates a new instance. */
    public GroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype) {
        super(editor);
        this.prototype = prototype;
        labels.configureAction(this, ID);
    }

    @Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(canGroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canGroup() {
        return getView().getSelectionCount() > 1;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (canGroup()) {
            final DrawingView view = getView();
            final LinkedList<Figure> ungroupedFigures = new LinkedList<Figure>(view.getSelectedFigures());
            final CompositeFigure group = (CompositeFigure) prototype.clone();
            CompositeEdit edit = new CompositeEdit(labels.getString("selectionGroup")) {

                public void redo() throws CannotRedoException {
                    super.redo();
                    groupFigures(view, group, ungroupedFigures);
                }

                public void undo() throws CannotUndoException {
                    ungroupFigures(view, group);
                    super.undo();
                }

                public boolean addEdit(UndoableEdit anEdit) {
                    return super.addEdit(anEdit);
                }
            };
            fireUndoableEditHappened(edit);
            groupFigures(view, group, ungroupedFigures);
            fireUndoableEditHappened(edit);
        }
    }

    public Collection<Figure> ungroupFigures(DrawingView view, CompositeFigure group) {
        LinkedList<Figure> figures = new LinkedList<Figure>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().getFigureCount(), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }

    public void groupFigures(DrawingView view, CompositeFigure group, Collection<Figure> figures) {
        Collection<Figure> sorted = view.getDrawing().sort(figures);
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(group);
        group.willChange();
        for (Figure f : sorted) {
            group.basicAdd(f);
        }
        group.changed();
        view.addToSelection(group);
    }
}
