package com.objectwave.tools.uiWidget;

import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.*;
import com.objectwave.utility.FileList;
import com.objectwave.uiWidget.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provide a list of class
 */
public class SelectData extends javax.swing.JDialog {

    JPanel componentList2;

    JCheckBox[] items;

    JTextField tfOthers;

    FileList list;

    /**
	 */
    public SelectData(Frame parent, String title, boolean modal) {
        this(parent, modal);
        setTitle(title);
    }

    /**
	 */
    public SelectData(Frame w, boolean modal) {
        super(w, modal);
        getContentPane().setLayout(new BorderLayout());
        setBounds(100, 100, 430, 270);
        setTitle("SelectData");
        componentList2 = new JPanel();
        componentList2.setLayout(new java.awt.GridLayout());
        JScrollPane selection = new JScrollPane(componentList2);
        selection.setMinimumSize(new java.awt.Dimension(10, 40));
        getContentPane().add(selection);
        getContentPane().add("South", getButtonPanel());
        tfOthers = new JTextField();
        getContentPane().add("North", tfOthers);
        AbstractAction anAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                enterKeyPressed();
            }
        };
        tfOthers.addActionListener(anAction);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
	*/
    void close() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
	 * Add the file name to the list of file choices.
	 * Update the display.
	 */
    void enterKeyPressed() {
        list.addFileName(tfOthers.getText());
        setFileList(list);
    }

    /**
	 * Return a button panel containing an OK and Cancel button.
	 */
    public JPanel getButtonPanel() {
        JPanel panel = new JPanel();
        JButton b = new JButton("Ok");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pbOkClicked();
            }
        });
        panel.add(b);
        b = new JButton("Cancel");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pbCancelClicked();
            }
        });
        panel.add(b);
        return panel;
    }

    /**
	 * Deselect every selected item and close the frame.
	 */
    void pbCancelClicked() {
        if (items != null) {
            for (int i = 0; i < items.length; ++i) {
                JCheckBox box = items[i];
                box.setSelected(false);
            }
        }
        close();
    }

    /**
	 * Just close the dialog.
	 */
    void pbOkClicked() {
        close();
    }

    /**
	 * Update the list of possible data choices.
	 * @param FileList The list of options.
	 */
    public void setFileList(FileList list) {
        this.list = list;
        items = list.getCheckBoxList();
        componentList2.removeAll();
        if (items.length > 0) {
            ((java.awt.GridLayout) componentList2.getLayout()).setRows(items.length);
            for (int i = 0; i < items.length; ++i) {
                componentList2.add(items[i]);
            }
            componentList2.revalidate();
        }
    }
}
