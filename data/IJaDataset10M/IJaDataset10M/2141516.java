package com.iplayawriter.novelizer.view;

import com.iplayawriter.novelizer.model.IValues;
import com.iplayawriter.novelizer.view.editors.GenericEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Generic dialog for adding or editing novel elements.
 * 
 * @author Erik
 */
class GenericDialog extends JDialog {

    /** The editor panel for the dialog */
    private final GenericEditor editor;

    /** 
     * Creates the dialog and lays out its controls for the values specified.
     * 
     * @param frame the frame this dialog is displayed for
     * @param title the title of the dialog
     * @param values the values used to create the controls
     */
    GenericDialog(JFrame frame, String title, IValues values) {
        super(frame, title);
        setLocationRelativeTo(frame);
        getContentPane().setLayout(new BorderLayout());
        editor = new GenericEditor(values);
        getContentPane().add(editor, BorderLayout.CENTER);
        JPanel buttonPane = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        JButton okButton = new JButton("OK");
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                editor.saveResults();
                setVisible(false);
            }
        });
        buttonPane.add(okButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPane.add(cancelButton);
        buttonPane.setAlignmentX(0f);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        pack();
    }

    /**
     * Gets the resulting values from running the dialog.
     * 
     * @return 
     */
    IValues getResults() {
        return editor.getResults();
    }
}
