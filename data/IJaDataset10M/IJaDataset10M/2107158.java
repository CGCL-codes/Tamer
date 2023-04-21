package com.foursoft.foureveredit.view.impl.swing.action;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import com.foursoft.fourever.xmlfileio.Fragment;
import com.foursoft.foureveredit.controller.command.DeleteCurrentInstanceCommand;
import com.foursoft.foureveredit.view.impl.swing.SwingUtils;
import com.foursoft.foureveredit.view.impl.swing.ViewManagerImpl;
import com.foursoft.mvc.view.swing.CommandAction;

/**
 * @author kivlehan_adm Action to execute a DeleteCurrentInstance Command
 */
public class DeleteCurrentInstanceCommandAction extends CommandAction {

    public DeleteCurrentInstanceCommandAction(String name, Icon icon, DeleteCurrentInstanceCommand command) {
        super(name, icon, command);
    }

    public void actionPerformed(ActionEvent e) {
        Fragment fragForCurrentinstance = ((DeleteCurrentInstanceCommand) command).getFragmentForCurrentSelection();
        if (fragForCurrentinstance != null) {
            int agreeWithDelete = JOptionPane.showConfirmDialog(SwingUtils.getContainingWindow(e.getSource()), ViewManagerImpl.getMessage("com.foursoft.foureveredit.view.agreeWithExternalDelete"), ViewManagerImpl.getMessage("com.foursoft.foureveredit.view.deleteLinkToExternal"), JOptionPane.YES_NO_OPTION);
            if (agreeWithDelete != JOptionPane.YES_OPTION) {
                return;
            }
        }
        super.actionPerformed(e);
    }
}
