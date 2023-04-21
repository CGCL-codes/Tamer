package gui.listeners;

import gui.connect.UserConnector;
import gui.dbtree.MyNewTree;
import gui.start.MyApp;
import gui.util.DbTO;
import gui.util.TreeBO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import db.MsServerDB;

public class ListenerConnect implements ActionListener {

    public void actionPerformed(ActionEvent arg0) {
        try {
            if (MsServerDB.con.isClosed() == true) {
                System.out.println("Disconn");
                UserConnector userConnector = new UserConnector(MyApp.frame);
                userConnector.displayLoginWindow();
            } else {
                System.out.println("Connn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
