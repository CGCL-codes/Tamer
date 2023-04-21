package jtq.implementation.common.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import jtq.column.IntegerColumn;
import jtq.core.ATable;
import jtq.core.SqlError;

class IntegerColumnImp extends NumberColumnImp<Integer, Number> implements IntegerColumn {

    IntegerColumnImp(String pColumnName, ATable<?> pTable, boolean pIsPrimaryKey) {
        super(pColumnName, pTable, pIsPrimaryKey);
    }

    IntegerColumnImp(String pColumnName, ATable<?> pTable, boolean pIsPrimaryKey, boolean pIsGenerated) {
        super(pColumnName, pTable, pIsPrimaryKey, pIsGenerated);
    }

    @Override
    public final Integer getValue(ResultSet pResultSet, int pColumnIndex) throws SqlError {
        try {
            int intValue = pResultSet.getInt(pColumnIndex);
            return !pResultSet.wasNull() ? Integer.valueOf(intValue) : null;
        } catch (SQLException e) {
            throw new SqlError(e);
        }
    }
}
