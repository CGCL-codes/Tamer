package xbird.util.jdbc;

import java.sql.SQLException;
import xbird.config.Settings;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI <yuin405+xbird@gmail.com>
 */
public interface BulkLoader {

    static final String COLUMN_DELIMITER = Settings.get("xbird.db.delimiter");

    static final String CHARACTER_DEMIMITER = Settings.get("xbird.db.character_delimiter");

    static final String ENV_ENCODING = Settings.get("xbird.xml.encoding");

    public void importTable(String tableName, String file) throws SQLException;
}
