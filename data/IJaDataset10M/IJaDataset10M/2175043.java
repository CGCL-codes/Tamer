package apollo.dataadapter.gbrowse;

import java.sql.*;
import java.util.*;
import apollo.dataadapter.mysql.*;

public class DBAdaptor extends BaseAdaptor {

    Hashtable adaptors;

    public DBAdaptor(MySQLDatabase db) {
        super(db);
        adaptors = new Hashtable();
    }

    public FeatureAdaptor getFeatureAdaptor() {
        if (!adaptors.containsKey("FeatureAdaptor")) {
            FeatureAdaptor fa = new FeatureAdaptor(getDatabase());
            adaptors.put("FeatureAdaptor", fa);
        }
        return (FeatureAdaptor) adaptors.get("FeatureAdaptor");
    }
}
