package com.avaje.ebeaninternal.server.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import com.avaje.ebeaninternal.server.lucene.LLuceneTypes;

/**
 * ScalarType for Long and long.
 */
public class ScalarTypeLong extends ScalarTypeBase<Long> {

    public ScalarTypeLong() {
        super(Long.class, true, Types.BIGINT);
    }

    public void bind(DataBind b, Long value) throws SQLException {
        if (value == null) {
            b.setNull(Types.BIGINT);
        } else {
            b.setLong(value.longValue());
        }
    }

    public Long read(DataReader dataReader) throws SQLException {
        return dataReader.getLong();
    }

    public Object toJdbcType(Object value) {
        return BasicTypeConverter.toLong(value);
    }

    public Long toBeanType(Object value) {
        return BasicTypeConverter.toLong(value);
    }

    public String formatValue(Long t) {
        return t.toString();
    }

    public Long parse(String value) {
        return Long.valueOf(value);
    }

    public Long parseDateTime(long systemTimeMillis) {
        return Long.valueOf(systemTimeMillis);
    }

    public boolean isDateTimeCapable() {
        return true;
    }

    public int getLuceneType() {
        return LLuceneTypes.LONG;
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
            long val = dataInput.readLong();
            return Long.valueOf(val);
        }
    }

    public void writeData(DataOutput dataOutput, Object v) throws IOException {
        Long value = (Long) v;
        if (value == null) {
            dataOutput.writeBoolean(false);
        } else {
            dataOutput.writeBoolean(true);
            dataOutput.writeLong(value.longValue());
        }
    }
}
