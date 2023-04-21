package net.sourceforge.webflowtemplate.utility.conversion.datatype;

import java.sql.SQLException;
import net.sourceforge.webflowtemplate.constants.db.DBProduct;
import net.sourceforge.webflowtemplate.db.DBExceptionTranslator;
import org.springframework.dao.DataAccessException;

public class DateTimeConversion {

    public static java.util.Date getDate(Object pDateTime) throws DataAccessException {
        final String METHOD_NAME = "getDate(TIMESTAMP)";
        java.util.Date d = null;
        if (pDateTime instanceof oracle.sql.TIMESTAMP) {
            try {
                d = ((oracle.sql.TIMESTAMP) pDateTime).timestampValue();
            } catch (SQLException sqle) {
                throw new DBExceptionTranslator(DBProduct.ORACLE).translate(METHOD_NAME + " -> converting Oracle TIMESTAMP to java Date", null, sqle);
            }
        } else if (pDateTime instanceof java.sql.Timestamp) {
            d = new java.util.Date(((java.sql.Timestamp) pDateTime).getTime());
        } else if (pDateTime instanceof java.sql.Date) {
            d = new java.util.Date(((java.sql.Date) pDateTime).getTime());
        }
        return d;
    }
}
