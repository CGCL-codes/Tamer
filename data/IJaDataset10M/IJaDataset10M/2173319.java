package com.hazelcast.nio;

import java.io.*;

public class SerializationHelper {

    protected void writeObject(DataOutput out, Object obj) throws IOException {
        if (obj == null) {
            out.writeByte(0);
        }
        if (obj instanceof Long) {
            out.writeByte(1);
            out.writeLong((Long) obj);
        } else if (obj instanceof Integer) {
            out.writeByte(2);
            out.writeInt((Integer) obj);
        } else if (obj instanceof String) {
            out.writeByte(3);
            out.writeUTF((String) obj);
        } else if (obj instanceof Double) {
            out.writeByte(4);
            out.writeDouble((Double) obj);
        } else if (obj instanceof Float) {
            out.writeByte(5);
            out.writeDouble((Float) obj);
        } else if (obj instanceof Boolean) {
            out.writeByte(6);
            out.writeBoolean((Boolean) obj);
        } else {
            out.writeByte(7);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            byte[] buf = bos.toByteArray();
            out.writeInt(buf.length);
            out.write(buf);
        }
    }

    protected Object readObject(DataInput in) throws IOException {
        byte type = in.readByte();
        if (type == 0) {
            return null;
        } else if (type == 1) {
            return in.readInt();
        } else if (type == 2) {
            return in.readInt();
        } else if (type == 3) {
            return in.readUTF();
        } else if (type == 4) {
            return in.readDouble();
        } else if (type == 5) {
            return in.readFloat();
        } else if (type == 6) {
            return in.readBoolean();
        } else if (type == 7) {
            int len = in.readInt();
            byte[] buf = new byte[len];
            in.readFully(buf);
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(buf));
            try {
                return oin.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            oin.close();
        } else {
            throw new IOException("Unknown object type=" + type);
        }
        return null;
    }
}
