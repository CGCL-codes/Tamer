package com.pyrphoros.erddb.gui.windows.main.toolbar.actions;

import java.awt.event.*;
import com.pyrphoros.erddb.Designer;
import com.pyrphoros.erddb.gui.util.ParentFinder;
import com.pyrphoros.erddb.gui.windows.edit.sequence.EditSequenceDialog;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;

/**
 *
 */
public class NewSequenceAction implements ActionListener {

    /**
     *
     */
    public void actionPerformed(ActionEvent e) {
        Window parent = ParentFinder.getParentWindow((Component) e.getSource());
        EditSequenceDialog dialog = new EditSequenceDialog((Frame) parent, Designer.getResource("gui.dialog.editsequence.title.new"), true, null);
        dialog.pack();
        dialog.setVisible(true);
    }
}
