package com.wuala.loader2.loader.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import com.wuala.loader2.copied.Util;

public class ItemData {

    private static final byte VERSION = 1;

    private static final byte NOT_EXISTING = 2;

    private String name;

    private short version;

    private byte[] data;

    public Object cachedData;

    public ItemData(ZipFile zipFile, ZipEntry entry) throws IOException {
        this(entry.getName(), extractVersion(entry), readData(zipFile, entry));
    }

    private static byte[] readData(ZipFile zipFile, ZipEntry entry) throws IOException {
        InputStream is = zipFile.getInputStream(entry);
        try {
            return Util.toByteArray(is, (int) entry.getSize());
        } finally {
            is.close();
        }
    }

    public ItemData(String name, boolean notExisting) {
        assert notExisting;
        this.version = -1;
        this.name = name;
        this.data = null;
    }

    public ItemData(String name, short version, byte[] data) {
        this.name = name;
        this.version = version;
        this.data = data;
    }

    public ItemData(String name, short version, int size, InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            Util.copy(is, baos);
            this.data = baos.toByteArray();
            this.name = name;
            this.version = version;
        } finally {
            baos.close();
        }
    }

    public ItemData(ByteBuffer data) {
        byte v = data.get();
        if (v == VERSION) {
            this.version = data.getShort();
            this.name = Util.toStringUTF8(data);
            int len = Util.getFlex(data);
            this.data = new byte[len];
            data.get(this.data);
        } else {
            assert v == NOT_EXISTING;
            this.name = Util.toStringUTF8(data);
        }
    }

    public ItemData(DataInputStream dis) throws IOException {
        byte v = (byte) dis.read();
        if (v == -1) {
            throw new EOFException();
        } else {
            assert v == VERSION;
            this.version = dis.readShort();
            this.name = dis.readUTF();
            int len = dis.readInt();
            this.data = new byte[len];
            int totRead = 0;
            while (totRead < data.length) {
                int read = dis.read(this.data, totRead, data.length - totRead);
                if (read == -1) {
                    throw new EOFException("end");
                } else {
                    totRead += read;
                }
            }
        }
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void write(File libPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(libPath);
        fos.write(data);
        fos.close();
    }

    public URL createURL() {
        try {
            return new URL("wuala", null, 0, name, new ItemURLStream(data));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public ZipEntry createZipEntry() {
        ZipEntry entry = new ZipEntry(name);
        entry.setExtra(Util.toBytes(version, 2));
        return entry;
    }

    public static short extractVersion(ZipEntry entry) {
        return (short) Util.toInt(entry.getExtra(), 0, 2);
    }

    public byte[] getBytes() {
        return data;
    }

    class ItemURLStream extends URLStreamHandler {

        byte[] data;

        public ItemURLStream(byte[] data) {
            this.data = data;
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            return new URLConnection(u) {

                @Override
                public void connect() throws IOException {
                }

                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(data);
                }
            };
        }
    }

    public boolean isExisting() {
        return data != null;
    }

    public static ItemData createFromData(ByteBuffer data) {
        return new ItemData(data);
    }

    public void toBytes(ByteBuffer buffer) {
        if (isExisting()) {
            buffer.put(VERSION);
            buffer.putShort(version);
            Util.toBytesUTF8(name, buffer);
            Util.putFlex(buffer, data.length);
            buffer.put(this.data);
        } else {
            buffer.put(NOT_EXISTING);
            Util.toBytesUTF8(name, buffer);
        }
    }

    public int getMaxLen() {
        return 1 + 2 + name.length() * 4 + 4 + data.length;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ (version << 5);
    }

    @Override
    public boolean equals(Object other) {
        ItemData oi = (ItemData) other;
        return oi.name.equals(name) && oi.version == version;
    }

    @Override
    public String toString() {
        return name + " v" + version;
    }

    public boolean sameContent(ItemData data) {
        if (data.data.length == this.data.length) {
            for (int i = 0; i < this.data.length; i++) {
                if (data.data[i] != this.data[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void write(DataOutputStream out) throws IOException {
        out.write(VERSION);
        out.writeShort(version);
        out.writeUTF(name);
        out.writeInt(data.length);
        out.write(data);
    }
}
