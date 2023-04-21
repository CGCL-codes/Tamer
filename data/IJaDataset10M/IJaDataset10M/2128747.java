package bm.core.io;

import bm.core.CoreConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Handles simple and complex serialization of objects.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision: 6 $
 */
public class SerializerOutputStream extends OutputStream {

    protected DataOutputStream out;

    public SerializerOutputStream(final OutputStream out) {
        this.out = out instanceof DataOutputStream ? (DataOutputStream) out : new DataOutputStream(out);
    }

    public void write(int i) throws IOException {
        out.write(i);
    }

    public void writeInt(final int i) throws SerializationException {
        try {
            out.writeInt(i);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeInt(final Integer i) throws SerializationException {
        try {
            out.writeBoolean(i == null);
            if (i != null) {
                out.writeInt(i.intValue());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeByte(final byte b) throws SerializationException {
        try {
            out.writeByte(b);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeByte(final Byte b) throws SerializationException {
        try {
            out.writeBoolean(b == null);
            if (b != null) {
                out.writeByte(b.byteValue());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeBoolean(final boolean b) throws SerializationException {
        try {
            out.writeBoolean(b);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeBoolean(final Boolean b) throws SerializationException {
        try {
            out.writeBoolean(b == null);
            if (b != null) {
                out.writeBoolean(b.booleanValue());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeShort(final short s) throws SerializationException {
        try {
            out.writeShort(s);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeShort(final Short s) throws SerializationException {
        try {
            out.writeBoolean(s == null);
            if (s != null) {
                out.writeShort(s.shortValue());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeLong(final long l) throws SerializationException {
        try {
            out.writeLong(l);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeLong(final Long l) throws SerializationException {
        try {
            out.writeBoolean(l == null);
            if (l != null) {
                out.writeLong(l.longValue());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeDate(final Date date) throws SerializationException {
        try {
            out.writeLong(date.getTime());
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeNullableDate(final Date date) throws SerializationException {
        try {
            out.writeBoolean(date == null);
            if (date != null) {
                out.writeLong(date.getTime());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeString(final String s) throws SerializationException {
        try {
            out.writeUTF(s);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeString(final String s, final int maxLength) throws SerializationException {
        try {
            out.writeUTF(s.length() > maxLength ? s.substring(0, maxLength) : s);
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeNullableString(final String s, final int maxLength) throws SerializationException {
        try {
            out.writeBoolean(s == null);
            if (s != null) {
                out.writeUTF(s.length() > maxLength ? s.substring(0, maxLength) : s);
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeNullableString(final String s) throws SerializationException {
        try {
            out.writeBoolean(s == null);
            if (s != null) {
                out.writeUTF(s);
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeBlob(final byte[] data) throws SerializationException {
        writeBlob(data, 0, data.length);
    }

    public void writeBlob(final byte[] data, final int offset, final int length) throws SerializationException {
        try {
            final int last = offset + length;
            out.writeInt(length);
            for (int i = offset; i < last; i++) {
                out.writeByte(data[i]);
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    public void writeNullableBlob(final byte[] data) throws SerializationException {
        try {
            out.writeBoolean(data == null);
            if (data != null) {
                writeBlob(data);
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS);
        }
    }

    /**
     * Write an non-null object to a Stream.<br/>
     * Objects can be of one of these types:
     * Serializable, String, Long, Byte, Date, Boolean, Integer, Short, byte[].<br/>
     * They can also be collections of objects of the above types, to any nesting
     * level. Collections can be simple arrays, Vector or Hashtable.
     *
     * @param o value to write
     * @throws bm.core.io.SerializationException on serialization exceptions
     */
    public void writeObject(final Object o) throws SerializationException {
        try {
            if (o instanceof byte[]) {
                out.writeByte(CoreConstants.BOB);
                writeBlob((byte[]) o);
            } else if (o.getClass().isArray()) {
                out.writeByte(CoreConstants.ARRAY);
                final Object[] array = (Object[]) o;
                final int length = array.length;
                out.writeShort(length);
                for (int i = 0; i < length; i++) {
                    writeNullableObject(array[i]);
                }
            } else if (o instanceof Vector) {
                out.writeByte(CoreConstants.VECTOR);
                final Vector list = (Vector) o;
                final int listLength = list.size();
                out.writeShort(listLength);
                for (int i = 0; i < listLength; i++) {
                    writeObject(list.elementAt(i));
                }
            } else if (o instanceof Hashtable) {
                out.writeByte(CoreConstants.MAP);
                final Hashtable map = (Hashtable) o;
                out.writeShort(map.size());
                for (Enumeration i = map.keys(); i.hasMoreElements(); ) {
                    final Object key = i.nextElement();
                    final Object value = map.get(key);
                    writeObject(key);
                    writeObject(value);
                }
            } else if (o instanceof Serializable) {
                out.writeByte(CoreConstants.SERIALIZABLE);
                final String className = ((Serializable) o).getSerializableClassName();
                if (className != null) {
                    writeString(className);
                } else {
                    writeString(o.getClass().getName());
                }
                ((Serializable) o).serialize(this);
            } else if (o instanceof String) {
                out.writeByte(CoreConstants.STRING);
                writeString((String) o);
            } else if (o instanceof Long) {
                out.writeByte(CoreConstants.LONG);
                out.writeLong(((Long) o).longValue());
            } else if (o instanceof Byte) {
                out.writeByte(CoreConstants.BYTE);
                out.writeByte(((Byte) o).byteValue());
            } else if (o instanceof Boolean) {
                out.writeByte(CoreConstants.BOOLEAN);
                out.writeBoolean(((Boolean) o).booleanValue());
            } else if (o instanceof Integer) {
                out.writeByte(CoreConstants.INTEGER);
                out.writeInt(((Integer) o).intValue());
            } else if (o instanceof Short) {
                out.writeByte(CoreConstants.SHORT);
                out.writeShort(((Short) o).shortValue());
            } else if (o instanceof Date) {
                out.writeByte(CoreConstants.DATE);
                out.writeLong(((Date) o).getTime());
            } else {
                out.writeByte(CoreConstants.STRING);
                writeString(o.toString());
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS, e);
        }
    }

    public void writeNullableObject(final Object o) throws SerializationException {
        try {
            out.writeBoolean(o == null);
            if (o != null) {
                writeObject(o);
            }
        } catch (IOException e) {
            throw new SerializationException(CoreConstants.ERR_SOS, e);
        }
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        super.close();
        out.close();
    }
}
