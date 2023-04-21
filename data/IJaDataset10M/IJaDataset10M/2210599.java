package org.isi.monet.core.agents;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgentDatabaseMysql extends AgentDatabase {

    public String getDateAsText(Date dtDate) {
        if (dtDate == null) return "null";
        SimpleDateFormat sdtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "\'" + sdtFormat.format(dtDate) + "\'";
    }

    public Date getDate(ResultSet oResult, String idColumn) {
        try {
            return oResult.getTimestamp(idColumn);
        } catch (SQLException oException) {
            return null;
        }
    }

    public String getNullValue() {
        return "null";
    }

    public Boolean isValid(Connection oConnection) {
        Boolean bResult;
        Statement oStatement;
        try {
            oStatement = oConnection.createStatement();
            bResult = oStatement.execute("SELECT 1");
            return bResult;
        } catch (SQLException oException) {
            return false;
        }
    }

    public String getColumnDefinition(String Type) {
        if (Type.equals(AgentDatabase.ColumnTypes.BOOLEAN)) return "INT(1)";
        if (Type.equals(AgentDatabase.ColumnTypes.DATE)) return "DATETIME";
        if (Type.equals(AgentDatabase.ColumnTypes.INTEGER)) return "INT(11)";
        if (Type.equals(AgentDatabase.ColumnTypes.STRING)) return "VARCHAR(100) COLLATE UTF8_BIN";
        if (Type.equals(AgentDatabase.ColumnTypes.MEMO)) return "VARCHAR(255) COLLATE UTF8_BIN";
        return "VARCHAR(100)";
    }
}
