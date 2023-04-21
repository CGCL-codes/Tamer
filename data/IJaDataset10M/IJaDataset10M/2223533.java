package com.magicpwd.e.maoc.mexp;

import com.magicpwd.__a.maoc.AMaocAction;

/**
 *
 * @author Aven
 */
public class ReuseValueAction extends AMaocAction {

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        maocPtn.reuseExpValue();
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
