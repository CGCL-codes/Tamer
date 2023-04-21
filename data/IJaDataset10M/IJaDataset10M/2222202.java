package mynotes;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Property {

    public static final int PT_NOTHING = -1;

    public static final int PT_BYTE = 1;

    public static final int PT_SHORT = 2;

    public static final int PT_INT = 3;

    public static final int PT_LONG = 4;

    public static final int PT_CHAR = 5;

    public static final int PT_FLOAT = 6;

    public static final int PT_DOUBLE = 7;

    public static final int PT_BOOLEAN = 8;

    public static final int PT_STRING = 9;

    String key;

    Object value;

    int type;

    protected int rsId;

    public Property() {
        type = PT_NOTHING;
        key = null;
        value = null;
    }

    public boolean fromByteArray(byte[] data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, data.length);
            DataInputStream dis = new DataInputStream(bais);
            type = dis.readInt();
            key = dis.readUTF();
            switch(type) {
                case PT_BYTE:
                    value = new Byte(dis.readByte());
                    break;
                case PT_SHORT:
                    value = new Short(dis.readShort());
                    break;
                case PT_INT:
                    value = new Integer(dis.readInt());
                    break;
                case PT_LONG:
                    value = new Long(dis.readLong());
                    break;
                case PT_CHAR:
                    value = new Character(dis.readChar());
                    break;
                case PT_FLOAT:
                    value = new Float(dis.readFloat());
                    break;
                case PT_DOUBLE:
                    value = new Double(dis.readDouble());
                    break;
                case PT_BOOLEAN:
                    value = new Boolean(dis.readBoolean());
                    break;
                case PT_STRING:
                    value = new String(dis.readUTF());
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public byte[] toByteArray() {
        byte[] data = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream das = new DataOutputStream(baos);
            das.writeInt(type);
            das.writeUTF(key);
            switch(type) {
                case PT_BYTE:
                    das.writeByte(((Integer) value).intValue());
                    break;
                case PT_SHORT:
                    das.writeShort(((Integer) value).intValue());
                    break;
                case PT_INT:
                    das.writeInt(((Integer) value).intValue());
                    break;
                case PT_LONG:
                    das.writeLong(((Integer) value).intValue());
                    break;
                case PT_CHAR:
                    das.writeChar(((Integer) value).intValue());
                    break;
                case PT_FLOAT:
                    das.writeFloat(((Float) value).floatValue());
                    break;
                case PT_DOUBLE:
                    das.writeDouble(((Double) value).doubleValue());
                    break;
                case PT_BOOLEAN:
                    das.writeBoolean(((Boolean) value).booleanValue());
                    break;
                case PT_STRING:
                    das.writeUTF((String) value);
                    break;
            }
            data = baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
