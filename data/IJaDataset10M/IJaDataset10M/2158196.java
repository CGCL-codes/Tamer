package org.nakedobjects.nof.core.util;

import java.io.InputStream;
import org.nakedobjects.noa.util.ByteDecoderBuffer;

public class DebugByteDecoder extends ByteDecoderBuffer {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DebugByteDecoder.class);

    public DebugByteDecoder(InputStream input) {
        super(input);
    }

    public boolean getBoolean() {
        boolean b = super.getBoolean();
        LOG.debug("boolean: " + b);
        return b;
    }

    public int getByte() {
        int b = super.getByte();
        LOG.debug("byte: " + b);
        return b;
    }

    public byte[] getBytes() {
        byte[] bs = super.getBytes();
        LOG.debug("bytes: " + new String(bs));
        return bs;
    }

    public int getInt() {
        int i = super.getInt();
        LOG.debug("int: " + i);
        return i;
    }

    public String[] getList() {
        String[] strings = super.getList();
        LOG.debug("list: " + strings);
        return strings;
    }

    public long getLong() {
        long l = super.getLong();
        LOG.debug("long: " + l);
        return l;
    }

    public Object getObject() {
        Object object = super.getObject();
        LOG.debug(">>> object");
        return object;
    }

    public Object[] getObjects() {
        Object[] objects = super.getObjects();
        LOG.debug(">>> objects x" + objects.length);
        return objects;
    }

    public String getString() {
        String string = super.getString();
        LOG.debug("string: " + string);
        return string;
    }

    public void end() {
        LOG.debug("<<<  end");
        super.end();
    }
}
