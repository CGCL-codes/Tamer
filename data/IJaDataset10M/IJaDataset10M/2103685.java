package com.xavax.xstore;

import com.xavax.xstore.exception.*;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Types;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AttributeMap encapsulates the metadata for an attribute (or field)
 * within a class and includes methods to get and set the value of an
 * attribute in an instance of the class.  The metadata consists of
 * the attribute name, data type, position within the attribute list,
 * and the name of the ColumnMap to which this attribute is mapped.
 * All metadata except the attribute and column name are determined
 * at initialization time using reflection.
 */
public class AttributeMap extends PersistenceMap {

    /**
   * Construct an AttributeMap.
   *
   * @param pm  the persistence manager.
   * @param name  the name of this attribute.
   * @param classMap  the ClassMap to which this attribute belongs.
   * @param column  the ColumnMap to which this attribute is mapped.
   */
    AttributeMap(PersistenceManager pm, String name, ClassMap<?> classMap, ColumnMap column) throws PersistenceMetadataException {
        super(pm, name);
        _classMap = classMap;
        _column = column;
        _position = 0;
        _field = null;
        try {
            _field = classMap.implementation().getDeclaredField(name);
            _field.setAccessible(true);
            _type = JTypes.value(_field.getType().getName());
        } catch (NoSuchFieldException e) {
            throw new PersistenceMetadataException(manager(), classMap.name(), name, _msg[0], e);
        }
    }

    /**
   * Get the value of this attribute from the specified object and
   * set the specified positional parameter of a prepared statement.
   *
   * @param stmt  the prepared statement to modify.
   * @param position  the positional parameter to be set.
   * @param target  the instance from which the attribute is read.
   */
    void getField(PreparedStatement stmt, int position, Object target) throws PersistenceException {
        if (_field != null) {
            try {
                switch(_type) {
                    case JTypes.BOOLEAN:
                        {
                            boolean value = _field.getBoolean(target);
                            stmt.setBoolean(position, value);
                        }
                        break;
                    case JTypes.BYTE:
                        {
                            byte value = _field.getByte(target);
                            stmt.setByte(position, value);
                        }
                        break;
                    case JTypes.CHAR:
                        {
                            char value = _field.getChar(target);
                            stmt.setString(position, String.valueOf(value));
                        }
                        break;
                    case JTypes.SHORT:
                        {
                            short value = _field.getShort(target);
                            stmt.setShort(position, value);
                        }
                        break;
                    case JTypes.INT:
                        {
                            int value = _field.getInt(target);
                            stmt.setInt(position, value);
                        }
                        break;
                    case JTypes.LONG:
                        {
                            long value = _field.getLong(target);
                            stmt.setLong(position, value);
                        }
                        break;
                    case JTypes.FLOAT:
                        {
                            float value = _field.getFloat(target);
                            stmt.setFloat(position, value);
                        }
                        break;
                    case JTypes.DOUBLE:
                        {
                            double value = _field.getDouble(target);
                            stmt.setDouble(position, value);
                        }
                        break;
                    case JTypes.STRING:
                        {
                            String value = (String) _field.get(target);
                            if (value != null) {
                                stmt.setString(position, value);
                            } else {
                                stmt.setNull(position, Types.VARCHAR);
                            }
                        }
                        break;
                    case JTypes.DATE:
                        {
                            Date value = (Date) _field.get(target);
                            if (value != null) {
                                stmt.setDate(position, value);
                            } else {
                                stmt.setNull(position, Types.DATE);
                            }
                        }
                        break;
                    case JTypes.WBOOLEAN:
                        {
                            Boolean value = (Boolean) _field.get(target);
                            if (value != null) {
                                stmt.setBoolean(position, value.booleanValue());
                            } else {
                                stmt.setNull(position, Types.BOOLEAN);
                            }
                        }
                        break;
                    case JTypes.WBYTE:
                        {
                            Byte value = (Byte) _field.get(target);
                            if (value != null) {
                                stmt.setByte(position, value.byteValue());
                            } else {
                                stmt.setNull(position, Types.TINYINT);
                            }
                        }
                        break;
                    case JTypes.WCHAR:
                        {
                            Character value = (Character) _field.get(target);
                            if (value != null) {
                                String s = Character.toString(value.charValue());
                                stmt.setString(position, s);
                            } else {
                                stmt.setNull(position, Types.CHAR);
                            }
                        }
                        break;
                    case JTypes.WSHORT:
                        {
                            Short value = (Short) _field.get(target);
                            if (value != null) {
                                stmt.setShort(position, value.shortValue());
                            } else {
                                stmt.setNull(position, Types.SMALLINT);
                            }
                        }
                        break;
                    case JTypes.WINT:
                        {
                            Integer value = (Integer) _field.get(target);
                            if (value != null) {
                                stmt.setInt(position, value.intValue());
                            } else {
                                stmt.setNull(position, Types.INTEGER);
                            }
                        }
                        break;
                    case JTypes.WLONG:
                        {
                            Long value = (Long) _field.get(target);
                            if (value != null) {
                                stmt.setLong(position, value.longValue());
                            } else {
                                stmt.setNull(position, Types.BIGINT);
                            }
                        }
                        break;
                    case JTypes.WFLOAT:
                        {
                            Float value = (Float) _field.get(target);
                            if (value != null) {
                                stmt.setFloat(position, value.floatValue());
                            } else {
                                stmt.setNull(position, Types.FLOAT);
                            }
                        }
                        break;
                    case JTypes.WDOUBLE:
                        {
                            Double value = (Double) _field.get(target);
                            if (value != null) {
                                stmt.setDouble(position, value.doubleValue());
                            } else {
                                stmt.setNull(position, Types.DOUBLE);
                            }
                        }
                        break;
                    case JTypes.TIMESTAMP:
                        {
                            Timestamp value = (Timestamp) _field.get(target);
                            if (value != null) {
                                stmt.setTimestamp(position, value);
                            } else {
                                stmt.setNull(position, Types.TIMESTAMP);
                            }
                        }
                        break;
                }
            } catch (IllegalAccessException e) {
                throw new FieldAccessException(manager(), _classMap, name(), e);
            } catch (SQLException e) {
                throw new PersistenceException(manager(), _msg[1], e);
            }
        }
    }

    /**
   * Set this attribute in the specified object using the value read
   * from a result set.
   *
   * @param rs  the result set a select statement.
   * @param target  the instance in which this attribute is set.
   */
    void setField(ResultSet rs, Object target) throws PersistenceException {
        if (_field != null) {
            try {
                switch(_type) {
                    case JTypes.BOOLEAN:
                        {
                            boolean value = rs.getBoolean(_position);
                            _field.setBoolean(target, value);
                        }
                        break;
                    case JTypes.BYTE:
                        {
                            byte value = rs.getByte(_position);
                            _field.setByte(target, value);
                        }
                        break;
                    case JTypes.CHAR:
                        {
                            String value = rs.getString(_position);
                            _field.setChar(target, value.charAt(0));
                        }
                        break;
                    case JTypes.SHORT:
                        {
                            short value = rs.getShort(_position);
                            _field.setShort(target, value);
                        }
                        break;
                    case JTypes.INT:
                        {
                            int value = rs.getInt(_position);
                            _field.setInt(target, value);
                        }
                        break;
                    case JTypes.LONG:
                        {
                            long value = rs.getLong(_position);
                            _field.setLong(target, value);
                        }
                        break;
                    case JTypes.FLOAT:
                        {
                            float value = rs.getFloat(_position);
                            _field.setFloat(target, value);
                        }
                        break;
                    case JTypes.DOUBLE:
                        {
                            double value = rs.getDouble(_position);
                            _field.setDouble(target, value);
                        }
                        break;
                    case JTypes.STRING:
                        {
                            String s = rs.getString(_position);
                            String value = rs.wasNull() ? null : s;
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.OBJECT:
                        break;
                    case JTypes.DATE:
                        {
                            Date value = rs.getDate(_position);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WBOOLEAN:
                        {
                            boolean b = rs.getBoolean(_position);
                            Boolean value = rs.wasNull() ? null : new Boolean(b);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WBYTE:
                        {
                            byte b = rs.getByte(_position);
                            Byte value = rs.wasNull() ? null : new Byte(b);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WCHAR:
                        {
                            String s = rs.getString(_position);
                            Character value = rs.wasNull() ? null : new Character(s.charAt(0));
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WSHORT:
                        {
                            short s = rs.getShort(_position);
                            Short value = rs.wasNull() ? null : new Short(s);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WINT:
                        {
                            int i = rs.getInt(_position);
                            Integer value = rs.wasNull() ? null : new Integer(i);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WLONG:
                        {
                            long l = rs.getLong(_position);
                            Long value = rs.wasNull() ? null : new Long(l);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WFLOAT:
                        {
                            float f = rs.getFloat(_position);
                            Float value = rs.wasNull() ? null : new Float(f);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.WDOUBLE:
                        {
                            double d = rs.getDouble(_position);
                            Double value = rs.wasNull() ? null : new Double(d);
                            _field.set(target, value);
                        }
                        break;
                    case JTypes.TIMESTAMP:
                        {
                            Timestamp value = rs.getTimestamp(_position);
                            _field.set(target, value);
                        }
                        break;
                }
            } catch (IllegalAccessException e) {
                throw new FieldAccessException(manager(), _classMap, name(), e);
            } catch (SQLException e) {
                throw new PersistenceException(manager(), _msg[1], e);
            }
        }
    }

    /**
   * Output a description of this attribute on stdout.
   */
    public void show() {
        TableMap table = _column.table();
        System.out.print("    " + name());
        if ((_type & JTypes.UNSIGNED) != 0) {
            System.out.print(" unsigned");
        }
        System.out.println(" " + JTypes.name(_type) + " -> " + table.name() + "." + _column.name());
    }

    /**
   * Output an XML representation of this attribute on the specified
   * print writer.
   *
   * @param out  the print writer to use for all output.
   */
    public void outputXML(PrintWriter out) {
        out.println("    <attribute-descriptor>");
        out.println("      <attribute-name>" + name() + "</attribute-name>");
        out.println("      <type>" + JTypes.name(_type) + "</type>");
        if ((_type & JTypes.UNSIGNED) != 0) {
            out.println("      <modifier>unsigned</modifier>");
        }
        out.println("      <column>" + _column.name() + "</column>");
        out.println("    </attribute-descriptor>");
    }

    /**
   * Returns the ClassMap for the class to which this attribute belongs.
   *
   * @return the ClassMap for the class to which this attribute belongs.
   */
    public ClassMap<?> classMap() {
        return _classMap;
    }

    /**
   * Returns the ColumnMap to which this attribute is mapped.
   *
   * @return the ColumnMap to which this attribute is mapped.
   */
    public ColumnMap column() {
        return _column;
    }

    /**
   * Returns the Field obtained using reflection for this attribute.
   *
   * @return the Field for this attribute.
   */
    Field field() {
        return _field;
    }

    /**
   * Set the position of this attribute within prepared statements.
   *
   * @param i  the parameter position.
   */
    void position(int i) {
        _position = i;
    }

    /**
   * Returns the position of this attribute within prepared statements.
   *
   * @return the position of this attribute within prepared statements.
   */
    int position() {
        return _position;
    }

    /**
   * Returns the index of the table containing this attribute within
   * the "from" clause in prepared statements used by the persistence
   * manager.
   *
   * @return the index of the table containing this attribute.
   */
    int tableIndex() {
        return _tindex;
    }

    /**
   * Sets the index of the table containing this attribute within the
   * "from" clause in prepared statements used by the persistence manager.
   *
   * @param i  the index of the table containing this attribute.
   */
    void tableIndex(int i) {
        _tindex = i;
    }

    /**
   * Returns the Java data type of this attribute.
   *
   * @see com.xavax.xstore.JTypes
   * @return the Java data type of this attribute.
   */
    public int type() {
        return _type;
    }

    private static final String[] _msg = new String[] { "no such field.", "unexpected SQL exception." };

    private short _type;

    private int _position;

    private int _tindex;

    private ClassMap<?> _classMap;

    private ColumnMap _column;

    private Field _field;
}
