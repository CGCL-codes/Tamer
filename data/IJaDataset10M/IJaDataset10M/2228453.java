package edu.vt.eng.swat.workflow.db.sqlite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import static com.google.common.base.Preconditions.checkNotNull;
import edu.vt.eng.swat.workflow.db.base.BaseDao;

public class SQLiteBaseDao extends BaseDao {

    private static Logger LOG = Logger.getLogger(SQLiteBaseDao.class);

    private static DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    protected void setDateValue(PreparedStatement statement, int index, Date date) throws SQLException {
        checkNotNull(statement);
        if (date != null) {
            statement.setString(index, format.format(date));
        } else {
            statement.setString(index, null);
        }
    }

    @Override
    protected Date getDateValue(ResultSet resultSet, String columnName) throws SQLException {
        checkNotNull(resultSet);
        checkNotNull(columnName);
        String dateValue = resultSet.getString(columnName);
        if (dateValue != null) {
            Date date = null;
            try {
                date = format.parse(dateValue);
            } catch (ParseException ex) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(ex.getMessage());
                }
            }
            return date;
        }
        return null;
    }

    @Override
    protected <T extends Enum<T>> T getEnumValue(ResultSet resultSet, String columnName, Class<T> clazz) throws SQLException {
        checkNotNull(resultSet);
        checkNotNull(columnName);
        checkNotNull(clazz);
        Integer dbValue = resultSet.getInt(columnName);
        for (T enumValue : clazz.getEnumConstants()) {
            if (enumValue.ordinal() == dbValue.intValue()) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Illegal argument in column " + columnName + " : " + dbValue + "; Enum class " + clazz + " doesn't have such value;");
    }

    @Override
    protected <T extends Enum<T>> void setEnumValue(PreparedStatement statement, int index, T enumValue) throws SQLException {
        checkNotNull(statement);
        checkNotNull(enumValue);
        statement.setLong(index, enumValue.ordinal());
    }
}
