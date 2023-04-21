package gui.client.listener;

import gui.GuiCommon;
import gui.MenuName;
import gui.client.Client;
import gui.common.DefaultGroupList;
import gui.common.LoginDialogue;
import gui.common.msg.MsgType;
import gui.common.msg.SysMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import data.Group;

public class ClientMenuEventListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        Client client = Client.getInstance();
        String menuName = e.getActionCommand();
        if (menuName.equals(MenuName.INFO)) {
            SysMessage msg = new SysMessage();
            msg.subject = "About Discuss Forum";
            msg.detail = "Create by Scm && YM" + "\n2009-05-18";
            GuiCommon.showAlertWindow(msg);
        } else if (menuName.equals(MenuName.LOGIN)) {
            if (client.isConnected()) {
                LoginDialogue ld = new LoginDialogue(client);
                ld.setVisible(true);
            }
        } else if (menuName.equals(MenuName.LOGOUT)) {
            client.getDataClient().setCurrentUser(null);
            try {
                List<Group> groupInfo = null;
                DefaultGroupList gp = null;
                String welcome = client.getDataClient().getRemoteServer().getWelcomeInfo();
                groupInfo = client.getDataClient().getRemoteServer().getGroupList(false);
                gp = new DefaultGroupList(groupInfo, new ClientButtonListener(null), welcome);
                gp.showButtonBeforeLogin();
                client.setContent(gp);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (menuName.equals(MenuName.CONNECT)) {
            SysMessage msg = client.connectServer();
            if (msg.type == MsgType.Success) {
                try {
                    List<Group> groupInfo = null;
                    DefaultGroupList gp = null;
                    String welcome = client.getDataClient().getRemoteServer().getWelcomeInfo();
                    groupInfo = client.getDataClient().getRemoteServer().getGroupList(false);
                    gp = new DefaultGroupList(groupInfo, new ClientButtonListener(null), welcome);
                    gp.showButtonBeforeLogin();
                    client.setContent(gp);
                    msg.type = MsgType.Success;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    msg.detail = msg.detail + "\n" + e2.getMessage();
                }
            }
            if (msg.type != MsgType.Success) {
                GuiCommon.showAlertWindow(msg);
            }
        }
    }
}
