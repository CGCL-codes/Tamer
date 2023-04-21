package annone.database;

import java.io.InputStream;
import java.util.Date;
import annone.util.AnnoneException;
import annone.util.Text;

public class Column {

    private final Cursor cursor;

    private final int index;

    Column(Cursor cursor, int index) {
        this.cursor = cursor;
        this.index = index;
    }

    public String getString() {
        try {
            return cursor.rs.getString(index);
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read string column."), xp);
        }
    }

    public boolean getBoolean(boolean defaultValue) {
        try {
            boolean value = cursor.rs.getBoolean(index);
            return cursor.rs.wasNull() ? defaultValue : value;
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read boolean column."), xp);
        }
    }

    public int getInt(int defaultValue) {
        try {
            int value = cursor.rs.getInt(index);
            return cursor.rs.wasNull() ? defaultValue : value;
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read int column."), xp);
        }
    }

    public long getLong(long defaultValue) {
        try {
            long value = cursor.rs.getLong(index);
            return cursor.rs.wasNull() ? defaultValue : value;
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read long column."), xp);
        }
    }

    public double getDouble(double defaultValue) {
        try {
            double value = cursor.rs.getDouble(index);
            return cursor.rs.wasNull() ? defaultValue : value;
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read double column."), xp);
        }
    }

    public Date getDate() {
        try {
            return cursor.rs.getDate(index);
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read date column."), xp);
        }
    }

    public byte[] getBytes() {
        try {
            return cursor.rs.getBytes(index);
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read bytes column."), xp);
        }
    }

    public InputStream getBinaryStream() {
        try {
            return cursor.rs.getBinaryStream(index);
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read binary stream column."), xp);
        }
    }

    public Object getObject() {
        try {
            return cursor.rs.getObject(index);
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read object column."), xp);
        }
    }

    public Object getAny() {
        try {
            return cursor.rs.getObject(index);
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read any-object column."), xp);
        }
    }

    public long getId() {
        return getLong(0);
    }

    public <E extends Enum<E>> E getEnum(Class<E> enumClass) {
        try {
            int value = cursor.rs.getInt(index);
            return cursor.rs.wasNull() ? null : enumClass.getEnumConstants()[value];
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Can''t read enum column."), xp);
        }
    }
}
