package edu.xtec.jclic.edit;

import edu.xtec.util.ExtendedJDialog;
import edu.xtec.util.Options;
import edu.xtec.util.ResourceManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class is a basic template of a {@link javax.swing.JDialog} useful for editing
 * objects. It builds two {@link javax.swing.Action} and {@link javax.swing.JButton}
 * objects under the main JPanel, used to confirm or cancel the changes made in the
 * edited object.
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class EditDialog extends ExtendedJDialog {

    public Options options;

    public JPanel southPanel;

    public Action okAction;

    public Action cancelAction;

    public JButton okButton, cancelButton;

    public boolean result = false;

    protected Component owner;

    public EditDialog(Options options, String msgKey, Dialog owner) {
        super(owner, options.getMsg(msgKey), true);
        this.owner = owner;
        buildDialog(options);
    }

    public EditDialog(Options options, String msgKey, Frame owner) {
        super(owner, options.getMsg(msgKey), true);
        this.owner = owner;
        buildDialog(options);
    }

    /** Creates new EditDialog */
    protected void buildDialog(Options options) {
        this.options = options;
        southPanel = new JPanel();
        okAction = new AbstractAction(options.getMsg("OK"), ResourceManager.getImageIcon("icons/commit_changes.gif")) {

            public void actionPerformed(ActionEvent ev) {
                result = true;
                setVisible(false);
            }
        };
        cancelAction = new AbstractAction(options.getMsg("CANCEL"), ResourceManager.getImageIcon("icons/cancel.gif")) {

            public void actionPerformed(ActionEvent ev) {
                result = false;
                setVisible(false);
            }
        };
        okButton = new JButton(okAction);
        cancelButton = new JButton(cancelAction);
        southPanel.add(okButton);
        southPanel.add(cancelButton);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

    public boolean showDialog() {
        pack();
        if (owner != null) centerOver(owner);
        setVisible(true);
        return result;
    }
}
