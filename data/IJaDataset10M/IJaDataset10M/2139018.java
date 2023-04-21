package javadata.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * <p>
 * <b>Title: </b>Customisable OK/Cancel dialog class.
 * </p>
 *
 * <p>
 * <b>Description: </b>Customisable OK/Cancel dialog class. The user can define 
 * the title, the number of editable text fields, the labels to place
 * before those fields, and the size in characters of those fields. 
 * There is also an OK and Cancel button.
 * The modal is also configurable, giving the option to make the dialog 
 * box the only active window in the application or not.
 * </p>
 *
 * <p><b>Version: </b>1.0</p>
 * 
 * <p>
 * <b>Author: </b> Matthew Pearson, Copyright 2006, 2007
 * </p>
 * 
 * <p>
 * <b>License: </b>This file is part of JavaData.
 * </p>
 * <p>
 * JavaData is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * </p>
 * <p>
 * JavaData is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with JavaData.  If not, see 
 * <a href="http://www.gnu.org/licenses/">GNU licenses</a>.
 * </p> 
 *
 */
public class CustomDialog extends JDialog implements ActionListener {

    /**
	 * Show the dialog box.
	 * @param parentFrame The parent <code>JFrame</code> of this dialog box.
	 * @param modal Set to <code>true</code> if no other window is to be 
	 * active while the dialog is open, set to <code>false</code> otherwise.
	 * @param title The title of the dialog box.
	 * @param fieldNames The strings to place before the editable text fields. This
	 * also defines the number of text fields.
	 * @param fieldSize The width of the text fields (in characters).
	 * @return A vector of strings containing the information that the user entered
	 * before pressing OK. 
	 */
    public static Vector<String> showDialog(JFrame parentFrame, boolean modal, String title, Vector<String> fieldNames, int fieldSize) {
        new CustomDialog(parentFrame, modal, title, fieldNames, fieldSize);
        return mInput;
    }

    /**
	 * Constructor.
	 * @param parentFrame The parent <code>JFrame</code> of this dialog box.
	 * @param modal Set to <code>true</code> if no other window is to be 
	 * active while the dialog is open, set to <code>false</code> otherwise.
	 * @param title The title of the dialog box.
	 * @param fieldNames The strings to place before the editable text fields. This
	 * also defines the number of text fields.
	 * @param fieldSize The width of the text fields (in characters).
	 */
    private CustomDialog(JFrame parentFrame, boolean modal, String title, Vector<String> fieldNames, int fieldSize) {
        super(parentFrame, title, modal);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.mOKButton = new JButton(this.mOK);
        this.mOKButton.addActionListener(new OKAction());
        this.getRootPane().setDefaultButton(this.mOKButton);
        this.mOKButton.setMnemonic(java.awt.event.KeyEvent.VK_O);
        this.mCancelButton = new JButton(this.mCancel);
        this.mCancelButton.addActionListener(new CancelAction());
        this.mCancelButton.setMnemonic(java.awt.event.KeyEvent.VK_C);
        for (String name : fieldNames) {
            JLabel label = new JLabel(name);
            JTextField textField = new JTextField(fieldSize);
            this.mLabels.add(label);
            this.mTextFields.add(textField);
            textField.addActionListener(this);
        }
        Container cp = this.getContentPane();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, fieldNames.size() + 1, 0, 0);
        c.gridy = 0;
        int yindex = 0;
        for (int i = 0; i < (fieldNames.size() * 2); i++) {
            c.gridx = i % 2;
            if (((i % 2) == 0) && (i > 0)) {
                yindex++;
                c.gridy = yindex;
            }
            if ((i % 2) == 0) {
                cp.add(this.mLabels.elementAt(yindex), c);
            } else {
                cp.add(this.mTextFields.elementAt(yindex), c);
            }
        }
        c.gridx = 0;
        c.gridy = fieldNames.size() + 1;
        cp.add(this.mOKButton, c);
        c.gridx = 1;
        c.gridy = fieldNames.size() + 1;
        c.anchor = GridBagConstraints.WEST;
        cp.add(this.mCancelButton, c);
        this.addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                mTextFields.elementAt(0).requestFocusInWindow();
            }
        });
        this.pack();
        GuiUtil.centre(this);
        this.setVisible(true);
    }

    /**
     * When enter/return is pressed in a text field,
     * call the <code>OKAction{@link #actionPerformed(ActionEvent)}</code>
     * method.
     * 
     * @param arg0
     */
    public void actionPerformed(ActionEvent arg0) {
        (new OKAction()).actionPerformed(arg0);
    }

    /**
	 * Action to get the strings entered by the user.
	 * Nothing is done if any of the text fields are empty.
	 */
    private class OKAction implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            CustomDialog.mInput.clear();
            for (int i = 0; i < mTextFields.size(); i++) {
                String userText = mTextFields.elementAt(i).getText();
                if ((userText != null) && (userText.length() > 0)) {
                    mInput.add(userText);
                }
            }
            if (mInput.size() != mTextFields.size()) {
                mInput.clear();
            } else {
                CustomDialog.this.dispose();
            }
        }
    }

    /**
	 * Do nothing, just dispose window.
	 */
    private class CancelAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            CustomDialog.this.dispose();
        }
    }

    private static final long serialVersionUID = 1L;

    private final String mOK = "OK";

    private final String mCancel = "Cancel";

    private JButton mOKButton = null;

    private JButton mCancelButton = null;

    private Vector<JLabel> mLabels = new Vector<JLabel>();

    private Vector<JTextField> mTextFields = new Vector<JTextField>();

    private static Vector<String> mInput = new Vector<String>();
}
