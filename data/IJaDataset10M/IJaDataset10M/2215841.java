package com.cep.jqemu.ui.action;

import com.cep.jqemu.JQEMU;
import com.cep.jqemu.ui.*;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author cbourque
 */
public class OptionsAction extends GenericAction {

    private OptionsDialog dialog;

    public OptionsAction(String name, JQEMU jqemu, int mnemonic) {
        super(name, jqemu, mnemonic);
        this.dialog = new OptionsDialog(jqemu, true);
    }

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        dialog.setVisible(true);
    }
}
