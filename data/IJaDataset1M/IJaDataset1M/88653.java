package org.visu.ui.components.apptree;

import org.visu.postgres.*;
import javax.swing.*;
import org.visu.ui.media.*;
import java.awt.event.*;
import org.visu.ui.*;
import java.sql.*;
import java.util.*;
import org.visu.ui.components.*;

/**
 * @author Vladimir Stanciu
 * @version 1.0
 */
public class DbAppTreeNode extends AppTreeNode {

    private DatabaseDescriptor databaseDescriptor;

    private ThinMenuItem conMenuItem = new ThinMenuItem();

    private ThinMenuItem disconMenuItem = new ThinMenuItem();

    private ThinMenuItem unregMenuItem = new ThinMenuItem();

    public DbAppTreeNode(DatabaseDescriptor dd, AppTreeNode parent) {
        super(parent);
        this.databaseDescriptor = dd;
        super.children = createChildren();
        createPopup();
    }

    protected List createChildren() {
        List children = new LinkedList();
        try {
            List schemas = getDatabaseDescriptor().getSchemas();
            Iterator iter = schemas.iterator();
            while (iter.hasNext()) {
                Schema s = (Schema) iter.next();
                children.add(new SchemaAppTreeNode(s, this));
            }
        } catch (SQLException ex) {
            Message.showError(ex);
        }
        return children;
    }

    public boolean equals(Object o) {
        if (!(o instanceof DbAppTreeNode)) {
            return false;
        }
        return getDatabaseDescriptor().equals(((DbAppTreeNode) o).getDatabaseDescriptor());
    }

    public void handleActionPerformed() {
    }

    public String getDisplayName() {
        return getDatabaseDescriptor().getDbName() + "@" + getDatabaseDescriptor().getHost();
    }

    public ImageIcon getIcon() {
        return MediaFile.getIcon(MediaFile.DB);
    }

    public DatabaseDescriptor getDatabaseDescriptor() {
        return databaseDescriptor;
    }

    protected void createPopup() {
        pop = new JPopupMenu();
        conMenuItem.setText("Connect");
        conMenuItem.setIcon(MediaFile.getIcon(MediaFile.CONNECT));
        conMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                conMenuItem_actionPerformed(e);
            }
        });
        disconMenuItem.setText("Disconnect");
        disconMenuItem.setIcon(MediaFile.getIcon(MediaFile.DISCONNECT));
        disconMenuItem.setEnabled(false);
        disconMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                disconMenuItem_actionPerformed(e);
            }
        });
        unregMenuItem.setText("Unregister " + getDatabaseDescriptor().getDbName());
        unregMenuItem.setIcon(MediaFile.getIcon(MediaFile.DBX));
        unregMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                unregMenuItem_actionPerformed(e);
            }
        });
        pop.add(conMenuItem);
        pop.add(disconMenuItem);
        pop.addSeparator();
        pop.add(unregMenuItem);
    }

    private void conMenuItem_actionPerformed(ActionEvent e) {
        try {
            getDatabaseDescriptor().connect();
            MainFrame.getInstance().getAppTree().reload();
            toggleConnectedMenu(true);
        } catch (SQLException ex) {
            Message.showError(ex);
        }
    }

    private void disconMenuItem_actionPerformed(ActionEvent e) {
        getDatabaseDescriptor().disconnect();
        toggleConnectedMenu(false);
        MainFrame.getInstance().getAppTree().reload();
    }

    private void unregMenuItem_actionPerformed(ActionEvent e) {
        int q = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Really unregister " + getDatabaseDescriptor().getDbName() + "?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.OK_OPTION) {
            APP.unregister(getDatabaseDescriptor());
            MainFrame.getInstance().getAppTree().reload();
        }
    }

    private void toggleConnectedMenu(boolean connected) {
        this.conMenuItem.setEnabled(!connected);
        this.disconMenuItem.setEnabled(connected);
    }
}
