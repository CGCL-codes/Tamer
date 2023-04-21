package hoplugins.teamplanner.dao;

import hoplugins.Commons;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Missing Class Documentation
 *
 * @author Draghetto
 */
public class DividerDAO {

    /** Missing Parameter Documentation */
    public static final int DEFAULT_POSITION = 50;

    static {
        checkTable();
    }

    /**
     * Creates a new DividerDAO object.
     */
    private DividerDAO() {
    }

    /**
     * Missing Method Documentation
     *
     * @param key Missing Method Parameter Documentation
     * @param position Missing Method Parameter Documentation
     */
    public static void setDividerPosition(String key, int position) {
        String query = "update TEAMPLANNER_DIVIDER set POSITION = " + position + " where KEY = '" + key + "'";
        int count = Commons.getModel().getAdapter().executeUpdate(query);
        if (count == 0) {
            Commons.getModel().getAdapter().executeUpdate("insert into TEAMPLANNER_DIVIDER (KEY, POSITION) values ('" + key + "', " + position + ")");
        }
    }

    /**
     * Missing Method Documentation
     *
     * @param key Missing Method Parameter Documentation
     *
     * @return Missing Return Method Documentation
     */
    public static int getDividerPosition(String key) {
        ResultSet rs;
        String query = "select POSITION from TEAMPLANNER_DIVIDER where KEY='" + key + "'";
        rs = Commons.getModel().getAdapter().executeQuery(query);
        int i = DEFAULT_POSITION;
        try {
            rs.next();
            i = rs.getInt("POSITION");
        } catch (SQLException e1) {
        }
        try {
            rs.close();
        } catch (SQLException e1) {
        }
        return i;
    }

    /**
     * Missing Method Documentation
     */
    private static void checkTable() {
        try {
            ResultSet rs = Commons.getModel().getAdapter().executeQuery("select * from TEAMPLANNER_DIVIDER");
            rs.next();
        } catch (Exception e) {
        }
    }
}
