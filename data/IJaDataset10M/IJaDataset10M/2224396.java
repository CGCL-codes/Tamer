package com.magicpwd.e.mpro.edit;

import com.magicpwd.__a.mpro.AMproAction;
import com.magicpwd._cons.ConsDat;

/**
 *
 * @author Amon
 */
public class ChangeDataAction extends AMproAction {

    public ChangeDataAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        mproPtn.changeBean(ConsDat.INDX_DATA);
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
