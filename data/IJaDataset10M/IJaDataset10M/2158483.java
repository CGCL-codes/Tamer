package org.fpdev.apps.cart;

import java.util.Stack;

/**
 *
 * @author demory
 */
public class CartActionHistory {

    private Cartographer cart_;

    private Editor ed_;

    private Stack<CartAction> undoStack_, redoStack_;

    public CartActionHistory(Cartographer cart) {
        cart_ = cart;
        undoStack_ = new Stack<CartAction>();
        redoStack_ = new Stack<CartAction>();
    }

    void setEditor(Editor ed) {
        ed_ = ed;
    }

    public void addAction(CartAction action) {
        undoStack_.add(action);
        redoStack_.clear();
        cart_.getGUI().updateUndoRedo(ed_);
    }

    public boolean undoActionExists() {
        return !undoStack_.isEmpty();
    }

    public boolean redoActionExists() {
        return !redoStack_.isEmpty();
    }

    public String undoActionName() {
        if (undoStack_.isEmpty()) {
            return "";
        }
        return undoStack_.peek().getName();
    }

    public String redoActionName() {
        if (redoStack_.isEmpty()) {
            return "";
        }
        return redoStack_.peek().getName();
    }

    public void undoLast() {
        if (undoStack_.isEmpty()) {
            cart_.getGUI().msg("No actions to undo!");
            return;
        }
        CartAction action = undoStack_.peek();
        try {
            action.undoAction(cart_);
        } catch (UnsupportedOperationException uoe) {
            cart_.getGUI().msg("Last action cannot be undone");
            return;
        }
        redoStack_.add(undoStack_.pop());
        cart_.getGUI().updateUndoRedo(ed_);
    }

    public void redoLast() {
        if (redoStack_.isEmpty()) {
            cart_.getGUI().msg("No actions to redo!");
            return;
        }
        CartAction action = redoStack_.peek();
        try {
            action.doAction(cart_);
        } catch (UnsupportedOperationException uoe) {
            cart_.getGUI().msg("Last action cannot be redone");
            return;
        }
        undoStack_.add(redoStack_.pop());
        cart_.getGUI().updateUndoRedo(ed_);
    }
}
