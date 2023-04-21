package org.asam.ods;

/**
 *	Generated from IDL definition of enum "ErrorCode"
 *	@author JacORB IDL compiler 
 */
public final class ErrorCodeHelper {

    private static org.omg.CORBA.TypeCode _type = null;

    public static org.omg.CORBA.TypeCode type() {
        if (_type == null) {
            _type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.ErrorCodeHelper.id(), "ErrorCode", new String[] { "AO_UNKNOWN_ERROR", "AO_ACCESS_DENIED", "AO_BAD_OPERATION", "AO_BAD_PARAMETER", "AO_CONNECT_FAILED", "AO_CONNECT_REFUSED", "AO_CONNECTION_LOST", "AO_DUPLICATE_BASE_ATTRIBUTE", "AO_DUPLICATE_NAME", "AO_DUPLICATE_VALUE", "AO_HAS_INSTANCES", "AO_HAS_REFERENCES", "AO_IMPLEMENTATION_PROBLEM", "AO_INCOMPATIBLE_UNITS", "AO_INVALID_ASAM_PATH", "AO_INVALID_ATTRIBUTE_TYPE", "AO_INVALID_BASE_ELEMENT", "AO_INVALID_BASETYPE", "AO_INVALID_BUILDUP_FUNCTION", "AO_INVALID_COLUMN", "AO_INVALID_COUNT", "AO_INVALID_DATATYPE", "AO_INVALID_ELEMENT", "AO_INVALID_LENGTH", "AO_INVALID_ORDINALNUMBER", "AO_INVALID_RELATION", "AO_INVALID_RELATION_RANGE", "AO_INVALID_RELATION_TYPE", "AO_INVALID_RELATIONSHIP", "AO_INVALID_SET_TYPE", "AO_INVALID_SMATLINK", "AO_INVALID_SUBMATRIX", "AO_IS_BASE_ATTRIBUTE", "AO_IS_BASE_RELATION", "AO_IS_MEASUREMENT_MATRIX", "AO_MATH_ERROR", "AO_MISSING_APPLICATION_ELEMENT", "AO_MISSING_ATTRIBUTE", "AO_MISSING_RELATION", "AO_MISSING_VALUE", "AO_NO_MEMORY", "AO_NO_PATH_TO_ELEMENT", "AO_NOT_FOUND", "AO_NOT_IMPLEMENTED", "AO_NOT_UNIQUE", "AO_OPEN_MODE_NOT_SUPPORTED", "AO_SESSION_LIMIT_REACHED", "AO_SESSION_NOT_ACTIVE", "AO_TRANSACTION_ALREADY_ACTIVE", "AO_TRANSACTION_NOT_ACTIVE", "AO_HAS_BASE_RELATION", "AO_HAS_BASE_ATTRIBUTE", "AO_UNKNOWN_UNIT", "AO_NO_SCALING_COLUMN", "AO_QUERY_TYPE_INVALID", "AO_QUERY_INVALID", "AO_QUERY_PROCESSING_ERROR", "AO_QUERY_TIMEOUT_EXCEEDED", "AO_QUERY_INCOMPLETE", "AO_QUERY_INVALID_RESULTTYPE" });
        }
        return _type;
    }

    public static void insert(final org.omg.CORBA.Any any, final org.asam.ods.ErrorCode s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static org.asam.ods.ErrorCode extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static String id() {
        return "IDL:org/asam/ods/ErrorCode:1.0";
    }

    public static ErrorCode read(final org.omg.CORBA.portable.InputStream in) {
        return ErrorCode.from_int(in.read_long());
    }

    public static void write(final org.omg.CORBA.portable.OutputStream out, final ErrorCode s) {
        out.write_long(s.value());
    }
}
