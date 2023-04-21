package de.grogra.persistence;

import de.grogra.reflect.*;

public class FieldAccessor {

    protected final Field field;

    private final int typeId;

    public FieldAccessor(Field field) {
        this.field = field;
        typeId = field.getType().getTypeId();
    }

    public Type getType() {
        return field.getType();
    }

    public boolean getBoolean(Object object) throws IllegalAccessException {
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getBoolean(object, null) : field.getBoolean(object);
    }

    public boolean setBoolean(Object object, boolean value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            f.setBoolean(object, null, value, t);
        } else {
            field.setBoolean(object, value);
        }
        return value;
    }

    public byte getByte(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (byte) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object);
    }

    public byte setByte(Object object, byte value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (byte) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (byte) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (byte) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (byte) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (byte) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (byte) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (byte) value;
            }
            f.setByte(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (byte) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (byte) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (byte) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (byte) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (byte) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (byte) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (byte) (double) value;
            }
            field.setByte(object, value);
        }
        return value;
    }

    public short getShort(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (short) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object);
    }

    public short setShort(Object object, short value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (short) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (short) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (short) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (short) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (short) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (short) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (short) value;
            }
            f.setShort(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (short) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (short) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (short) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (short) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (short) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (short) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (short) (double) value;
            }
            field.setShort(object, value);
        }
        return value;
    }

    public char getChar(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (char) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object);
    }

    public char setChar(Object object, char value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (char) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (char) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (char) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (char) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (char) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (char) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (char) value;
            }
            f.setChar(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (char) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (char) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (char) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (char) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (char) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (char) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (char) (double) value;
            }
            field.setChar(object, value);
        }
        return value;
    }

    public int getInt(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (int) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object);
    }

    public int setInt(Object object, int value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (int) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (int) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (int) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (int) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (int) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (int) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (int) value;
            }
            f.setInt(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (int) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (int) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (int) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (int) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (int) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (int) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (int) (double) value;
            }
            field.setInt(object, value);
        }
        return value;
    }

    public long getLong(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (long) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object);
    }

    public long setLong(Object object, long value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (long) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (long) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (long) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (long) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (long) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (long) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (long) value;
            }
            f.setLong(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (long) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (long) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (long) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (long) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (long) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (long) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (long) (double) value;
            }
            field.setLong(object, value);
        }
        return value;
    }

    public float getFloat(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (float) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object);
    }

    public float setFloat(Object object, float value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (float) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (float) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (float) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (float) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (float) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (float) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (float) value;
            }
            f.setFloat(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (float) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (float) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (float) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (float) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (float) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (float) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (float) (double) value;
            }
            field.setFloat(object, value);
        }
        return value;
    }

    public double getDouble(Object object) throws IllegalAccessException {
        switch(typeId) {
            case TypeId.BYTE:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getByte(object, null) : field.getByte(object));
            case TypeId.SHORT:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getShort(object, null) : field.getShort(object));
            case TypeId.CHAR:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getChar(object, null) : field.getChar(object));
            case TypeId.INT:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getInt(object, null) : field.getInt(object));
            case TypeId.LONG:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getLong(object, null) : field.getLong(object));
            case TypeId.FLOAT:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getFloat(object, null) : field.getFloat(object));
            case TypeId.DOUBLE:
                return (double) ((field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object));
        }
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getDouble(object, null) : field.getDouble(object);
    }

    public double setDouble(Object object, double value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            switch(typeId) {
                case TypeId.BYTE:
                    f.setByte(object, null, (byte) value, t);
                    return (double) value;
                case TypeId.SHORT:
                    f.setShort(object, null, (short) value, t);
                    return (double) value;
                case TypeId.CHAR:
                    f.setChar(object, null, (char) value, t);
                    return (double) value;
                case TypeId.INT:
                    f.setInt(object, null, (int) value, t);
                    return (double) value;
                case TypeId.LONG:
                    f.setLong(object, null, (long) value, t);
                    return (double) value;
                case TypeId.FLOAT:
                    f.setFloat(object, null, (float) value, t);
                    return (double) value;
                case TypeId.DOUBLE:
                    f.setDouble(object, null, (double) value, t);
                    return (double) value;
            }
            f.setDouble(object, null, value, t);
        } else {
            switch(typeId) {
                case TypeId.BYTE:
                    field.setByte(object, (byte) value);
                    return (double) (byte) value;
                case TypeId.SHORT:
                    field.setShort(object, (short) value);
                    return (double) (short) value;
                case TypeId.CHAR:
                    field.setChar(object, (char) value);
                    return (double) (char) value;
                case TypeId.INT:
                    field.setInt(object, (int) value);
                    return (double) (int) value;
                case TypeId.LONG:
                    field.setLong(object, (long) value);
                    return (double) (long) value;
                case TypeId.FLOAT:
                    field.setFloat(object, (float) value);
                    return (double) (float) value;
                case TypeId.DOUBLE:
                    field.setDouble(object, (double) value);
                    return (double) (double) value;
            }
            field.setDouble(object, value);
        }
        return value;
    }

    public Object getObject(Object object) throws IllegalAccessException {
        return (field instanceof PersistenceField) ? ((PersistenceField) field).getObject(object, null) : field.getObject(object);
    }

    public Object setObject(Object object, Object value, Transaction t) throws IllegalAccessException {
        if (field instanceof PersistenceField) {
            PersistenceField f = (PersistenceField) field;
            f.setObject(object, null, value, t);
        } else {
            field.setObject(object, value);
        }
        return value;
    }

    public Object setSubfield(Object object, FieldChain fields, int[] indices, Object value, Transaction t) {
        if (field instanceof PersistenceField) {
            IndirectField i = new IndirectField((PersistenceField) field);
            i.add(fields).set((PersistenceCapable) object, indices, value, t);
        } else {
            throw new UnsupportedOperationException();
        }
        return value;
    }
}
