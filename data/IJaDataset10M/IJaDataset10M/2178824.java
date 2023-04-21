package osa.ora.server.client.ui.utils;

import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class TreeModelGenerator {

    /**
     * @param spaces
     * @return
     */
    public static DefaultTreeModel getModel(String rootNodeStr, Vector groups, Vector rooms, User user, boolean showOnlineOnly, boolean showUsersOnly) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootNodeStr);
        for (int i = 0; i < groups.size(); i++) {
            if (showUsersOnly) {
                if (((Group) groups.get(i)).getUsers() != null) {
                    setChildes(rootNode, ((Group) groups.get(i)).getUsers(), user, showOnlineOnly);
                }
            } else {
                DefaultMutableTreeNode nodeTemp = new DefaultMutableTreeNode(groups.get(i));
                rootNode.add(nodeTemp);
                if (((Group) groups.get(i)).getUsers() != null) {
                    setChildes(nodeTemp, ((Group) groups.get(i)).getUsers(), user, showOnlineOnly);
                }
            }
        }
        for (int i = 0; i < rooms.size(); i++) {
            DefaultMutableTreeNode nodeTemp = new DefaultMutableTreeNode(rooms.get(i));
            rootNode.add(nodeTemp);
        }
        return new DefaultTreeModel(rootNode);
    }

    /**
     * @param selectedNode
     * @param content
     */
    public static void setChildes(DefaultMutableTreeNode selectedNode, Vector<User> content, User user, boolean showOnlineOnly) {
        for (int i = 0; i < content.size(); i++) {
            if (user != null && user.getId() != content.get(i).getId()) {
                if (showOnlineOnly) {
                    if (content.get(i).getStatus_id() > 0) {
                        DefaultMutableTreeNode dmn = new DefaultMutableTreeNode(content.get(i));
                        selectedNode.add(dmn);
                    }
                } else {
                    DefaultMutableTreeNode dmn = new DefaultMutableTreeNode(content.get(i));
                    selectedNode.add(dmn);
                }
            }
        }
    }
}
