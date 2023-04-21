package com.magicpwd.e.mpro.file;

import com.magicpwd.__a.mpro.AMproAction;

/**
 *
 * @author Amon
 */
public class LockAction extends AMproAction {

    public LockAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        mproPtn.lockFrame();
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
