package sjtu.llgx.util;

import java.util.HashMap;
import java.util.Map;

public final class JavaCenterHome {

    public static final boolean IN_JCHOME = true;

    public static final String JCH_CHARSET = "UTF-8";

    public static final String JCH_VERSION = "2.0";

    public static final int JCH_RELEASE = 20100830;

    public static String jchRoot = null;

    public static Map<String, String> jchConfig = new HashMap<String, String>();

    private static Map<String, String> tableNames = new HashMap<String, String>();

    public static String getTableName(String name) {
        String tableName = tableNames.get(name);
        if (tableName == null) {
            tableName = jchConfig.get("tablePre") + name;
            tableNames.put(name, tableName);
        }
        return tableName;
    }

    public static void clearTableNames() {
        tableNames.clear();
    }
}
