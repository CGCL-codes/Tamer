package org.dbunit.dataset.datatype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dbunit.dataset.ITable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Manuel Laflamme
 * @version $Revision: 1023 $
 */
public class IntegerDataType extends AbstractDataType {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(IntegerDataType.class);

    IntegerDataType(String name, int sqlType) {
        super(name, sqlType, Integer.class, true);
    }

    public Object typeCast(Object value) throws TypeCastException {
        logger.debug("typeCast(value={}) - start", value);
        if (value == null || value == ITable.NO_VALUE) {
            return null;
        }
        if (value instanceof Number) {
            return new Integer(((Number) value).intValue());
        }
        String stringValue = value.toString().trim();
        if (stringValue.length() <= 0) {
            return null;
        }
        try {
            return typeCast(new BigDecimal(stringValue));
        } catch (java.lang.NumberFormatException e) {
            throw new TypeCastException(value, this, e);
        }
    }

    public Object getSqlValue(int column, ResultSet resultSet) throws SQLException, TypeCastException {
        if (logger.isDebugEnabled()) logger.debug("getSqlValue(column={}, resultSet={}) - start", new Integer(column), resultSet);
        int value = resultSet.getInt(column);
        if (resultSet.wasNull()) {
            return null;
        }
        return new Integer(value);
    }

    public void setSqlValue(Object value, int column, PreparedStatement statement) throws SQLException, TypeCastException {
        if (logger.isDebugEnabled()) logger.debug("setSqlValue(value={}, column={}, statement={}) - start", new Object[] { value, new Integer(column), statement });
        statement.setInt(column, ((Integer) typeCast(value)).intValue());
    }
}
