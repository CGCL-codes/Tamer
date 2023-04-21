package org.jbudget.gui;

import javax.swing.JButton;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import org.jbudget.gui.templ.DialogTemplate;
import org.jbudget.gui.templ.OkCancelButtonPanel;

/**
 *
 * @author anagaf
 */
public abstract class OkCancelButtonDialog extends DialogTemplate {

    public enum Result {

        OK, CANCEL, UNDEFINED
    }

    ;

    protected Result result = Result.UNDEFINED;

    protected OkCancelButtonPanel buttonPanel = new OkCancelButtonPanel();

    public OkCancelButtonDialog(java.awt.Frame parent) {
        super(parent, true);
        buttonPanelHolder.add(buttonPanel);
        buttonPanel.okButton.addActionListener(new OkListener());
        buttonPanel.cancelButton.addActionListener(new CancelListener());
        buttonPanel.helpButton.addActionListener(new HelpListener());
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                buttonPanel.cancelButton.doClick();
            }
        });
    }

    protected boolean handleOkPressed() {
        return true;
    }

    protected boolean handleCancelPressed() {
        return true;
    }

    protected boolean handleHelpPressed() {
        return true;
    }

    public Result getResult() {
        return result;
    }

    public class OkListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (handleOkPressed()) {
                result = Result.OK;
                setVisible(false);
                dispose();
            }
        }
    }

    public class CancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (handleCancelPressed()) {
                result = Result.CANCEL;
                setVisible(false);
                dispose();
            }
        }
    }

    public class HelpListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            handleHelpPressed();
        }
    }
}
