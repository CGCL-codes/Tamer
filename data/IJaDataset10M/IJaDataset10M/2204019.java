package crawler.init;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import crawler.db.DBObjectDB;
import crawler.db.ListDB;

public class init {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Connection cn = null;
        Collection<String> list = null;
        try {
            cn = DBObjectDB.get_connection();
            list = ListDB.findByLimit(cn, 10000, 10000);
            for (String id : list) {
                InitParseFriends.getFriends(cn, id, 40);
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
