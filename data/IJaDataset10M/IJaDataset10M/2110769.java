package axs.jdbc.dataSpecification.supportedJavaTypes;

import java.sql.Date;
import axs.jdbc.dataSourceConfiguration.TypeException;
import axs.jdbc.utils.DateUtils;

public class SqlDateClass extends TypeClass {

    public Object fromString(String value) throws TypeException {
        try {
            if (value == null) return (Date) null;
            value = value.trim();
            if (value.toUpperCase().equals("NULL") || value.equals("")) return (Date) null;
            return Date.valueOf(value);
        } catch (Exception e) {
            throw new TypeException("Unable to parse from String to Date -> " + e.getMessage());
        }
    }

    public Object fromString(String value, String dateFormat) throws TypeException {
        try {
            if (value == null) return (Date) null;
            value = value.trim();
            if (value.toUpperCase().equals("NULL")) return (Date) null;
            return DateUtils.parse(value, dateFormat);
        } catch (Exception e) {
            throw new TypeException("Unable to parse from String to Date -> " + e.getMessage());
        }
    }
}
