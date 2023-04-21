package com.tensegrity.palowebviewer.modules.engine.server;

import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.CubeView;
import org.palo.api.Database;
import org.palo.api.ext.favoriteviews.FavoriteView;
import org.palo.api.ext.favoriteviews.FavoriteViewObject;
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;
import org.palo.api.ext.favoriteviews.FavoriteViewsFolder;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFavoriteNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class LoadFavoriteViewsTask extends DbTask {

    private XFolder result;

    protected String getServer() {
        return null;
    }

    protected void task() throws InvalidObjectPathException {
        Connection connection = getConnection();
        result = null;
        FavoriteViewTreeNode treeNode = connection.loadFavoriteViews();
        if (treeNode != null) {
            result = new XFolder();
            result.setConnnection(true);
            FavoriteViewObject object = treeNode.getUserObject();
            result.setName(object.getName());
            addChildren(result, treeNode);
        }
    }

    private void addChildren(XFolder folder, FavoriteViewTreeNode treeNode) {
        final int size = treeNode.getChildCount();
        for (int i = 0; i < size; i++) {
            FavoriteViewTreeNode childNode = treeNode.getChildAt(i);
            FavoriteViewObject object = childNode.getUserObject();
            XFavoriteNode node = null;
            if (object instanceof FavoriteView) {
                FavoriteView view = (FavoriteView) object;
                node = createLink(view);
            } else if (object instanceof FavoriteViewsFolder) {
                XFolder newFolder = new XFolder();
                node = newFolder;
                addChildren(newFolder, childNode);
            } else {
                Logger.error("unknown favorite view object " + object);
            }
            if (node != null) {
                node.setName(object.getName());
                folder.addChild(node);
            }
        }
    }

    private XViewLink createLink(FavoriteView view) {
        XViewLink result = null;
        CubeView cubeView = view.getCubeView();
        if (cubeView != null) {
            result = new XViewLink();
            String connectionId = getConnectionId(view.getConnection());
            Cube cube = cubeView.getCube();
            Database database = cube.getDatabase();
            String viewId = cubeView.getId();
            String cubeId = cube.getId();
            String databaseId = database.getId();
            result.setConnectionId(connectionId);
            result.setDatabaseId(databaseId);
            result.setCubeId(cubeId);
            result.setViewId(viewId);
        }
        return result;
    }

    private String getConnectionId(Connection connection) {
        return connection.getServer() + ":" + connection.getService();
    }

    public XFolder getResult() {
        return result;
    }
}
