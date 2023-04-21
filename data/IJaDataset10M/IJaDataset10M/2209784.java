package org.javizy.sql;

import org.javizy.sql.queries.ShowTables;

public class DatabaseUtils {

    private DatabaseUtils() {
    }

    public static String[] getTables(AbstractDatabase db) {
        ShowTables show = ShowTables.getInstance();
        try {
            db.Select(show);
            return show.getTables();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
