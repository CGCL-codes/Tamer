package mx.com.nyak.base.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/** 
 * 
 * Derechos Reservados (c)Jose Carlos Perez Cervantes 2009 
 * 
 * 
 * */
public class MapConverter {

    private static Logger logger = Logger.getLogger(MapConverter.class);

    public static Map toMap(ResultSet rs) throws SQLException {
        Map map = new HashMap();
        ResultSetMetaData meta = rs.getMetaData();
        int numberOfColumns = meta.getColumnCount();
        for (int i = 1; i <= numberOfColumns; ++i) {
            String name = meta.getColumnName(i);
            Object value = rs.getObject(i);
            logger.debug("[" + name + "," + value + "]");
            map.put(StringUtil.convertToCamelCase(name), value + "");
        }
        return map;
    }
}
