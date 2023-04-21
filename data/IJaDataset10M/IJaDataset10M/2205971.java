package jtq.implementation.common.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import jtq.ICondition;
import jtq.column.AColumn;
import jtq.column.StringColumn;
import jtq.core.ATable;
import jtq.core.OperatorEnum;
import jtq.core.ParameterStatement;
import jtq.core.SqlError;

class StringColumnImp extends AColumn<String, String> implements StringColumn {

    StringColumnImp(String pColumnName, ATable<?> pTable, boolean pIsPrimaryKey) {
        super(pColumnName, pTable, pIsPrimaryKey);
    }

    public final ICondition likeC(String pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.LikeC, pValue);
    }

    public final ICondition notLikeC(String pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.NotLikeC, pValue);
    }

    public final ICondition likeI(String pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.LikeI, pValue);
    }

    public final ICondition notLikeI(String pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.NotLikeI, pValue);
    }

    @Override
    public final String getValue(ResultSet pResultSet, int pColumnIndex) throws SqlError {
        try {
            return pResultSet.getString(pColumnIndex);
        } catch (SQLException e) {
            throw new SqlError(e);
        }
    }

    @Override
    public int setParameters(ParameterStatement pStatement, int pCurrentParameter, Object pValue) throws SQLException {
        if (pValue == null) pStatement.setNull(pCurrentParameter, Types.VARCHAR); else pStatement.setString(pCurrentParameter, (String) pValue);
        return pCurrentParameter + 1;
    }
}
