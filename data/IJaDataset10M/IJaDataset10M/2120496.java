package org.tango.workbench.navigator.model;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.ApiUtil;

public class DatabaseNode extends TreeNode {

    public DatabaseNode(String host, String port) {
        super(null);
        this.name = host + ":" + port;
        this.type = "Database";
        try {
            db = ApiUtil.get_db_obj(host, port);
        } catch (DevFailed e) {
        }
    }

    public DatabaseNode(TreeNode parent) {
        super(parent);
        String tangoHost = System.getProperty("TANGO_HOST", "null");
        this.name = tangoHost;
        this.type = "database";
        if (tangoHost.equals("null") || tangoHost.equals("")) {
        } else {
            try {
                db = ApiUtil.get_db_obj();
            } catch (DevFailed e) {
            }
        }
    }
}
