package org.jostraca;

import org.jostraca.util.StandardException;

/** WriterFormatException class. */
public class WriterFormatException extends StandardException {

    public static final Code CODE_writer_format_load = new Code(Code.CAT_system, org.jostraca.MsgCode.unable_to_load_writer_format);

    public static final Code CODE_prop_missing = new Code(Code.CAT_user, org.jostraca.MsgCode.prop_missing);

    public WriterFormatException() {
        super();
    }

    public WriterFormatException(String pMsg) {
        super(pMsg);
    }

    public WriterFormatException(Code pCode, String pMsg) {
        super(pCode, pMsg);
    }

    public WriterFormatException(Code pCode) {
        super(pCode);
    }

    public WriterFormatException(Object pContext) {
        super(pContext);
    }

    public WriterFormatException(String[] pContext) {
        super(pContext);
    }

    public WriterFormatException(Object[] pContext) {
        super(pContext);
    }

    public WriterFormatException(String pMsg, Object pContext) {
        super(pMsg, pContext);
    }

    public WriterFormatException(String pMsg, String[] pContext) {
        super(pMsg, pContext);
    }

    public WriterFormatException(String pMsg, Object[] pContext) {
        super(pMsg, pContext);
    }

    public WriterFormatException(Code pCode, String pMsg, Object pContext) {
        super(pCode, pMsg, pContext);
    }

    public WriterFormatException(Code pCode, String pMsg, String[] pContext) {
        super(pCode, pMsg, pContext);
    }

    public WriterFormatException(Code pCode, String pMsg, Object[] pContext) {
        super(pCode, pMsg, pContext);
    }

    public WriterFormatException(Code pCode, Object pContext) {
        super(pCode, pContext);
    }

    public WriterFormatException(Code pCode, String[] pContext) {
        super(pCode, pContext);
    }

    public WriterFormatException(Code pCode, Object[] pContext) {
        super(pCode, pContext);
    }

    public WriterFormatException(Code pCode, Throwable pContained) {
        super(pCode, pContained);
    }

    public WriterFormatException(Throwable pContained) {
        super(pContained);
    }

    public WriterFormatException(String pMessage, Throwable pContained) {
        super(pMessage, pContained);
    }

    public WriterFormatException(Code pCode, String pMessage, Throwable pContained) {
        super(pCode, pMessage, pContained);
    }

    public WriterFormatException(Code pCode, Object pContext, Throwable pContained) {
        super(pCode, pContext, pContained);
    }

    public WriterFormatException(Code pCode, String[] pContext, Throwable pContained) {
        super(pCode, pContext, pContained);
    }

    public WriterFormatException(Code pCode, Object[] pContext, Throwable pContained) {
        super(pCode, pContext, pContained);
    }

    public static WriterFormatException CODE_writer_format_load(String[] pContext) {
        return new WriterFormatException(CODE_writer_format_load, pContext);
    }

    public static WriterFormatException CODE_writer_format_load(String pName1, String pValue1) {
        return new WriterFormatException(CODE_writer_format_load, new String[] { pName1, pValue1 });
    }

    public static WriterFormatException CODE_writer_format_load(String pName1, String pValue1, String pName2, String pValue2) {
        return new WriterFormatException(CODE_writer_format_load, new String[] { pName1, pValue1, pName2, pValue2 });
    }

    public static WriterFormatException CODE_writer_format_load(String pName1, String pValue1, String pName2, String pValue2, String pName3, String pValue3) {
        return new WriterFormatException(CODE_writer_format_load, new String[] { pName1, pValue1, pName2, pValue2, pName3, pValue3 });
    }

    public static WriterFormatException CODE_writer_format_load(Object pContext) {
        return new WriterFormatException(CODE_writer_format_load, pContext);
    }

    public static WriterFormatException CODE_writer_format_load(Object pContext, Throwable pContained) {
        return new WriterFormatException(CODE_writer_format_load, pContext, pContained);
    }

    public static WriterFormatException CODE_prop_missing(String[] pContext) {
        return new WriterFormatException(CODE_prop_missing, pContext);
    }

    public static WriterFormatException CODE_prop_missing(String pName1, String pValue1) {
        return new WriterFormatException(CODE_prop_missing, new String[] { pName1, pValue1 });
    }

    public static WriterFormatException CODE_prop_missing(String pName1, String pValue1, String pName2, String pValue2) {
        return new WriterFormatException(CODE_prop_missing, new String[] { pName1, pValue1, pName2, pValue2 });
    }

    public static WriterFormatException CODE_prop_missing(String pName1, String pValue1, String pName2, String pValue2, String pName3, String pValue3) {
        return new WriterFormatException(CODE_prop_missing, new String[] { pName1, pValue1, pName2, pValue2, pName3, pValue3 });
    }

    public static WriterFormatException CODE_prop_missing(Object pContext) {
        return new WriterFormatException(CODE_prop_missing, pContext);
    }

    public static WriterFormatException CODE_prop_missing(Object pContext, Throwable pContained) {
        return new WriterFormatException(CODE_prop_missing, pContext, pContained);
    }

    public String getPackage() {
        return "org.jostraca";
    }

    /** Code enumeration class for WriterFormatException */
    public static class Code extends StandardException.Code {

        protected Code(Cat pWriterFormat, String pKey) {
            super(pWriterFormat, pKey);
        }
    }
}
