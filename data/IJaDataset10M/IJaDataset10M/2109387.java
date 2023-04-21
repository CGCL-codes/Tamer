package com.avaje.ebeaninternal.server.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import com.avaje.ebeaninternal.server.lucene.LLuceneTypes;

/**
 * ScalarType for Float and float.
 */
public class ScalarTypeFloat extends ScalarTypeBase<Float> {

    public ScalarTypeFloat() {
        super(Float.class, true, Types.REAL);
    }

    public void bind(DataBind b, Float value) throws SQLException {
        if (value == null) {
            b.setNull(Types.REAL);
        } else {
            b.setFloat(value.floatValue());
        }
    }

    public Float read(DataReader dataReader) throws SQLException {
        return dataReader.getFloat();
    }

    public Object toJdbcType(Object value) {
        return BasicTypeConverter.toFloat(value);
    }

    public Float toBeanType(Object value) {
        return BasicTypeConverter.toFloat(value);
    }

    public String formatValue(Float t) {
        return t.toString();
    }

    public Float parse(String value) {
        return Float.valueOf(value);
    }

    public Float parseDateTime(long systemTimeMillis) {
        return Float.valueOf(systemTimeMillis);
    }

    public boolean isDateTimeCapable() {
        return true;
    }

    public String toJsonString(Float value) {
        if (value.isInfinite() || value.isNaN()) {
            return "null";
        } else {
            return value.toString();
        }
    }

    public int getLuceneType() {
        return LLuceneTypes.FLOAT;
    }

    public Object luceneFromIndexValue(Object value) {
        return value;
    }

    public Object luceneToIndexValue(Object value) {
        return value;
    }

    public Object readData(DataInput dataInput) throws IOException {
        if (!dataInput.readBoolean()) {
            return null;
        } else {
            float val = dataInput.readFloat();
            return Float.valueOf(val);
        }
    }

    public void writeData(DataOutput dataOutput, Object v) throws IOException {
        Float value = (Float) v;
        if (value == null) {
            dataOutput.writeBoolean(false);
        } else {
            dataOutput.writeBoolean(true);
            dataOutput.writeFloat(value.floatValue());
        }
    }
}
