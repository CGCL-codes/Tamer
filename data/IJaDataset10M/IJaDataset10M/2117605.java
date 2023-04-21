package com.daffodilwoods.daffodildb.utils;

import java.math.*;
import java.sql.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.utils.byteconverter.*;
import com.daffodilwoods.daffodildb.utils.comparator.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.utils.comparator.SuperComparator;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.daffodildb.utils.FieldBaseConverter;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.DBlobUpdatable;
import java.text.Collator;
import com.daffodilwoods.database.utility.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;

public class FieldUtility {

    public static BufferRange NULLBUFFERRANGE = new BufferRange(true);

    public static FieldBase NULLFIELDBASE = new FieldLiteral(NULLBUFFERRANGE, -1);

    public static final FieldBase Int1 = new FieldLiteral(new Integer(1), Datatype.INTEGER);

    public static final FieldBase Int0 = new FieldLiteral(new Integer(0), Datatype.INTEGER);

    public static final FieldBase Int_1 = new FieldLiteral(new Integer(-1), Datatype.INTEGER);

    public static FieldBase convertToAppropriateType(FieldBase field, int datatype, int size, Collator collator) throws DException {
        return convertToAppropriateType(field, datatype, size, -1, collator);
    }

    public static FieldBase convertToAppropriateType(FieldBase field, int datatype, int size, int scale, Collator collator) throws DException {
        int fieldType = field.getDatatype();
        if (fieldType == Datatypes.DEFAULT_DATATYPE) return field;
        if (field.isNull()) {
            FieldBase convertedField = getField(datatype, null, collator);
            convertedField.setBufferRange(NULLBUFFERRANGE);
            return convertedField;
        }
        if (fieldType == -1) {
            try {
                field.setDatatype(-1);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            fieldType = field.getDatatype();
        }
        FieldBase convertedField;
        switch(datatype) {
            case Datatype.BIGDECIMAL:
            case Datatype.DEC:
            case Datatype.DECIMAL:
            case Datatype.NUMERIC:
                switch(fieldType) {
                    case Datatype.BOOLEAN:
                        if (scale == 0) return new FieldBigDecimal(new BufferRange(Boolean.TRUE.equals(field.getObject()) ? "1".getBytes() : "0".getBytes()), datatype);
                        convertedField = new FieldBigDecimal(new BufferRange(Boolean.TRUE.equals(field.getObject()) ? "1.0".getBytes() : "0.0".getBytes()), datatype);
                        return convertedField;
                    case Datatype.BIGDECIMAL:
                    case Datatype.DEC:
                    case Datatype.DECIMAL:
                    case Datatype.NUMERIC:
                        scale = scale < 0 ? 0 : scale;
                        checkPrecisionScale(field.getObject().toString(), size, scale);
                        BigDecimal bd = ((BigDecimal) field.getObject());
                        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
                        convertedField = new FieldBigDecimal(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes(bd)) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.LONG:
                    case Datatype.BIGINT:
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.FLOAT:
                    case Datatype.REAL:
                    case Datatype.TINYINT:
                    case Datatype.BYTE:
                    case Datatype.INT:
                    case Datatype.INTEGER:
                        scale = scale < 0 ? 0 : scale;
                        checkPrecisionScale(field.getObject().toString(), size, scale);
                        bd = new BigDecimal((field.getObject().toString()));
                        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
                        convertedField = new FieldBigDecimal(new BufferRange(CCzufDpowfsufs.getBytes(bd)), datatype);
                        return convertedField;
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                    case Datatype.VARCHAR:
                    default:
                        try {
                            scale = scale < 0 ? 0 : scale;
                            checkPrecisionScale(field.getObject().toString(), size, scale);
                            bd = new BigDecimal((field.getObject().toString()));
                            bd = bd.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
                            convertedField = new FieldBigDecimal(new BufferRange(CCzufDpowfsufs.getBytes(bd)), datatype);
                            return convertedField;
                        } catch (RuntimeException ex) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.BIGINT:
            case Datatype.LONG:
                switch(fieldType) {
                    case Datatype.BIGINT:
                    case Datatype.LONG:
                        convertedField = new FieldLong(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Long) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.BIGDECIMAL:
                    case Datatype.DEC:
                    case Datatype.DECIMAL:
                    case Datatype.NUMERIC:
                        TypeValidityHandler.checkLongWithBigDecimal(field);
                        convertedField = new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(((BigDecimal) field.getObject()).longValue())), datatype);
                        return convertedField;
                    case Datatype.BOOLEAN:
                        convertedField = new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(Boolean.TRUE.equals(field.getObject()) ? 1 : 0)), datatype);
                        return convertedField;
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.FLOAT:
                        TypeValidityHandler.checkLongWithDouble(field);
                        convertedField = new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).longValue())), datatype);
                        return convertedField;
                    case Datatype.REAL:
                        TypeValidityHandler.checkLongWithFloat(field);
                        convertedField = new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).longValue())), datatype);
                        return convertedField;
                    case Datatype.INT:
                    case Datatype.INTEGER:
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                    case Datatype.TINYINT:
                    case Datatype.BYTE:
                        convertedField = new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).longValue())), datatype);
                        return convertedField;
                    default:
                        try {
                            convertedField = new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(new Long(field.getObject().toString()))), datatype);
                            return convertedField;
                        } catch (RuntimeException re) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.BOOLEAN:
                switch(fieldType) {
                    case Datatype.BOOLEAN:
                        convertedField = new FieldBoolean(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Boolean) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    default:
                        try {
                            String ss = field.getObject().toString();
                            if (ss.equals("1")) {
                                convertedField = new FieldBoolean(new BufferRange(CCzufDpowfsufs.getBytes(new Boolean(true))), datatype);
                                return convertedField;
                            } else {
                                convertedField = new FieldBoolean(new BufferRange(CCzufDpowfsufs.getBytes(new Boolean(ss))), datatype);
                                return convertedField;
                            }
                        } catch (RuntimeException re) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.TINYINT:
            case Datatype.BYTE:
                switch(fieldType) {
                    case Datatype.BYTE:
                    case Datatype.TINYINT:
                        convertedField = new FieldByte(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Byte) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.BOOLEAN:
                        convertedField = new FieldByte(new BufferRange(Boolean.TRUE.equals(field.getObject()) ? new byte[] { 1 } : new byte[] { 0 }), datatype);
                        return convertedField;
                    case Datatype.INT:
                    case Datatype.INTEGER:
                    case Datatype.LONG:
                    case Datatype.BIGINT:
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                        TypeValidityHandler.checkByteWithNonDecimal(field);
                        convertedField = new FieldByte(new BufferRange(new byte[] { ((Number) field.getObject()).byteValue() }), datatype);
                        return convertedField;
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.FLOAT:
                        TypeValidityHandler.checkByteWithDouble(field);
                        convertedField = new FieldByte(new BufferRange(new byte[] { ((Number) field.getObject()).byteValue() }), datatype);
                        return convertedField;
                    case Datatype.REAL:
                        TypeValidityHandler.checkByteWithFloat(field);
                        convertedField = new FieldByte(new BufferRange(new byte[] { ((Number) field.getObject()).byteValue() }), datatype);
                        return convertedField;
                    default:
                        try {
                            convertedField = new FieldByte(new BufferRange(CCzufDpowfsufs.getBytes(new Byte(field.getObject().toString()))), datatype);
                            return convertedField;
                        } catch (RuntimeException re) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.BINARY:
            case Datatype.VARBINARY:
                switch(fieldType) {
                    case Datatype.BINARY:
                    case Datatype.VARBINARY:
                        try {
                            if (field.getBufferRange().getLength() > size) {
                                throw new SizeMisMatchException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                            }
                        } catch (NullPointerException ex) {
                            throw ex;
                        }
                        byte[] bytes1 = (byte[]) field.getObject();
                        if (datatype == Datatype.BINARY) {
                            byte[] tmp1 = new byte[size];
                            Arrays.fill(tmp1, (byte) 0);
                            System.arraycopy(bytes1, 0, tmp1, (size - bytes1.length), bytes1.length);
                            bytes1 = tmp1;
                        }
                        convertedField = new FieldBinary(new BufferRange(bytes1), datatype);
                        return convertedField;
                    case Datatype.BLOB:
                    case Datatype.BINARYLARGEOBJECT:
                    case Datatype.LONGVARBINARY:
                    case Datatype.VARCHAR:
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                        try {
                            if (field.getBufferRange().getLength() > size) {
                                throw new SizeMisMatchException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                            }
                        } catch (NullPointerException ex) {
                            throw ex;
                        }
                        byte[] bytes = field.getBytes();
                        if (datatype == Datatype.BINARY) {
                            byte[] tmp = new byte[size];
                            Arrays.fill(tmp, (byte) 0);
                            System.arraycopy(bytes, 0, tmp, (size - bytes.length), bytes.length);
                            bytes = tmp;
                        }
                        convertedField = new FieldBinary(new BufferRange(bytes), datatype);
                        return convertedField;
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                    case Datatype.TINYINT:
                    case Datatype.BYTE:
                    case Datatype.INT:
                    case Datatype.INTEGER:
                        convertedField = convertNonDecimalToBinary(field, size, fieldType);
                        return convertedField;
                    default:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                }
            case Datatype.CHARACTERLARGEOBJECT:
            case Datatype.CHARLARGEOBJECT:
            case Datatype.CLOB:
            case Datatype.LONGVARCHAR:
                switch(fieldType) {
                    case Datatype.CLOB:
                        try {
                            return new DClobUpdatable(((Clob) field.getObject()).getAsciiStream());
                        } catch (SQLException ex) {
                            throw new DException("DSE0", new Object[] { ex.getMessage() });
                        }
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                    case Datatype.VARCHAR:
                        return new DClobUpdatable((String) field.getObject());
                    case Datatype.BINARY:
                    case Datatype.VARBINARY:
                        return new DClobUpdatable((byte[]) field.getObject());
                    default:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                }
            case Datatype.BLOB:
            case Datatype.BINARYLARGEOBJECT:
            case Datatype.LONGVARBINARY:
                switch(fieldType) {
                    case Datatype.BLOB:
                        try {
                            return new DBlobUpdatable(((Blob) field.getObject()).getBinaryStream());
                        } catch (SQLException ex) {
                            throw new DException("DSE0", new Object[] { ex.getMessage() });
                        }
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                    case Datatype.VARCHAR:
                        return new DBlobUpdatable((String) field.getObject());
                    case Datatype.BINARY:
                    case Datatype.VARBINARY:
                        return new DBlobUpdatable((byte[]) field.getObject());
                    default:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                }
            case Datatype.CHAR:
            case Datatype.CHARACTER:
            case Datatype.CHARACTERVARYING:
            case Datatype.CHARVARYING:
            case Datatype.VARCHAR:
                switch(fieldType) {
                    case Datatype.BINARY:
                    case Datatype.VARBINARY:
                    case Datatype.BLOB:
                    case Datatype.BINARYLARGEOBJECT:
                    case Datatype.LONGVARBINARY:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                    case Datatype.CHARACTERLARGEOBJECT:
                    case Datatype.CHARLARGEOBJECT:
                    case Datatype.CLOB:
                    case Datatype.LONGVARCHAR:
                        if (field.getLength() > size) {
                            ;
                            throw new SizeMisMatchException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                        String ss1 = null;
                        try {
                            ss1 = ((Clob) field.getObject()).getSubString(1, field.getLength());
                        } catch (SQLException ex) {
                            throw new DException("DSE0", new Object[] { ex.getMessage() });
                        }
                        convertedField = new FieldString(new BufferRange(CCzufDpowfsufs.getBytes(ss1, datatype == Datatype.CHAR || datatype == Datatype.CHARACTER ? size : -1, collator != null)), datatype, collator);
                        return convertedField;
                    default:
                        String ss = field.getObject().toString();
                        if (ss.length() > size) {
                            ;
                            throw new SizeMisMatchException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                        convertedField = new FieldString(new BufferRange(CCzufDpowfsufs.getBytes(ss, datatype == Datatype.CHAR || datatype == Datatype.CHARACTER ? size : -1, collator != null)), datatype, collator);
                        return convertedField;
                }
            case Datatype.BIT:
            case Datatype.BITVARYING:
                switch(fieldType) {
                    case Datatype.BOOLEAN:
                        String s = field.getObject().toString().trim();
                        s = (s.equalsIgnoreCase("0") || s.equalsIgnoreCase("false")) ? "0" : "1";
                        if (s.length() > size) throw new SizeMisMatchException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        convertedField = new FieldString(new BufferRange(CCzufDpowfsufs.getBytes(s, datatype == Datatype.BIT ? size : -1, false)), datatype, collator);
                        return convertedField;
                    default:
                        try {
                            String ss = field.getObject().toString().trim();
                            ss = (ss.equalsIgnoreCase("0") || ss.equalsIgnoreCase("false")) ? "0" : (ss.equalsIgnoreCase("1") || ss.equalsIgnoreCase("true")) ? "1" : ss;
                            if (ss.length() > size) {
                                ;
                                throw new SizeMisMatchException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                            }
                            for (int i = 0; i < ss.length(); i++) {
                                if (!(ss.charAt(i) == '0' || ss.charAt(i) == '1')) {
                                    throw new DException("DSE773", null);
                                }
                            }
                            convertedField = new FieldString(new BufferRange(CCzufDpowfsufs.getBytes(ss, datatype == Datatype.BIT ? size : -1, false)), datatype, collator);
                            return convertedField;
                        } catch (RuntimeException re) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.DOUBLE:
            case Datatype.DOUBLEPRECISION:
            case Datatype.FLOAT:
                switch(fieldType) {
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.FLOAT:
                        convertedField = new FieldDouble(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Double) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.BIGDECIMAL:
                        TypeValidityHandler.checkDoubleWithBigDecimal(field);
                        convertedField = new FieldDouble(new BufferRange(CCzufDpowfsufs.getBytes(((BigDecimal) field.getObject()).doubleValue())), datatype);
                        return convertedField;
                    case Datatype.BOOLEAN:
                        convertedField = new FieldDouble(new BufferRange(CCzufDpowfsufs.getBytes(Boolean.TRUE.equals(field.getObject()) ? 1d : 0d)), datatype);
                        return convertedField;
                    case Datatype.INT:
                    case Datatype.INTEGER:
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                    case Datatype.BIGINT:
                    case Datatype.LONG:
                    case Datatype.TINYINT:
                    case Datatype.BYTE:
                        convertedField = new FieldDouble(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).doubleValue())), Datatype.DOUBLE);
                        return convertedField;
                    case Datatype.REAL:
                        TypeValidityHandler.checkDoubleWithFloat(field);
                        convertedField = new FieldDouble(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).doubleValue())), Datatype.DOUBLE);
                        return convertedField;
                    default:
                        try {
                            convertedField = new FieldDouble(new BufferRange(CCzufDpowfsufs.getBytes(new Double(field.getObject().toString()))), datatype);
                            return convertedField;
                        } catch (RuntimeException re) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.INT:
            case Datatype.INTEGER:
                switch(fieldType) {
                    case Datatype.INT:
                    case Datatype.INTEGER:
                        convertedField = new FieldInteger(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Integer) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.BIGDECIMAL:
                    case Datatype.DEC:
                    case Datatype.DECIMAL:
                    case Datatype.NUMERIC:
                        TypeValidityHandler.checkIntegerWithBigDecimal(field);
                        convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(((BigDecimal) field.getObject()).intValue())), datatype);
                        return convertedField;
                    case Datatype.BOOLEAN:
                        convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(Boolean.TRUE.equals(field.getObject()) ? 1 : 0)), datatype);
                        return convertedField;
                    case Datatype.BIGINT:
                    case Datatype.LONG:
                        TypeValidityHandler.checkIntegerWithNonDecimal(field);
                        convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).intValue())), datatype);
                        return convertedField;
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.FLOAT:
                        TypeValidityHandler.checkIntegerWithDouble(field);
                        convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).intValue())), datatype);
                        return convertedField;
                    case Datatype.REAL:
                        TypeValidityHandler.checkIntegerWithFloat(field);
                        convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).intValue())), datatype);
                        return convertedField;
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                    case Datatype.TINYINT:
                    case Datatype.BYTE:
                        convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).intValue())), datatype);
                        return convertedField;
                    default:
                        try {
                            convertedField = new FieldInteger(new BufferRange(CCzufDpowfsufs.getBytes(new Integer(field.getObject().toString()))), datatype);
                            return convertedField;
                        } catch (RuntimeException re) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.REAL:
                switch(fieldType) {
                    case Datatype.REAL:
                        convertedField = new FieldReal(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Float) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.BIGDECIMAL:
                    case Datatype.DEC:
                    case Datatype.DECIMAL:
                    case Datatype.NUMERIC:
                        TypeValidityHandler.checkFloatWithBigDecimal(field);
                        convertedField = new FieldReal(new BufferRange(CCzufDpowfsufs.getBytes(((BigDecimal) field.getObject()).floatValue())), datatype);
                        return convertedField;
                    case Datatype.BOOLEAN:
                        convertedField = new FieldReal(new BufferRange(CCzufDpowfsufs.getBytes(Boolean.TRUE.equals(field.getObject()) ? 1.0f : 0.0f)), datatype);
                        return convertedField;
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                    case Datatype.BIGINT:
                    case Datatype.LONG:
                    case Datatype.INT:
                    case Datatype.INTEGER:
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.TINYINT:
                    case Datatype.BYTE:
                        convertedField = new FieldReal(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).floatValue())), datatype);
                        return convertedField;
                    case Datatype.FLOAT:
                        TypeValidityHandler.checkFloatWithDouble(field);
                        convertedField = new FieldReal(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).floatValue())), datatype);
                        return convertedField;
                    default:
                        try {
                            convertedField = new FieldReal(new BufferRange(CCzufDpowfsufs.getBytes(new Float(field.getObject().toString()))), datatype);
                            return convertedField;
                        } catch (RuntimeException e) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.SHORT:
            case Datatype.SMALLINT:
                switch(fieldType) {
                    case Datatype.BYTE:
                    case Datatype.TINYINT:
                        convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).shortValue())), datatype);
                        return convertedField;
                    case Datatype.SHORT:
                    case Datatype.SMALLINT:
                        convertedField = new FieldShort(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Short) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.BIGDECIMAL:
                        TypeValidityHandler.checkShortWithBigDecimal(field);
                        convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(((BigDecimal) field.getObject()).shortValue())), datatype);
                        return convertedField;
                    case Datatype.BOOLEAN:
                        convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(Boolean.TRUE.equals(field.getObject()) ? new Short("1") : new Short("0"))), datatype);
                        return convertedField;
                    case Datatype.BIGINT:
                    case Datatype.LONG:
                    case Datatype.INT:
                    case Datatype.INTEGER:
                        TypeValidityHandler.checkShortWithNonDecimal(field);
                        convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).shortValue())), datatype);
                        return convertedField;
                    case Datatype.DOUBLE:
                    case Datatype.DOUBLEPRECISION:
                    case Datatype.FLOAT:
                        TypeValidityHandler.checkShortWithDouble(field);
                        convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).shortValue())), datatype);
                        return convertedField;
                    case Datatype.REAL:
                        TypeValidityHandler.checkShortWithFloat(field);
                        convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(((Number) field.getObject()).shortValue())), datatype);
                        return convertedField;
                    default:
                        try {
                            convertedField = new FieldShort(new BufferRange(CCzufDpowfsufs.getBytes(new Short(field.getObject().toString()))), datatype);
                            return convertedField;
                        } catch (RuntimeException e) {
                            throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                        }
                }
            case Datatype.DATE:
                switch(fieldType) {
                    case Datatype.DATE:
                        convertedField = new FieldDate(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Date) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.TIMESTAMP:
                        convertedField = new FieldDate(new BufferRange(CCzufDpowfsufs.getBytes(TypeValidityHandler.getDBDate((Timestamp) field.getObject()))), datatype);
                        return convertedField;
                    case Datatype.TIME:
                        throw new DException("DSE5547", new Object[] { "Time", "Date" });
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                    case Datatype.VARCHAR:
                        convertedField = new FieldDate(new BufferRange(CCzufDpowfsufs.getBytes(TypeValidityHandler.getDBDateFromDateTimestamp((String) field.getObject()))), datatype);
                        return convertedField;
                    default:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                }
            case Datatype.TIME:
                switch(fieldType) {
                    case Datatype.TIME:
                        convertedField = new FieldTime(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Time) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.TIMESTAMP:
                        convertedField = new FieldTime(new BufferRange(CCzufDpowfsufs.getBytes(TypeValidityHandler.getTime((Timestamp) field.getObject()))), datatype);
                        return convertedField;
                    case Datatype.DATE:
                        throw new DException("DSE5547", new Object[] { "Date", "Time" });
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                    case Datatype.VARCHAR:
                        convertedField = new FieldTime(new BufferRange(CCzufDpowfsufs.getBytes(TypeValidityHandler.getTimeFromTimeTimeStamp((String) field.getObject()))), datatype);
                        return convertedField;
                    default:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                }
            case Datatype.TIMESTAMP:
                switch(fieldType) {
                    case Datatype.TIMESTAMP:
                        convertedField = new FieldTimeStamp(field.getBufferRange() == null ? new BufferRange(CCzufDpowfsufs.getBytes((Timestamp) field.getObject())) : field.getBufferRange(), datatype);
                        return convertedField;
                    case Datatype.TIME:
                        throw new DException("DSE5547", new Object[] { "Time", "Timestamp" });
                    case Datatype.DATE:
                        convertedField = new FieldTimeStamp(new BufferRange(CCzufDpowfsufs.getBytes(new Timestamp(((Date) field.getObject()).getTime()))), datatype);
                        return convertedField;
                    case Datatype.CHAR:
                    case Datatype.CHARACTER:
                    case Datatype.CHARACTERVARYING:
                    case Datatype.CHARVARYING:
                    case Datatype.VARCHAR:
                        convertedField = new FieldTimeStamp(new BufferRange(CCzufDpowfsufs.getBytes(TypeValidityHandler.getTimeStampFromTimestampDate((String) field.getObject()))), datatype);
                        return convertedField;
                    default:
                        throw new DException("DSE773", new Object[] { field.getObject(), StaticClass.getDataTypeName(datatype) });
                }
            case Datatype.ARRAY:
            case Datatype.MODULE:
            case Datatype.RECORD:
            case Datatype.RECORDSET:
            case Datatype.REF:
            case Datatype.STRUCT:
            case Datatype.SUM:
            case Datatype.TIME_WITH_TIMEZONE:
            case Datatype.TIMESTAMP_WITH_TIMEZONE:
                throw new DException("DSE1133", new Object[] { new Integer(datatype) });
            default:
                throw new DException("DSE1021", new Object[] { new Integer(datatype) });
        }
    }

    public static FieldBase[] convertToAppropriateType(FieldBase[] fields, int[] datatype, int[] size, int[] scale, Collator collator) throws DException {
        if (fields == null) return null;
        FieldBase[] fieldsToReturn = new FieldBase[fields.length];
        for (int i = 0; i < fields.length; i++) fieldsToReturn[i] = convertToAppropriateType(fields[i], datatype[i], size[i], scale[i], collator);
        return fieldsToReturn;
    }

    public static FieldBase[] convertToAppropriateType(Object[] fields, int[] datatype, int[] size, int[] scale, Collator collator) throws DException {
        if (fields == null) return null;
        FieldBase[] fieldsToReturn = new FieldBase[fields.length];
        for (int i = 0; i < fields.length; i++) fieldsToReturn[i] = convertToAppropriateType((FieldBase) fields[i], datatype[i], size[i], scale[i], collator);
        return fieldsToReturn;
    }

    public static FieldBase getField(int datatype, Object values, Collator collator) throws DException {
        switch(datatype) {
            case Datatype.INT:
            case Datatype.INTEGER:
                return FieldBaseConverter.getFieldBase((Integer) values, datatype);
            case Datatype.LONG:
            case Datatype.BIGINT:
                return FieldBaseConverter.getFieldBase((Long) values, datatype);
            case Datatype.DOUBLE:
            case Datatype.DOUBLEPRECISION:
            case Datatype.FLOAT:
                return FieldBaseConverter.getFieldBase((Double) values, datatype);
            case Datatype.REAL:
                return FieldBaseConverter.getFieldBase((Float) values, datatype);
            case Datatype.SMALLINT:
            case Datatype.SHORT:
                return FieldBaseConverter.getFieldBase((Short) values, datatype);
            case Datatype.TINYINT:
            case Datatype.BYTE:
                return FieldBaseConverter.getFieldBase((Byte) values, datatype);
            case Datatype.BINARY:
            case Datatype.VARBINARY:
                return FieldBaseConverter.getFieldBase((byte[]) values, datatype);
            case Datatype.DEC:
            case Datatype.DECIMAL:
            case Datatype.NUMERIC:
            case Datatype.BIGDECIMAL:
                return FieldBaseConverter.getFieldBase((BigDecimal) values, datatype);
            case Datatype.VARCHAR:
            case Datatype.CHAR:
            case Datatype.CHARACTER:
            case Datatype.CHARACTERVARYING:
            case Datatype.CHARVARYING:
                return FieldBaseConverter.getFieldBase((String) values, datatype, collator);
            case Datatype.BIT:
            case Datatype.BITVARYING:
                return FieldBaseConverter.getFieldBase((String) values, datatype, null);
            case Datatype.BLOB:
            case Datatype.BINARYLARGEOBJECT:
            case Datatype.LONGVARBINARY:
                return values instanceof DBlobUpdatable ? values == null ? new DBlobUpdatable(true) : (FieldBase) values : FieldBaseConverter.getFieldBase((Blob) values);
            case Datatype.DATE:
                return FieldBaseConverter.getFieldBase((java.sql.Date) values);
            case Datatype.TIMESTAMP:
                return FieldBaseConverter.getFieldBase((java.sql.Timestamp) values);
            case Datatype.TIME:
                return FieldBaseConverter.getFieldBase((java.sql.Time) values);
            case Datatype.BOOLEAN:
                return FieldBaseConverter.getFieldBase((Boolean) values);
            case Datatype.CHARACTERLARGEOBJECT:
            case Datatype.CHARLARGEOBJECT:
            case Datatype.CLOB:
            case Datatype.LONGVARCHAR:
                return values instanceof DClobUpdatable ? values == null ? new DClobUpdatable(true) : (FieldBase) values : FieldBaseConverter.getFieldBase((Clob) values);
            default:
                throw new DException("DSE514", new Object[] { StaticClass.getDataTypeName(datatype) });
        }
    }

    public static FieldBase getField(int datatype, Object values) throws DException {
        switch(datatype) {
            case Datatype.INT:
            case Datatype.INTEGER:
                return FieldBaseConverter.getFieldBase((Integer) values, datatype);
            case Datatype.LONG:
            case Datatype.BIGINT:
                return FieldBaseConverter.getFieldBase((Long) values, datatype);
            case Datatype.DOUBLE:
            case Datatype.DOUBLEPRECISION:
            case Datatype.FLOAT:
                return FieldBaseConverter.getFieldBase((Double) values, datatype);
            case Datatype.REAL:
                return FieldBaseConverter.getFieldBase((Float) values, datatype);
            case Datatype.SMALLINT:
            case Datatype.SHORT:
                return FieldBaseConverter.getFieldBase((Short) values, datatype);
            case Datatype.TINYINT:
            case Datatype.BYTE:
                return FieldBaseConverter.getFieldBase((Byte) values, datatype);
            case Datatype.BINARY:
            case Datatype.VARBINARY:
                return FieldBaseConverter.getFieldBase((byte[]) values, datatype);
            case Datatype.DEC:
            case Datatype.DECIMAL:
            case Datatype.NUMERIC:
            case Datatype.BIGDECIMAL:
                return FieldBaseConverter.getFieldBase((BigDecimal) values, datatype);
            case Datatype.VARCHAR:
            case Datatype.CHAR:
            case Datatype.CHARACTER:
            case Datatype.CHARACTERVARYING:
            case Datatype.CHARVARYING:
                return FieldBaseConverter.getFieldBase((String) values, datatype, null);
            case Datatype.BIT:
            case Datatype.BITVARYING:
                return FieldBaseConverter.getFieldBase((String) values, datatype, null);
            case Datatype.BLOB:
            case Datatype.BINARYLARGEOBJECT:
            case Datatype.LONGVARBINARY:
                return values instanceof DBlobUpdatable ? values == null ? new DBlobUpdatable(true) : (FieldBase) values : FieldBaseConverter.getFieldBase((Blob) values);
            case Datatype.DATE:
                return FieldBaseConverter.getFieldBase((java.sql.Date) values);
            case Datatype.TIMESTAMP:
                return FieldBaseConverter.getFieldBase((java.sql.Timestamp) values);
            case Datatype.TIME:
                return FieldBaseConverter.getFieldBase((java.sql.Time) values);
            case Datatype.BOOLEAN:
                return FieldBaseConverter.getFieldBase((Boolean) values);
            case Datatype.CHARACTERLARGEOBJECT:
            case Datatype.CHARLARGEOBJECT:
            case Datatype.CLOB:
            case Datatype.LONGVARCHAR:
                return values instanceof DClobUpdatable ? values == null ? new DClobUpdatable(true) : (FieldBase) values : FieldBaseConverter.getFieldBase((Clob) values);
            default:
                throw new DException("get filed nit implemnetd for datatype  " + datatype, null);
        }
    }

    public static FieldBase getField(int datatype, BufferRange values) throws DException {
        switch(datatype) {
            case Datatype.INT:
            case Datatype.INTEGER:
                return new FieldInteger(values, datatype);
            case Datatype.LONG:
            case Datatype.BIGINT:
                return new FieldLong(values, datatype);
            case Datatype.DOUBLE:
            case Datatype.DOUBLEPRECISION:
            case Datatype.FLOAT:
                return new FieldDouble(values, datatype);
            case Datatype.REAL:
                return new FieldReal(values, datatype);
            case Datatype.SMALLINT:
            case Datatype.SHORT:
                return new FieldShort(values, datatype);
            case Datatype.TINYINT:
            case Datatype.BYTE:
                return new FieldByte(values, datatype);
            case Datatype.BINARY:
            case Datatype.VARBINARY:
                return new FieldBinary(values, datatype);
            case Datatype.DEC:
            case Datatype.DECIMAL:
            case Datatype.NUMERIC:
            case Datatype.BIGDECIMAL:
                return new FieldBigDecimal(values, datatype);
            case Datatype.VARCHAR:
            case Datatype.CHAR:
            case Datatype.CHARACTER:
            case Datatype.CHARACTERVARYING:
            case Datatype.CHARVARYING:
                return new FieldString(values, datatype, null);
            case Datatype.DATE:
                return new FieldDate(values, datatype);
            case Datatype.TIMESTAMP:
                return new FieldTimeStamp(values, datatype);
            case Datatype.TIME:
                return new FieldTime(values, datatype);
            case Datatype.BOOLEAN:
                return new FieldBoolean(values, datatype);
            default:
                throw new DException("get field not implemented for datatype  " + datatype, null);
        }
    }

    public static FieldBase getField(Long value) {
        return new FieldLong(new BufferRange(CCzufDpowfsufs.getBytes(value)), Datatype.LONG);
    }

    public static FieldBase getFieldForVariable(Object object) throws DException {
        FieldBase field = null;
        if (object == null) {
            field = new FieldLiteral(null, -1);
            field.setBufferRange(NULLBUFFERRANGE);
        } else if (object instanceof Boolean) {
            field = new FieldBooleanLiteral(((Boolean) object).booleanValue() ? 0 : 1, Datatype.BOOLEAN);
        } else if (object instanceof String) {
            field = new FieldStringLiteral(object, Datatype.CHAR);
        } else if (object instanceof byte[]) {
            field = new FieldBinary(new BufferRange((byte[]) object), Datatype.BINARY);
        } else if (object instanceof Blob) {
            try {
                field = new DBlobUpdatable(((Blob) object).getBinaryStream());
            } catch (SQLException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
        } else if (object instanceof Clob) {
            try {
                field = new DClobUpdatable(((Clob) object).getAsciiStream());
            } catch (SQLException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
        } else if (object instanceof Date) {
            field = new FieldDateLiteral(object);
        } else if (object instanceof Time) {
            field = new FieldTimeLiteral(object);
        } else if (object instanceof Timestamp) {
            field = new FieldTimeStampLiteral(object);
        } else field = new FieldLiteral(object, -1);
        return field;
    }

    public static SuperComparator getAppropriateComparator(int type1, int type2, boolean caseSensitive) {
        throw new UnsupportedOperationException("method getField() not implemented");
    }

    public static FieldBase[] getFields(Object[] object) throws DException {
        FieldBase[] array = new FieldBase[object.length];
        for (int i = 0; i < object.length; i++) {
            array[i] = getFieldForVariable(object[i]);
        }
        return array;
    }

    public static FieldBase[] getFields(int[] datatypes, Collator collator) throws DException {
        FieldBase[] array = new FieldBase[datatypes.length];
        for (int i = 0; i < datatypes.length; i++) {
            array[i] = getField(datatypes[i], null, collator);
        }
        return array;
    }

    public static Object[] getBlankFields(int[] datatypes, Collator collator) throws DException {
        Object[] array = new Object[datatypes.length];
        for (int i = 0; i < datatypes.length; i++) {
            array[i] = getField(datatypes[i], null, collator);
        }
        return array;
    }

    private static FieldBinary convertNonDecimalToBinary(FieldBase field, int size, int fieldType) throws DException {
        BufferRange bf = field.getBufferRange();
        int length = bf.getLength();
        byte[] result = size == length ? bf.getBytes() : size > length ? padBytes(bf.getBytes(), size - length) : truncateBytes(bf.getBytes(), length, size);
        return new FieldBinary(new BufferRange(result), fieldType);
    }

    private static byte[] truncateBytes(byte[] bytes, int length, int size) throws DException {
        byte[] result = new byte[length - size];
        System.arraycopy(bytes, length - size, result, 0, size);
        return result;
    }

    private static byte[] padBytes(byte[] bytes, int length) throws DException {
        byte sign = getSign(bytes[0]);
        byte b = sign == -1 ? (byte) -1 : (byte) 0;
        byte[] result = new byte[bytes.length + length];
        for (int i = 0; i < length; i++) {
            result[i] = b;
        }
        System.arraycopy(bytes, 0, result, length, bytes.length);
        return result;
    }

    private static byte getSign(byte b) {
        return (byte) ((b >> 7) & 0xff);
    }

    public static FieldBase[] setFieldLiteralBufferRange(Object[] object) throws DException {
        FieldBase[] fbs = new FieldBase[object.length];
        FieldBase fb = null;
        for (int i = 0, len = object.length; i < len; i++) {
            fb = (FieldBase) object[i];
            fb.setDatatype(fb.getDatatype());
            fbs[i] = fb;
        }
        return fbs;
    }

    public static FieldBase[] setFieldLiteralBufferRangeWithArray(Object[] object) throws DException {
        FieldBase[] fbs = new FieldBase[object.length];
        FieldBase fb = null;
        for (int i = 0, len = object.length; i < len; i++) {
            if (object[i] instanceof Object[]) fb = (FieldBase) ((Object[]) object[i])[0]; else fb = (FieldBase) object[i];
            fb.setDatatype(fb.getDatatype());
            fbs[i] = fb;
        }
        return fbs;
    }

    public static FieldBase setFieldLiteralBufferRange(Object object) throws DException {
        FieldBase fb = (FieldBase) object;
        fb.setDatatype(fb.getDatatype());
        return fb;
    }

    private static void checkPrecisionScale(String object, int size, int scale) throws DException {
        char aaa = object.charAt(0);
        if (aaa == '-' || aaa == '+') {
            object = object.substring(1);
        }
        int length = object.indexOf('.');
        if (length == -1) {
            int expIndex = object.indexOf('E');
            expIndex = expIndex == -1 ? object.indexOf('e') : expIndex;
            length = expIndex == -1 ? object.length() : (Integer.parseInt(object.substring(expIndex + 1)) + 1) + expIndex - 1;
            if (length > (size - scale)) throw new DException("DSE5546", null);
        } else {
            if (size == scale) {
                if ((length == 1 && !object.startsWith("0")) || length > 1) throw new DException("DSE5546", null);
            } else {
                int eIndex = object.indexOf('E');
                eIndex = eIndex == -1 ? object.indexOf('e') : eIndex;
                length = eIndex == -1 ? length : Integer.parseInt(object.substring(eIndex + 1)) + 1;
                if (length > (size - scale)) {
                    throw new DException("DSE5546", null);
                }
            }
        }
    }

    public static FieldBase[] changeIntoFildBase(Object obj) throws DException {
        Object[] temp = (Object[]) obj;
        FieldBase[] fb = new FieldBase[temp.length];
        for (int i = 0; i < temp.length; i++) {
            fb[i] = (FieldBase) temp[i];
        }
        return fb;
    }
}
