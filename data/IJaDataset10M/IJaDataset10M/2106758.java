package ch.bbv.mda.cartridges.dotNet;

import java.util.ArrayList;
import java.util.List;
import ch.bbv.dog.reference.ReferenceCodeImpl;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.cartridges.BoConstants;

/**
 * The MySql type code defines all MySql data types supported by the pmMDA.NET framework.<br/>
 * The MySql type code also allows to convert a java or .NET type to a SQL type.
 * @author Mikkala Pedersen
 */
public class MySqlTypeCode extends ReferenceCodeImpl {

    /**
   * Serial version identifier coded as back compatibility date.
   */
    private static final long serialVersionUID = 20050321;

    /**
   * List of all reference codes of this type.
   */
    private static List<MySqlTypeCode> codes;

    /**
   * 8 bit unsigned integer
   */
    public static final int TINYINT = 1;

    /**
   * 16 bit signed integer
   */
    public static final int SMALLINT = 2;

    /**
   * 32 bit signed integer
   */
    public static final int INTEGER = 3;

    /**
   * 64 bit signed integer
   */
    public static final int BIGINT = 4;

    /**
   * Single-precision floating point number (32 Bit)
   */
    public static final int FLOAT = 5;

    /**
   * Double-precision floating point number (64 Bit)
   */
    public static final int DOUBLE = 6;

    /**
   * Decimal number (precision=32, scale=2)
   */
    public static final int DECIMAL = 7;

    /**
   * Date and time
   */
    public static final int DATETIME = 8;

    /**
   * Text
   */
    public static final int VARCHAR = 9;

    /**
   * Boolean (TRUE or FALSE)
   */
    public static final int BOOLEAN = 10;

    /**
   * BLOB (Binary data)
   */
    public static final int BLOB = 11;

    static {
        codes = new ArrayList<MySqlTypeCode>();
        codes.add(new MySqlTypeCode(TINYINT, 1, "TINYINT", "8 bit unsigned integer", new String[] { "byte", "Byte", "System.Byte" }));
        codes.add(new MySqlTypeCode(SMALLINT, 2, "SMALLINT", "16 bit signed integer", new String[] { "short", "Int16", "System.Int16", "Short" }));
        codes.add(new MySqlTypeCode(INTEGER, 3, "INTEGER", "32 bit signed integer", new String[] { "int", "Int32", "System.Int32", "Integer" }));
        codes.add(new MySqlTypeCode(BIGINT, 4, "BIGINT", "64 bit signed integer", new String[] { "long", "Int64", "System.Int64", "Long" }));
        codes.add(new MySqlTypeCode(FLOAT, 5, "FLOAT", "Single-precision floating point number (32 Bit)", new String[] { "float", "Single", "System.Single" }));
        codes.add(new MySqlTypeCode(DOUBLE, 6, "DOUBLE", "Double-precision floating point number (64 Bit)", new String[] { "double", "Double", "System.Double" }));
        codes.add(new MySqlTypeCode(DECIMAL, 7, "DECIMAL(32,2)", "Decimal number (precision=32, scale=2)", new String[] { "decimal", "Decimal", "System.Decimal", "BigDecimal", "java.math.BigDecimal" }));
        codes.add(new MySqlTypeCode(DATETIME, 8, "DATETIME", "Date and time", new String[] { "DateTime", "System.DateTime", "Date", "Time", "java.util.Date", "java.util.Time" }));
        codes.add(new MySqlTypeCode(VARCHAR, 9, "VARCHAR", "Text", new String[] { "string", "java.lang.String", "String", "System.String" }));
        codes.add(new MySqlTypeCode(BOOLEAN, 10, "BOOLEAN", "Boolean (true or false)", new String[] { "bool", "boolean", "Boolean", "java.lang.Boolean", "System.Boolean" }));
        codes.add(new MySqlTypeCode(BLOB, 11, "BLOB", "BLOB (Binary data)", new String[] { "Byte[]", "java.lang.Byte[]", "System.Byte[]" }));
    }

    /**
   * Java or .NET types which are converted into the current MySql type.
   */
    private String[] types;

    /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   * @param types Java or .NET types which are converted into the MySql type.
   */
    public MySqlTypeCode(int code, int sortOrder, String shortDescription, String description, String[] types) {
        super(code, sortOrder, shortDescription, description, true);
        this.types = types;
    }

    /**
   * Returns the list of all reference codes.
   * @return list of all reference codes.
   */
    public static List<MySqlTypeCode> getCodes() {
        return codes;
    }

    /**
   * Returns the code instance associated with the given identifier.
   * @param code identifier of the requested code.
   * @return the requested code if found otherwise null.
   */
    public static MySqlTypeCode getCode(int code) {
        for (MySqlTypeCode type : codes) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    /**
   * Returns the MySql type code associated with the given dotNet or java type.
   * @param dotnetType .NET or java type of the requested MySql type.
   * @return the requested MySql type code.
   */
    public static MySqlTypeCode getCode(String dotnetType) {
        for (MySqlTypeCode code : codes) {
            for (String type : code.types) {
                if (type.equals(dotnetType)) {
                    return code;
                }
            }
        }
        return null;
    }

    public static MySqlTypeCode getCode(MetaClass clazz) {
        if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
            return null;
        }
        return getCode(clazz.getName());
    }

    /**
   * Guarantees that the returned object is unique in the virtual machine.
   * @return the code read from the stream if defined otherwise null.
   */
    private Object readResolve() {
        return getCode(getCode());
    }
}
