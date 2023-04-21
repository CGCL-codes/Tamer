package org.tcpfile.sql;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.main.Misc;
import org.tcpfile.main.UnbreakableTimerTask;
import org.tcpfile.net.ByteArray;
import org.tcpfile.utils.MapIterator;

/**
 * A list of serializable objects that are stored into the Database at 
 * Shutdown and retrieved after initialization of the rest of tcpfile.
 * Objects can install themselves with Initializable on startup after deserialization.
 * @author Stivo
 *
 */
public class SQLPersistantObjects {

    private static Logger log = LoggerFactory.getLogger(SQLPersistantObjects.class);

    private static HashMap<String, Serializable> objects = new HashMap<String, Serializable>();

    /**
	 * Use this class only static
	 */
    private SQLPersistantObjects() {
    }

    public static void remove(Serializable o) {
        MapIterator<String, Serializable> mapi = new MapIterator<String, Serializable>(objects);
        while (mapi.hasNext()) {
            if (mapi.next().equals(o)) {
                mapi.remove();
                break;
            }
        }
    }

    public static void add(Serializable o) {
        add(findLowestFreeKey(), o);
    }

    private static String findLowestFreeKey() {
        int key = 0;
        while (objects.containsKey(key + "")) key++;
        return key + "";
    }

    public static void add(String key, Serializable o) {
        assert (o != null && key != null);
        objects.put(key, o);
    }

    public static void loadSQLPersistantObjects() {
        SQLConnection sql = new SQLConnection();
        try {
            ResultSet rs = sql.getResultSet("select * from objects");
            while (rs.next()) {
                byte[] b = rs.getBytes("object");
                Serializable o = (Serializable) ByteArray.toObject(b);
                String s = rs.getString("id");
                if (null == s || s.equals("")) s = findLowestFreeKey();
                if (o instanceof SQLInitializable) {
                    ((SQLInitializable) o).afterLoad(sql, "select * from objects");
                }
                add(s, o);
            }
        } catch (SQLException e) {
            log.warn("", e);
        }
        TimerTask tt = new UnbreakableTimerTask() {

            public void runCaught() {
                saveSQLPersistantObjects();
            }
        };
        Misc.timer.schedule(tt, 1800 * 1000, 1800 * 1000);
    }

    public static void saveSQLPersistantObjects() {
        SQLConnection sql = new SQLConnection();
        sql.sendUpdate("delete from objects", false);
        MapIterator<String, Serializable> mapi = new MapIterator<String, Serializable>(objects);
        while (mapi.hasNext()) {
            Serializable spo = mapi.next();
            String id = mapi.getCurrentKey();
            boolean done;
            byte[] bytes = ByteArray.toByteArray(spo);
            done = sql.updateWithBlob("insert into objects (id,object) values ('" + id + "',?)", bytes);
            if (!done) {
                log.warn("Could not access database, can not save serializable objects");
                Misc.sleeps(50);
            }
        }
    }
}
