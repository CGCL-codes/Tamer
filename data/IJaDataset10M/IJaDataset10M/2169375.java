package ch.bbv.mda.cartridges.dotNet;

import java.util.List;
import java.util.ArrayList;
import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The nhibernate type code implements the possible types of
 * nhibernate types.<br/>
 * These types are specified by NHibernate.
 * See <a href="http://nhibernate.sourceforge.net/nh-docs/en/html/single/reference.html#mapping-types-basictypes">
 * here</a> for further information.
 * @author Mikkala Pedersen
 */
public class NHibernateTypeCode extends ReferenceCodeImpl {

    /**
   * Serial version identifier coded as back compatibility date.
   */
    private static final long serialVersionUID = 20050321;

    /**
   * List of all reference codes of this type.
   */
    private static List<NHibernateTypeCode> codes;

    public static final int AUTOMATIC = 0;

    public static final int STRING = 1;

    public static final int BOOLEAN = 2;

    public static final int BYTE = 3;

    public static final int CHAR = 4;

    public static final int INT16 = 5;

    public static final int INT32 = 6;

    public static final int INT64 = 7;

    public static final int SINGLE = 8;

    public static final int DOUBLE = 9;

    public static final int DECIMAL = 10;

    public static final int GUID = 11;

    public static final int DATE_TIME = 12;

    public static final int TIME_SPAN = 13;

    public static final int CULTURE_INFO = 14;

    public static final int TYPE = 15;

    public static final int BINARY = 16;

    static {
        codes = new ArrayList<NHibernateTypeCode>();
        codes.add(new NHibernateTypeCode(AUTOMATIC, 1, "", "The type is affected automatically", new String[] { "" }));
        codes.add(new NHibernateTypeCode(STRING, 1, "String", "Text", new String[] { "string", "String", "System.String", "java.lang.String" }));
        codes.add(new NHibernateTypeCode(BOOLEAN, 2, "Boolean", "true or false", new String[] { "bool", "Boolean", "boolean", "System.Boolean" }));
        codes.add(new NHibernateTypeCode(BYTE, 3, "Byte", "8 bit unsigned integer", new String[] { "byte", "Byte", "System.Byte" }));
        codes.add(new NHibernateTypeCode(CHAR, 4, "Char", "Unicode character", new String[] { "char", "Char", "System.Char" }));
        codes.add(new NHibernateTypeCode(INT16, 5, "Int16", "16 bit signed integer", new String[] { "short", "Int16", "System.Int16", "Short" }));
        codes.add(new NHibernateTypeCode(INT32, 6, "Int32", "32 bit signed integer", new String[] { "int", "Int32", "System.Int32", "Integer" }));
        codes.add(new NHibernateTypeCode(INT64, 7, "Int64", "64 bit signed integer", new String[] { "long", "Int64", "System.Int64", "Long" }));
        codes.add(new NHibernateTypeCode(SINGLE, 8, "Single", "Single-precision floating point number (32 Bit)", new String[] { "float", "Single", "System.Single" }));
        codes.add(new NHibernateTypeCode(DOUBLE, 9, "Double", "Double-precision floating point number (64 Bit)", new String[] { "double", "Double", "System.Double" }));
        codes.add(new NHibernateTypeCode(DECIMAL, 10, "Decimal", "Decimal number", new String[] { "decimal", "Decimal", "System.Decimal" }));
        codes.add(new NHibernateTypeCode(GUID, 11, "Guid", "Globally unique identifier (GUID)", new String[] { "Guid", "System.Guid" }));
        codes.add(new NHibernateTypeCode(DATE_TIME, 12, "DateTime", "Date and time", new String[] { "DateTime", "java.util.Date", "java.util.Time", "System.DateTime", "Date", "Time" }));
        codes.add(new NHibernateTypeCode(TIME_SPAN, 13, "TimeSpan", "Time interval", new String[] { "TimeSpan", "System.TimeSpan" }));
        codes.add(new NHibernateTypeCode(CULTURE_INFO, 14, "CultureInfo", "Information about a specific culture", new String[] { "CultureInfo", "System.Globalization.CultureInfo" }));
        codes.add(new NHibernateTypeCode(TYPE, 15, "Type", "type declaration", new String[] { "Type", "System.Type" }));
        codes.add(new NHibernateTypeCode(BINARY, 16, "Binary", "Binary data", new String[] { "byte[]", "System.Byte[]" }));
    }

    /**
   * Java or .NET types which are converted into the current NHibernate type.
   */
    private String[] types;

    /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   * @param types Java or .NET types which are converted into the NHibernate type.
   */
    public NHibernateTypeCode(int code, int sortOrder, String shortDescription, String description, String[] types) {
        super(code, sortOrder, shortDescription, description, true);
        this.types = types;
    }

    /**
   * Returns the list of all reference codes.
   * @return list of all reference codes.
   */
    public static List<NHibernateTypeCode> getCodes() {
        return codes;
    }

    /**
   * Returns the code instance associated with the given identifier.
   * @param code identifier of the requested code.
   * @return the requested code if found otherwise null.
   */
    public static NHibernateTypeCode getCode(int code) {
        for (NHibernateTypeCode type : codes) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    /**
   * Returns the NHibernate type code associated with the given dotNet or java type.
   * @param dotnetType .NET or java type of the requested NHibernate type.
   * @return the requested NHibernate type code.
   */
    public static NHibernateTypeCode getCode(String dotnetType) {
        for (NHibernateTypeCode code : codes) {
            for (String type : code.types) {
                if (type.equals(dotnetType)) {
                    return code;
                }
            }
        }
        return null;
    }

    /**
   * Guarantees that the returned object is unique in the virtual machine.
   * @return the code read from the stream if defined otherwise null.
   */
    private Object readResolve() {
        return getCode(getCode());
    }
}
