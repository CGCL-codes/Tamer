package nl.mwensveen.csv.db.type;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import nl.mwensveen.csv.db.type.api.DbType;

/**
 * @author mwensveen
 *
 */
public class DateDbType implements DbType {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    /**
	 * @throws SQLException 
	 * @see nl.mwensveen.csv.db.type.api.DbType#getInsertValue(int, java.sql.ResultSet)
	 */
    public String getInsertValue(int columnNumber, ResultSet resultSet) throws SQLException {
        java.sql.Date value = getValue(columnNumber, resultSet);
        return "'" + df.format(value) + "'";
    }

    private Date getValue(int columnNumber, ResultSet resultSet) throws SQLException {
        return resultSet.getDate(columnNumber);
    }

    /**
	 * @see nl.mwensveen.csv.db.type.api.DbType#getSqlType()
	 */
    public String getSqlType() {
        return "DATE";
    }

    /**
	 * @see nl.mwensveen.csv.db.type.api.DbType#insertIntoPreparedStatement(PreparedStatement, int, ResultSet, int)
	 */
    public void insertIntoPreparedStatement(PreparedStatement preparedStatement, int i, ResultSet resultSet, int j) throws SQLException {
        preparedStatement.setDate(i, getValue(j, resultSet));
    }
}
