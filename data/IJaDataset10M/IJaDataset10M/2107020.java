package com.daffodilwoods.daffodildb.server.datadictionarysystem.information;

import java.util.HashMap;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.Datatype;

public class DatatypeInfoMap implements java.io.Serializable {

    private static HashMap hashMap;

    public static HashMap getDataTypeInfoMap() {
        if (hashMap == null) {
            hashMap = new HashMap();
            setDataTypeInfo();
        }
        return hashMap;
    }

    private static void setDataTypeInfo() {
        hashMap.put("character", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("char", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("character varying", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("char varying", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("varchar", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("varchar2", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("character large object", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("char large object", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("long varchar", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("clob", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("blob", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("binary", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("varbinary", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("long varbinary", new DataTypeProperties(false, 10, false, null, 1073741823, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("bit", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("bit varying", new DataTypeProperties(true, 10, false, null, 4192, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, false));
        hashMap.put("numeric", new DataTypeProperties(true, 28, false, null, Datatype.BIGDECIMAL_PRECISION, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
        hashMap.put("decimal", new DataTypeProperties(true, 28, false, null, Datatype.BIGDECIMAL_PRECISION, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
        hashMap.put("dec", new DataTypeProperties(true, 28, false, null, Datatype.BIGDECIMAL_PRECISION, 1, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
        hashMap.put("int", new DataTypeProperties(true, Datatype.INTSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Datatype.INT_PRECISION, 0, true));
        hashMap.put("integer", new DataTypeProperties(true, Datatype.INTSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Datatype.INT_PRECISION, 0, true));
        hashMap.put("smallint", new DataTypeProperties(true, Datatype.SHORTSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Datatype.INT_PRECISION, 0, true));
        hashMap.put("float", new DataTypeProperties(true, Datatype.FLOATSIZE, true, null, Datatype.FLOAT_PRECISION, 1, true, Datatype.FLOAT_PRECISION, 0, true));
        hashMap.put("real", new DataTypeProperties(true, Datatype.REALSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Datatype.REAL_PRECISION, 0, true));
        hashMap.put("double precision", new DataTypeProperties(true, Datatype.DOUBLESIZE, true, (Object) null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Datatype.DOUBLEPRECISION, 0, true));
        hashMap.put("boolean", new DataTypeProperties(true, Datatype.BOOLEANSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Datatype.BOOLEAN_PRECISION, 0, false));
        hashMap.put("date", new DataTypeProperties(true, Datatype.DATESIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, 19, 0, false));
        hashMap.put("time", new DataTypeProperties(true, Datatype.TIMESIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, 19, 0, false));
        hashMap.put("timestamp", new DataTypeProperties(true, Datatype.TIMESTAMPSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, 23, 0, false));
        hashMap.put("BigInt", new DataTypeProperties(true, Datatype.LONGSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
        hashMap.put("TinyInt", new DataTypeProperties(true, Datatype.BYTESIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
        hashMap.put("Byte", new DataTypeProperties(true, Datatype.BYTESIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
        hashMap.put("Long", new DataTypeProperties(true, Datatype.LONGSIZE, true, null, Integer.MAX_VALUE, Integer.MIN_VALUE, true, Integer.MIN_VALUE, Integer.MIN_VALUE, true));
    }
}
