package druid.core.jdbc.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import ddf.lib.SqlMapper;
import ddf.type.SqlType;
import druid.core.jdbc.JdbcConnection;
import druid.core.jdbc.JdbcLib;
import druid.core.jdbc.RecordList;

public abstract class CodingEntity extends AbstractEntity {

    public RecordList rlParameters;

    public CodingEntity(JdbcConnection conn, String name, String type, String rems) {
        super(conn, name, type, rems);
    }

    protected void loadInfoI() throws SQLException {
        JdbcConnection jdbcConn = getJdbcConnection();
        ResultSet rs = jdbcConn.getMetaData().getProcedureColumns(null, getSchema(), sName, "%");
        RecordList rl = jdbcConn.retrieveResultSet(rs);
        rl.removeColumn(12);
        rl.removeColumn(10);
        rl.removeColumn(9);
        rl.removeColumn(8);
        rl.removeColumn(7);
        rl.removeColumn(6);
        rl.removeColumn(2);
        rl.removeColumn(1);
        rl.removeColumn(0);
        for (int i = 0; i < rl.getRowCount(); i++) {
            Vector row = rl.getRecordAt(i);
            Object param = row.elementAt(1);
            Object type = row.elementAt(2);
            String sParam = JdbcLib.convertParam(param);
            SqlType sqlType = SqlMapper.mapId(type);
            rl.setValueAt(sParam, i, 1);
            rl.setValueAt(sqlType.sName, i, 2);
        }
        rlParameters = rl;
    }
}
