package com.solidmatrix.regxmaker.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.InputFrame
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: Input frame
 *
 * Copyright (C) 2001, 2004 SolidMatrix Technologies, Inc.
 * This file is part of RegXmaker GUI utility.
 *
 * RegXmaker GUI Utility is is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RegXmaker GUI Utility; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Modification History
 *
 * 07-05-2004	YS Added header and licensing information
 * </PRE>
 */
public class InputFrame extends MyFrame {

    private JTextArea preview;

    private DefaultListModel listmodel;

    private JButton addFileButton;

    private boolean added;

    public InputFrame(RegXMakerGui gui) {
        super("Input", gui);
        added = false;
        parent = gui.getDesktopPane();
        listmodel = gui.getListModel();
        MyListener my = new MyListener();
        preview = new JTextArea();
        preview.addKeyListener(my);
        preview.getDocument().addDocumentListener(my);
        preview.setWrapStyleWord(true);
        JScrollPane previewScroller = new JScrollPane(preview);
        getContentPane().add(previewScroller);
        JToolBar buttonFrameCont = new JToolBar();
        addFileButton = new JButton();
        addFileButton.setEnabled(false);
        buttonFrameCont.add(addFileButton);
        addFileButton.setIcon(new ImageIcon(RegXMakerGui.ICON_DIR + "add-string.gif"));
        addFileButton.setToolTipText("Add string to the list");
        addFileButton.addActionListener(my);
        getContentPane().add(buttonFrameCont, BorderLayout.NORTH);
        myWindow = gui.addFrameToList(getTitle());
    }

    private boolean addString() {
        if (preview.getText().length() > 0) {
            listmodel.addElement(new stringP(preview.getText()));
        } else return true;
        return true;
    }

    protected void processClose() {
        if (preview.getText().length() > 0) {
            int x = JOptionPane.showConfirmDialog(this, "Save String?", "Confirm Close:", JOptionPane.YES_NO_CANCEL_OPTION);
            if ((x == JOptionPane.YES_OPTION && !addString()) || (x == JOptionPane.CANCEL_OPTION || x == JOptionPane.CLOSED_OPTION)) return;
        }
        stdClose();
    }

    public JTextArea getTextArea() {
        return preview;
    }

    private class MyListener extends MainListener implements ActionListener {

        protected void processCtrlS() {
            if (addString()) stdClose();
        }

        protected void processTextChange() {
            addFileButton.setEnabled(preview.getText().length() > 0);
        }

        public void actionPerformed(ActionEvent e) {
            processCtrlS();
        }
    }
}
