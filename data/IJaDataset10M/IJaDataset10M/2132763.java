package org.maestroframework.db.dialects.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import org.maestroframework.db.DatabaseTypeMapper;
import org.maestroframework.db.interfaces.TypeSerializer;

public class MySQLTypeMapper extends DatabaseTypeMapper {

    public MySQLTypeMapper() {
        TypeSerializer<Boolean> booleanSerializer = new TypeSerializer<Boolean>() {

            public void serialize(PreparedStatement ps, Boolean value, int colIndex) throws SQLException {
                ps.setBoolean(colIndex, value);
            }

            public Boolean deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getBoolean(colIndex);
            }
        };
        this.setTypeSerializer(Boolean.class, booleanSerializer);
        this.setTypeSerializer(Boolean.TYPE, booleanSerializer);
        TypeSerializer<Byte> byteSerializer = new TypeSerializer<Byte>() {

            public void serialize(PreparedStatement ps, Byte value, int colIndex) throws SQLException {
                ps.setByte(colIndex, value);
            }

            public Byte deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getByte(colIndex);
            }
        };
        this.setTypeSerializer(Byte.class, byteSerializer);
        this.setTypeSerializer(Byte.TYPE, byteSerializer);
        TypeSerializer<byte[]> byteArraySerializer = new TypeSerializer<byte[]>() {

            public void serialize(PreparedStatement ps, byte[] value, int colIndex) throws SQLException {
                ps.setBytes(colIndex, value);
            }

            public byte[] deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getBytes(colIndex);
            }
        };
        this.setTypeSerializer(byte[].class, byteArraySerializer);
        TypeSerializer<Double> doubleSerializer = new TypeSerializer<Double>() {

            public void serialize(PreparedStatement ps, Double value, int colIndex) throws SQLException {
                ps.setDouble(colIndex, value);
            }

            public Double deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getDouble(colIndex);
            }
        };
        this.setTypeSerializer(Double.class, doubleSerializer);
        this.setTypeSerializer(Double.TYPE, doubleSerializer);
        TypeSerializer<Float> floatSerializer = new TypeSerializer<Float>() {

            public void serialize(PreparedStatement ps, Float value, int colIndex) throws SQLException {
                ps.setFloat(colIndex, value);
            }

            public Float deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getFloat(colIndex);
            }
        };
        this.setTypeSerializer(Float.class, floatSerializer);
        this.setTypeSerializer(Float.TYPE, floatSerializer);
        TypeSerializer<Integer> integerSerializer = new TypeSerializer<Integer>() {

            public void serialize(PreparedStatement ps, Integer value, int colIndex) throws SQLException {
                ps.setInt(colIndex, value);
            }

            public Integer deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getInt(colIndex);
            }
        };
        this.setTypeSerializer(Integer.class, integerSerializer);
        this.setTypeSerializer(Integer.TYPE, integerSerializer);
        TypeSerializer<Long> longSerializer = new TypeSerializer<Long>() {

            public void serialize(PreparedStatement ps, Long value, int colIndex) throws SQLException {
                ps.setLong(colIndex, value);
            }

            public Long deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getLong(colIndex);
            }
        };
        this.setTypeSerializer(Long.class, longSerializer);
        this.setTypeSerializer(Long.TYPE, longSerializer);
        TypeSerializer<Short> shortSerializer = new TypeSerializer<Short>() {

            public void serialize(PreparedStatement ps, Short value, int colIndex) throws SQLException {
                ps.setShort(colIndex, value);
            }

            public Short deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getShort(colIndex);
            }
        };
        this.setTypeSerializer(Short.class, shortSerializer);
        this.setTypeSerializer(Short.TYPE, shortSerializer);
        this.setTypeSerializer(String.class, new TypeSerializer<String>() {

            public void serialize(PreparedStatement ps, String value, int colIndex) throws SQLException {
                ps.setString(colIndex, value);
            }

            public String deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getString(colIndex);
            }
        });
        this.setTypeSerializer(StringBuffer.class, new TypeSerializer<StringBuffer>() {

            public void serialize(PreparedStatement ps, StringBuffer value, int colIndex) throws SQLException {
                ps.setString(colIndex, value.toString());
            }

            public StringBuffer deserialize(ResultSet rs, int colIndex) throws SQLException {
                return new StringBuffer(rs.getString(colIndex));
            }
        });
        this.setTypeSerializer(StringBuilder.class, new TypeSerializer<StringBuilder>() {

            public void serialize(PreparedStatement ps, StringBuilder value, int colIndex) throws SQLException {
                ps.setString(colIndex, value.toString());
            }

            public StringBuilder deserialize(ResultSet rs, int colIndex) throws SQLException {
                return new StringBuilder(rs.getString(colIndex));
            }
        });
        TypeSerializer<java.util.Date> dateSerializer = new TypeSerializer<java.util.Date>() {

            public void serialize(PreparedStatement ps, java.util.Date value, int colIndex) throws SQLException {
                ps.setTimestamp(colIndex, new java.sql.Timestamp(value.getTime()));
            }

            public java.util.Date deserialize(ResultSet rs, int colIndex) throws SQLException {
                Date newDate = null;
                java.sql.Timestamp ts = rs.getTimestamp(colIndex);
                if (ts != null) newDate = new Date(ts.getTime());
                return newDate;
            }
        };
        this.setTypeSerializer(java.util.Date.class, dateSerializer);
        TypeSerializer<java.util.Calendar> calendarSerializer = new TypeSerializer<java.util.Calendar>() {

            public void serialize(PreparedStatement ps, java.util.Calendar value, int colIndex) throws SQLException {
                ps.setTimestamp(colIndex, new java.sql.Timestamp(value.getTime().getTime()));
            }

            public java.util.Calendar deserialize(ResultSet rs, int colIndex) throws SQLException {
                Date newDate = null;
                java.sql.Timestamp ts = rs.getTimestamp(colIndex);
                if (ts != null) newDate = new Date(ts.getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(newDate);
                return cal;
            }
        };
        this.setTypeSerializer(java.util.Calendar.class, calendarSerializer);
        TypeSerializer<java.net.URL> urlSerializer = new TypeSerializer<java.net.URL>() {

            public void serialize(PreparedStatement ps, java.net.URL value, int colIndex) throws SQLException {
                ps.setURL(colIndex, value);
            }

            public java.net.URL deserialize(ResultSet rs, int colIndex) throws SQLException {
                return rs.getURL(colIndex);
            }
        };
        this.setTypeSerializer(java.net.URL.class, urlSerializer);
    }
}
