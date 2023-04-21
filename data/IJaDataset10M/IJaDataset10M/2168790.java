package org.openexi.schema;

/**
 * EXISchemaConst lists the constant serial numbers given to the built-in schema
 * types in the compiled schema.
 * Note that it is the serial numbers that are constant. Node numbers are
 * usually different.
 */
public final class EXISchemaConst {

    public static final byte UNTYPED = -1;

    public static final byte ANY_TYPE = 0;

    public static final byte ANY_SIMPLE_TYPE = 1;

    public static final byte STRING_TYPE = 2;

    public static final byte BOOLEAN_TYPE = 3;

    public static final byte DECIMAL_TYPE = 4;

    public static final byte FLOAT_TYPE = 5;

    public static final byte DOUBLE_TYPE = 6;

    public static final byte DURATION_TYPE = 7;

    public static final byte DATETIME_TYPE = 8;

    public static final byte TIME_TYPE = 9;

    public static final byte DATE_TYPE = 10;

    public static final byte G_YEARMONTH_TYPE = 11;

    public static final byte G_YEAR_TYPE = 12;

    public static final byte G_MONTHDAY_TYPE = 13;

    public static final byte G_DAY_TYPE = 14;

    public static final byte G_MONTH_TYPE = 15;

    public static final byte HEXBINARY_TYPE = 16;

    public static final byte BASE64BINARY_TYPE = 17;

    public static final byte ANYURI_TYPE = 18;

    public static final byte QNAME_TYPE = 19;

    public static final byte NOTATION_TYPE = 20;

    static final byte N_PRIMITIVE_TYPES = 19;

    public static final byte INTEGER_TYPE = 21;

    public static final byte NON_NEGATIVE_INTEGER_TYPE = 22;

    public static final byte UNSIGNED_LONG_TYPE = 23;

    public static final byte POSITIVE_INTEGER_TYPE = 24;

    public static final byte NON_POSITIVE_INTEGER_TYPE = 25;

    public static final byte NEGATIVE_INTEGER_TYPE = 26;

    public static final byte INT_TYPE = 27;

    public static final byte SHORT_TYPE = 28;

    public static final byte BYTE_TYPE = 29;

    public static final byte UNSIGNED_SHORT_TYPE = 30;

    public static final byte UNSIGNED_BYTE_TYPE = 31;

    public static final byte LONG_TYPE = 32;

    public static final byte UNSIGNED_INT_TYPE = 33;

    public static final byte NORMALIZED_STRING_TYPE = 34;

    public static final byte TOKEN_TYPE = 35;

    public static final byte LANGUAGE_TYPE = 36;

    public static final byte NAME_TYPE = 37;

    public static final byte NCNAME_TYPE = 38;

    public static final byte NMTOKEN_TYPE = 39;

    public static final byte ENTITY_TYPE = 40;

    public static final byte IDREF_TYPE = 41;

    public static final byte ID_TYPE = 42;

    public static final byte ENTITIES_TYPE = 43;

    public static final byte IDREFS_TYPE = 44;

    public static final byte NMTOKENS_TYPE = 45;

    public static final byte N_BUILTIN_TYPES = 46;
}
