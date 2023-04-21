package ru.adv.db.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import ru.adv.db.adapter.Types;
import ru.adv.util.UnreachableCodeReachedException;

/**
 * Сохранение и чтение Java объектов в БД с привязкой к типам {@link
 * ru.adv.db.adapter.Types}
 *
 */
public abstract class DBValue implements Types, Cloneable {

    private Object value;

    private int type;

    protected DBValue(int type, Object value) throws DBCastException {
        this.type = type;
        set(value);
    }

    /**
     * Создает объект соответствующего подкласса {@link DBValue}
     * @param type значение из {@link Types}
     * @return
     */
    public static final DBValue createInstance(int type, Object value) throws DBCastException {
        DBValue result = null;
        switch(type) {
            case STRING:
            case TEXT:
                result = new StringDBValue(type, value);
                break;
            case SHORTINT:
                result = new ShortIntDBValue(type, value);
                break;
            case INT:
                result = new IntegerDBValue(type, value);
                break;
            case LONG:
                result = new LongDBValue(type, value);
                break;
            case DATE:
                result = new DateDBValue(type, value);
                break;
            case TIMESTAMP:
                result = new TimestampDBValue(type, value);
                break;
            case BOOLEAN:
                result = new BooleanDBValue(type, value);
                break;
            case FILE:
                result = new FileDBValue(type, value);
                break;
            case FLOAT:
                result = new FloatDBValue(type, value);
                break;
            case DOUBLE:
                result = new DoubleDBValue(type, value);
                break;
            case NUMERIC:
                result = new NumericDBValue(type, value);
                break;
            case CALCULATED:
                result = new CalculatedDBValue(type, value);
                break;
            default:
                throw new UnreachableCodeReachedException("Can't create DBValue instance, bad type value=" + type);
        }
        return result;
    }

    public static final DBValue createInstance(int type, ResultSet rs, int position) throws DBCastException, SQLException {
        DBValue result = createInstance(type, null);
        result.getFrom(rs, position);
        return result;
    }

    public final int type() {
        return type;
    }

    /**
     * Установливает значение с приведением к типу
     * @see DBCastException
     * @see ru.adv.db.base.MCast
     */
    protected final void set(Object v) throws DBCastException {
        if (v != null) {
            if (hasObjectNullValue(v)) {
                value = Null.INSTANCE;
            } else {
                value = customizeValue(v);
            }
        } else {
            value = null;
        }
    }

    private boolean hasObjectNullValue(Object value) {
        return isNull(value) || (isEmptyString(value) && isEmptyValueAllwaysNull());
    }

    public static boolean isNull(Object value) {
        return value == null || value == Null.INSTANCE;
    }

    private boolean isEmptyString(Object value) {
        return value.toString().length() == 0;
    }

    public boolean isString() {
        return false;
    }

    /**
     * Пустое значение всегда преобразуется в {@link Null}
     * @see #set
     * @return
     */
    protected abstract boolean isEmptyValueAllwaysNull();

    /**
     * Проверяет значение, приводит его к необходимому виду
     * @throws DBCastException неправильное значение
     * @see DBValue#set
     */
    protected abstract Object customizeValue(Object value) throws DBCastException;

    /**
     * Возвращает значение
     */
    public Object get() {
        return value;
    }

    /**
     * Если установлено значание NULL
     */
    public boolean isNull() {
        return (value instanceof Null);
    }

    /**
     * Зачитывает значение из <code>java.sql.ResultSet</code>
     * @param rs
     * @param position позиция значения в <code>rs</code>
     *
     */
    protected void getFrom(ResultSet rs, int position) throws SQLException, DBCastException {
        Object val = readFromResultSet(rs, position);
        if (rs.wasNull()) {
            set(Null.INSTANCE);
        } else {
            set(val);
        }
    }

    /**
     * Хранимое значение установлено и имеет тип {@link FileValue}
     * @return
     */
    public boolean containsFile() {
        return (get() != null && isFile());
    }

    /**
     *
     * @return
     */
    public boolean isFile() {
        return false;
    }

    /**
     * Зачитывает значение из {@link ResultSet}
     * @param rs
     * @param position
     * @return value для метода {@link #set}
     * @throws SQLException
     * @see #getFrom
     */
    protected abstract Object readFromResultSet(ResultSet rs, int position) throws SQLException;

    /**
     * equals
     * @param o
     * @return
     */
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!getClass().getName().equals(o.getClass().getName())) return false;
        final DBValue dbValue = (DBValue) o;
        if (value != null ? !value.equals(dbValue.value) : dbValue.value != null) return false;
        return true;
    }

    public final int hashCode() {
        return toString().hashCode();
    }

    public final String toString() {
        return getClass().getName() + " value=" + value;
    }

    /**
     * Читает из потока только значение объекта {@link #set}
     * @param input
     */
    public abstract void readFromStream(ObjectInputStream input) throws IOException, DBCastException, ClassNotFoundException;

    /**
     * Записывает только значение объекта {@link #get} в поток
     * @param output
     */
    public abstract void writeToStream(ObjectOutputStream output) throws IOException;

    /**
     * Проверяет что dbValue является типом файл и его значение установлено в false
     */
    public boolean isFileSetToFalse() {
        return false;
    }

    public DBValue copy() throws DBCastException {
        return copy(null);
    }

    public DBValue copy(Object value) throws DBCastException {
        DBValue result = null;
        try {
            result = (DBValue) clone();
            if (value != null) {
                result.set(value);
            }
        } catch (CloneNotSupportedException e) {
            throw new UnreachableCodeReachedException(e);
        }
        return result;
    }
}
