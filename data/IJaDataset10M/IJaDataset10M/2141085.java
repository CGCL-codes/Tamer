package com.magicpwd.e.maoc.mnum;

import com.magicpwd.__a.maoc.AMaocAction;

/**
 *
 * @author Aven
 */
public class CopyValueAction extends AMaocAction {

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        maocPtn.copyNumValue();
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
