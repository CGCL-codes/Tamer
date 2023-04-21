package org.digitall.projects.apps.dbadmin_091.classes;

public class DBAdminConfiguration {

    private static String dbOwner = "postgres";

    public DBAdminConfiguration() {
    }

    public static void setDbOwner(String _dbOwner) {
        dbOwner = _dbOwner;
    }

    public static String getDbOwner() {
        return dbOwner;
    }
}
