package com.tensegrity;

import org.palo.api.Connection;
import org.palo.api.ConnectionConfiguration;
import org.palo.api.ConnectionFactory;
import org.palo.api.Cube;
import org.palo.api.CubeView;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.ext.favoriteviews.FavoriteView;
import org.palo.api.ext.favoriteviews.FavoriteViewObject;
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.server.paloaccessor.PaloAccessor;

public class CubeCount {

    static int number = 2;

    private static void iterateFV(FavoriteViewTreeNode node) {
        if (node != null) {
            FavoriteViewObject obj = node.getUserObject();
            if (obj != null) {
                obj.getName();
                if (obj instanceof FavoriteView) {
                    FavoriteView fv = (FavoriteView) obj;
                    CubeView view = fv.getCubeView();
                    if (view != null) {
                        view.getName();
                        view.getId();
                    }
                }
            }
            FavoriteViewTreeNode[] nodes = node.getChildren();
            for (int j = 0; j < nodes.length; j++) {
                FavoriteViewTreeNode child = nodes[j];
                iterateFV(child);
            }
            ;
        }
    }

    private static void dump(Connection conn) {
        FavoriteViewTreeNode node = conn.loadFavoriteViews();
        iterateFV(node);
        Database db = conn.getDatabaseByName("olga-test");
        Cube cubes[] = db.getCubes();
        System.out.println("number of cubes : " + cubes.length);
    }

    private static void iterate(Connection conn) {
        Database[] db = conn.getDatabases();
        for (int i = 0; i < db.length; i++) {
            Database database = db[i];
            Dimension[] dims = database.getDimensions();
            for (int j = 0; j < dims.length; j++) {
                Dimension dimension = dims[j];
                dimension.getElementsTree();
            }
            Cube[] cubes = database.getCubes();
            for (int j = 0; j < cubes.length; j++) {
                Cube cube = cubes[j];
                cube.getCubeViews();
                cube.getDimensions();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Connection[] c = new Connection[number];
        c[1] = getConnection("dmol-temp");
        c[0] = getConnection("10.64.3.25");
        PaloAccessor accessor = new PaloAccessor();
        XPath pathDB = new XPath("/XRoot::Root/dmol-temp\\\\:7777:dmol-temp\\\\:7777:Server");
        XPath pathCubes = new XPath("/XRoot::Root/dmol-temp\\\\:7777:dmol-temp\\\\:7777:Server/3:olga-test:Database");
        iterateFV(c[1].loadFavoriteViews());
        iterateFV(c[0].loadFavoriteViews());
        accessor.loadChildren(pathDB, IXConsts.TYPE_DATABASE, c[1]);
        accessor.loadChildren(pathCubes, IXConsts.TYPE_CUBE, c[1]);
        dump(c[1]);
        System.out.println("add/delete cube in olga-temp and type enter");
        System.in.read();
        System.out.println("reloading...");
        c[1].reload();
        for (int i = 0; i < c.length; i++) {
            dump(c[i]);
        }
        for (int i = 0; i < c.length; i++) {
            c[i].disconnect();
        }
    }

    private static Connection getConnection(String host) {
        ConnectionConfiguration cc = new ConnectionConfiguration(host, "7777");
        cc.setUser("admin");
        cc.setPassword("admin");
        int type = Connection.TYPE_HTTP;
        cc.setType(type);
        Connection connection = ConnectionFactory.getInstance().newConnection(cc);
        return connection;
    }
}
