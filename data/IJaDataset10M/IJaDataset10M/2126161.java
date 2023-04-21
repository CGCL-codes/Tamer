package com.magicpwd.e.mpad;

import com.magicpwd.__a.mpad.AMpadAction;

/**
 *
 * @author Amon
 */
public class DropAction extends AMpadAction {

    public DropAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        mpadPtn.deleteNote();
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
