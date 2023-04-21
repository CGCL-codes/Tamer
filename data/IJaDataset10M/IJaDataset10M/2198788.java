package org.filbyte.dc;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import org.elite.jdcbot.framework.*;
import org.filbyte.*;

public class UserPopup implements PrettyTable.PopupBuilder {

    DCContext context;

    public UserPopup(DCContext context) {
        this.context = context;
    }

    @Override
    public void buildPopup(JTable table, MouseEvent e, List<Integer> selected) {
        SU.assertEdt();
        TableModel tm = table.getModel();
        if (!(tm instanceof UserTableModel)) return;
        UserTableModel utm = (UserTableModel) tm;
        final List<User> users = new ArrayList<User>();
        for (Integer i : selected) users.add(utm.getUser(i));
        JPopupMenu menu = new JPopupMenu();
        JMenuItem download = new JMenuItem("Start chat");
        download.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (User user : users) {
                    PMTab p = PMTab.maybeBuild(user, null);
                    if (p != null) {
                        context.tabs.addTab("PM - " + user.username(), p, true);
                    }
                }
            }
        });
        JMenuItem getFilelist = new JMenuItem("Get filelist");
        getFilelist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (User user : users) {
                    try {
                        context.manager.shareManager.downloadOthersFileList(user);
                    } catch (BotException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        menu.add(download);
        menu.add(getFilelist);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
