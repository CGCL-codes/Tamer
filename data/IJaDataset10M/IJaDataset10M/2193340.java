package com.apelon.dts.db.admin.migrate.from33to34.table;

import com.apelon.common.util.db.DBSystemConfig;
import com.apelon.dts.db.admin.DTSBaseTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Description : the import status create class
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Apelon Inc.
 * @version DTS 3.4.0
 * @since 3.4.0
 */
public class TableDTS_IMPORT_STATUS extends DTSBaseTable {

    protected TableDTS_IMPORT_STATUS(Connection targetConn, String connType, DBSystemConfig dbSystem, Map propertyMap) throws SQLException {
        super(targetConn, connType, dbSystem, propertyMap);
    }

    public TableDTS_IMPORT_STATUS() throws SQLException {
        super();
    }
}
