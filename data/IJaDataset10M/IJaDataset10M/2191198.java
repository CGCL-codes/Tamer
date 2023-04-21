package com.magicpwd.e.mpro.edit;

import com.magicpwd.__a.mpro.AMproAction;
import com.magicpwd._cons.ConsDat;

/**
 *
 * @author Amon
 */
public class AppendTextAction extends AMproAction {

    public AppendTextAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        mproPtn.appendBean(ConsDat.INDX_TEXT);
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
