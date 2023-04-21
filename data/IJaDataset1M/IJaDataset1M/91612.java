package com.tensegrity;

import org.palo.api.Connection;
import org.palo.api.ConnectionConfiguration;
import org.palo.api.ConnectionFactory;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.server.paloaccessor.PaloHelper;

public class XMATest {

    public static void main(String[] args) {
        ConnectionConfiguration cc = ConnectionFactory.getInstance().getConfiguration("http://10.64.3.25", "olap/msmdpump.dll", "a", "a");
        cc.setType(Connection.TYPE_XMLA);
        cc.setLoadOnDemand(true);
        Connection con = ConnectionFactory.getInstance().newConnection(cc);
        Connection con2 = ConnectionFactory.getInstance().newConnection(cc);
        Database db = con.getDatabaseByName("Adventure Works DW");
        Dimension[] dims = db.getDimensions();
        int n = dims.length;
        System.out.println("Found " + n + " dimensions:");
        for (int i = 0; i < n; i++) {
            System.out.println("Dimension " + (i + 1) + ": " + dims[i].getName() + " [" + dims[i].getId() + "]");
        }
        Cube cube = db.getCubeByName("Finance");
        dims = cube.getDimensions();
        XDatabase xdb = new XDatabase();
        xdb.setNativeObject(db);
        PaloHelper.getXDimensions(cube.getDimensions(), xdb);
        Element[][] coords = new Element[dims.length][1];
        for (int i = 0; i < dims.length; i++) {
            coords[i][0] = dims[i].getElementAt(0);
        }
        Object[] data = cube.getDataArray(coords);
        System.out.println("Retrieved data:");
        for (int i = 0; i < data.length; i++) {
            System.out.println("  " + data);
        }
        con.disconnect();
    }
}
