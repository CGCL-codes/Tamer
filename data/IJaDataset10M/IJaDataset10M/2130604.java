package com.arsenal.repository.client.listeners;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import com.arsenal.message.*;
import com.arsenal.client.*;
import com.arsenal.repository.RepositoryDataBean;
import com.arsenal.repository.message.RemoveRepositoryMessage;
import com.arsenal.repository.client.panels.RepositoryPanel;
import com.arsenal.session.client.panels.SessionPanel;
import com.arsenal.user.client.UserClientHandler;
import com.arsenal.log.Log;

public class RemoveRepositoryButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        if (Client.getInstance().isConnectedToServer()) {
            Log.debug(this, "Remove File From Repository Button Pressed....");
            Log.debug(this, "Will try to add remove file to repository: " + RepositoryPanel.getInstance().currentRepositoryNodeChosen);
            if (RepositoryPanel.getInstance().doesCurrentRepositoryChosenHaveAnyFilesInIt()) {
                RepositoryDataBean rdb = new RepositoryDataBean(RepositoryPanel.getInstance().currentRepositoryNodeChosen, ConnectionWindow.getInstance().getUsername(), RepositoryPanel.getInstance().currentRepositoryNodeChosenPermission);
                rdb.setGroup(UserClientHandler.getInstance().getGroupForMyUser());
                Log.debug(this, "Request to delete file from repository: " + RepositoryPanel.getInstance().currentRepositoryNodeChosen + "|" + RepositoryPanel.getInstance().currentRepositoryNodeChosenPermission);
                RemoveRepositoryMessage message = new RemoveRepositoryMessage();
                message.setHandlerName("repository");
                message.setPayload(rdb);
                if (Client.getInstance().isConnectedToServer()) Client.getInstance().sendMessage(message);
                SessionPanel.getInstance().disableAll();
                RepositoryPanel.getInstance().disableRepositoryButtons();
            } else {
            }
        } else {
        }
    }
}
