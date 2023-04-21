package com.magicpwd.e.mpro.file;

import com.magicpwd.__a.mpro.AMproAction;
import com.magicpwd._enum.AppView;
import com.magicpwd._util.Char;

/**
 *
 * @author Amon
 */
public class AppendAction extends AMproAction {

    public AppendAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (!mproPtn.newKeys()) {
            return;
        }
        if (mproPtn.getUserMdl().isEditVisible(AppView.mpro)) {
            return;
        }
        String cmd = e.getActionCommand();
        if (Char.isValidate(cmd)) {
            String[] arr = cmd.split(",");
            if (arr != null && arr.length == 2) {
                mproPtn.getMenuPtn().getButton(arr[0]).setSelected(true);
                mproPtn.getMenuPtn().getButton(arr[1]).setSelected(true);
            }
        }
        mproPtn.getUserMdl().setEditVisible(AppView.mpro, true);
        mproPtn.getUserMdl().setEditIsolate(AppView.mpro, true);
        mproPtn.setEditVisible(true);
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
