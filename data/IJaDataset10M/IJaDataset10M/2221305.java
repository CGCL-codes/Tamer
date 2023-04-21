package com.googlecode.sarasvati.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import com.googlecode.sarasvati.editor.GraphEditor;

public class CopyAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CopyAction() {
        super("Copy");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        GraphEditor.getInstance().editCopy();
    }
}
