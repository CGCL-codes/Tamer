package cn.edu.buaa.nlsde.grid.DatabaseOperate;

import java.sql.SQLException;
import java.util.*;
import cn.edu.buaa.nlsde.grid.DatabaseConnect.*;
import cn.edu.buaa.nlsde.grid.DatabaseManage.GlobalMapping;
import cn.edu.buaa.nlsde.grid.Structure.*;

public class TreeGenerate {

    DBPool dbpool = null;

    GlobalMapping gm = null;

    public TreeGenerate() {
        this.dbpool = new DBPool(DBInitial.getJndiName());
        gm = new GlobalMapping(dbpool);
    }

    public Grid formTree() {
        Grid grid = gm.getGrids();
        List<Cluster> clusters = gm.getClusters(grid.getID());
        for (int i = 0; i < clusters.size(); i++) {
            int id = ((Cluster) clusters.get(i)).getID();
            List<Host> hosts = gm.getHosts(id);
            ((Cluster) clusters.get(i)).setHosts(hosts);
        }
        grid.setClusters(clusters);
        try {
            dbpool.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grid;
    }
}
