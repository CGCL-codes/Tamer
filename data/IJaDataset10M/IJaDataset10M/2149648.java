package it.bfp.sark.sql.input.db2;

import it.bfp.sark.common.NameFormatter;
import it.bfp.sark.sql.input.SqlInput;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author BFP
 */
public class Db2SqlInput extends SqlInput {

    @Override
    protected boolean isValidTable(String tableOwner) throws SQLException {
        if (StringUtils.isBlank(tableOwner)) {
            throw new SQLException("Cannot retrive Owner Name");
        }
        if (StringUtils.equalsIgnoreCase(tableOwner, jdbcConnect.getSchema())) {
            return true;
        }
        return false;
    }

    @Override
    protected String getTableName(String tableOwner, String tableName) {
        return NameFormatter.toClassName(tableName);
    }
}
