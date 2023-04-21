package visu.handball.moves.controller;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import visu.handball.moves.model.HandballModel;
import visu.handball.moves.model.HandballModelListener;
import visu.handball.moves.views.CommentView;

public class CommentController implements DocumentListener, HandballModelListener {

    private CommentView view;

    private HandballModel model;

    public CommentController(HandballModel model, CommentView view) {
        this.view = view;
        this.model = model;
        view.getEditorPane().getDocument().addDocumentListener(this);
        model.addListener(this);
    }

    public void changedUpdate(DocumentEvent e) {
        updateComment();
    }

    public void insertUpdate(DocumentEvent e) {
        updateComment();
    }

    public void removeUpdate(DocumentEvent e) {
        updateComment();
    }

    private void updateComment() {
        model.setComment(view.getCommentText());
    }

    public void modelChanged() {
        if (model != null && view != null) {
            if (!model.getComment().equals(view.getCommentText())) {
                view.setCommentText(model.getComment());
            }
        }
    }
}
