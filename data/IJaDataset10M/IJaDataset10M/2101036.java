package ddbserver.connections;

import ddbserver.common.GDDNode;
import ddbserver.common.Site;
import ddbserver.constant.Constant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Roar
 */
public class GDDManager {

    private HashMap<String, GDDNode> GDD;

    private Properties properties;

    private SiteManager siteManager;

    private int nodeconter;

    private static GDDManager instance = null;

    public static synchronized GDDManager getInstance() {
        if (instance == null) {
            instance = new GDDManager();
        }
        return instance;
    }

    private GDDNode getTree(int num) throws Exception {
        GDDNode retval = new GDDNode();
        retval.setPredicate(new ArrayList<String>());
        retval.setSons(new ArrayList<GDDNode>());
        String leaf = properties.getProperty("node" + num + "isleaf");
        if (leaf.startsWith("Y") || leaf.startsWith("y")) {
            retval.setIsLeaf(true);
            String site = properties.getProperty("node" + num + "site");
            retval.setLocation(siteManager.getSiteByName(site));
        } else {
            retval.setIsLeaf(false);
            retval.setLocation(null);
            int sonnum = Integer.parseInt(properties.getProperty("node" + num + "sonnum"));
            retval.setFragmentType(properties.getProperty("node" + num + "fragmentType", Constant.DDB_FRAGMENT_UNKNOW));
            for (int i = 0; i < sonnum; i++) {
                retval.getPredicate().add(properties.getProperty("node" + num + "son" + i + "predicate"));
                retval.getSons().add(getTree(Integer.parseInt(properties.getProperty("node" + num + "son" + i + "nodenum"))));
            }
        }
        return retval;
    }

    private int setTree(GDDNode current) throws Exception {
        int retval = nodeconter++;
        if (current.isIsLeaf()) {
            properties.setProperty("node" + retval + "isleaf", "y");
            properties.setProperty("node" + retval + "site", current.getLocation().getName());
        } else {
            properties.setProperty("node" + retval + "isleaf", "n");
            properties.setProperty("node" + retval + "fragmentType", current.getFragmentType());
        }
        int sonnum = current.getSons().size();
        properties.setProperty("node" + retval + "sonnum", "" + sonnum);
        for (int idx = 0; idx < sonnum; idx++) {
            properties.setProperty("node" + retval + "son" + idx + "predicate", current.getPredicate().get(idx));
            properties.setProperty("node" + retval + "son" + idx + "nodenum", "" + setTree(current.getSons().get(idx)));
        }
        return retval;
    }

    private GDDManager() {
        this.siteManager = SiteManager.getInstance();
        GDD = new LinkedHashMap<String, GDDNode>();
        try {
            properties = new Properties();
            if (!new File("GDD.properties").exists()) {
                new File("GDD.properties").createNewFile();
                properties.load(new FileInputStream("GDD.properties"));
                properties.setProperty("tablenum", "0");
                properties.store(new FileOutputStream("GDD.properties"), null);
            }
            properties.load(new FileInputStream("GDD.properties"));
            int sitenum = Integer.parseInt(properties.getProperty("tablenum", "0"));
            for (int i = 0; i < sitenum; i++) {
                String tableName = properties.getProperty("table" + i + "name");
                int rootnode = Integer.parseInt(properties.getProperty("table" + i + "root", "0"));
                GDDNode root = getTree(rootnode);
                GDD.put(tableName, root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveProperties() {
        try {
            properties.load(new FileInputStream("GDD.properties"));
            properties.setProperty("tablenum", "" + GDD.size());
            Set<String> keys = GDD.keySet();
            int i = 0;
            nodeconter = 0;
            for (String key : keys) {
                properties.setProperty("table" + i + "name", key);
                properties.setProperty("table" + i + "root", "" + setTree(GDD.get(key)));
                i++;
            }
            properties.store(new FileOutputStream("GDD.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGDD(HashMap<String, GDDNode> GDD) {
        this.GDD = GDD;
        this.saveProperties();
    }

    public GDDNode getGDDNodeByTableName(String tableName) {
        for (String key : GDD.keySet()) {
            if (key.equalsIgnoreCase(tableName)) {
                return GDD.get(key);
            }
        }
        return null;
    }
}
