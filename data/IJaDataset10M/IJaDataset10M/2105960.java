package com.novasurv.turtle.frontend.swing.comments;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import com.novasurv.turtle.frontend.swing.data.SwingDataManager;

/**
 * Currently not in use; editing a comment is done through a separate dialog.
 * <p/>
 * Handles editing of comments directly in the comment table, making sure to update
 * the comment in the data manager.
 *
 * @author Jason Dobies
 */
public class CommentCellEditor extends DefaultCellEditor implements CellEditorListener {

    private SwingDataManager dataManager;

    private CommentsTable parent;

    public CommentCellEditor(SwingDataManager dataManager, CommentsTable parent) {
        super(new JTextField());
        JTextField editorField = (JTextField) this.getComponent();
        this.dataManager = dataManager;
        this.parent = parent;
        super.setClickCountToStart(2);
        super.addCellEditorListener(this);
    }

    public void editingStopped(ChangeEvent e) {
        Object editedValue = super.getCellEditorValue();
        int commentItemIndex = parent.getSelectedRow();
        String newComment = (String) editedValue;
        dataManager.updateCommentAt(newComment, commentItemIndex);
    }

    public void editingCanceled(ChangeEvent e) {
    }
}
