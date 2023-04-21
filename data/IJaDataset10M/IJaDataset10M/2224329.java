package preprocessing.methods.Import.databasedata.database;

import preprocessing.storage.PreprocessingStorage;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *This class converts java.sql.Types to Double or String data format.
 *
 * @author Jiri Petnik
 */
public class ConvertTypes {

    private ConvertTypes() {
    }

    /**
     * Retrieves whether is possible to convert input type.
     *
     * @param type data type
     * @return true if so; false otherwise
     * @see java.sql.Types
     */
    public static boolean isConvertable(int type) {
        switch(type) {
            case Types.BIGINT:
            case Types.BIT:
            case Types.BOOLEAN:
            case Types.CHAR:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.INTEGER:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NUMERIC:
            case Types.NVARCHAR:
            case Types.REAL:
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.VARCHAR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Retrieves data from ResultSet and convert them to an appropriate object type.
     *
     * @param rs ResultSet from which we want to get data
     * @param columnIndex data position in ResultSet
     * @return converted data Object
     * @throws SQLException thrown when mistake during getting data from ResultSet
     */
    public static Object getObject(ResultSet rs, int columnIndex) throws SQLException {
        int type = rs.getMetaData().getColumnType(columnIndex);
        switch(type) {
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                String s = rs.getNString(columnIndex);
                if (s != null) return s;
                return "";
            case Types.CHAR:
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
                String s2 = rs.getString(columnIndex);
                if (s2 != null) return s2;
                return "";
            case Types.DECIMAL:
            case Types.NUMERIC:
                BigDecimal bd = rs.getBigDecimal(columnIndex);
                if (bd == null) {
                    return Double.NaN;
                }
                return bd.doubleValue();
            case Types.BOOLEAN:
            case Types.BIT:
                Boolean b = rs.getBoolean(columnIndex);
                if (b == null) {
                    return Double.NaN;
                }
                if (b) {
                    return 1;
                }
                return 0;
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.REAL:
            case Types.SMALLINT:
            case Types.TINYINT:
                Double d = rs.getDouble(columnIndex);
                if (d == null) {
                    return Double.NaN;
                }
                return d;
            default:
                return null;
        }
    }

    /**
     * Convert java.sql.Types into GAME data type.
     *
     * @param type input java.sql.Types
     * @return GAME data type
     * @see java.sql.Types
     */
    public static PreprocessingStorage.DataType getType(int type) {
        switch(type) {
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
                return PreprocessingStorage.DataType.NOMINAL;
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.BOOLEAN:
            case Types.BIT:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.REAL:
            case Types.SMALLINT:
            case Types.TINYINT:
                return PreprocessingStorage.DataType.NUMERIC;
            default:
                return PreprocessingStorage.DataType.MIXED;
        }
    }
}
