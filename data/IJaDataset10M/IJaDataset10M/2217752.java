package de.flingelli.util.gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextPane;
import de.flingelli.scrum.datastructure.Product;
import de.flingelli.scrum.undo.UndoListener;

public class NavigatableComponentKeyListener implements KeyListener {

    private Product mProduct = null;

    private Component mComponent = null;

    private int mPreviousKey = -Integer.MAX_VALUE;

    public NavigatableComponentKeyListener(Component component) {
        mComponent = component;
    }

    public NavigatableComponentKeyListener(Component component, Product product) {
        mComponent = component;
        mProduct = product;
    }

    public void keyTyped(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
    }

    public void keyPressed(KeyEvent event) {
        if (mComponent != null) {
            if (mComponent instanceof JTextPane) {
                if ((mPreviousKey == KeyEvent.VK_SHIFT || event.isControlDown()) && (event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_TAB)) {
                    mComponent.transferFocus();
                }
            } else {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    mComponent.transferFocus();
                }
            }
        }
        if (mProduct != null) {
            if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Z) {
                UndoListener.getInstance(mProduct).undo();
            } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Y) {
                UndoListener.getInstance(mProduct).redo();
            }
        }
        mPreviousKey = event.getKeyCode();
    }
}
