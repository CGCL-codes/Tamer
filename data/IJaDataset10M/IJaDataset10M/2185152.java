package com.xixibase.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;

public interface XixiSocket {

    public ByteBuffer getWriteBuffer();

    public String getHost();

    public boolean trueClose();

    public void close();

    public byte readByte() throws IOException;

    public short readShort() throws IOException;

    public int readInt() throws IOException;

    public long readLong() throws IOException;

    public byte[] read(int len) throws IOException;

    public int read(byte[] b, int off, int len) throws IOException;

    public void write(byte[] b, int off, int len) throws IOException;

    public void flush() throws IOException;

    public void setLastActiveTime(long lastActiveTime);

    public long getLastActiveTime();

    public boolean isBlocking();

    public void configureBlocking(boolean block) throws IOException;

    public int read(ByteBuffer dst) throws IOException;

    public int write(ByteBuffer src) throws IOException;

    public boolean handleRead() throws IOException;

    public boolean handleWrite() throws IOException;

    public void register(Selector sel, int ops, AsyncHandle handle) throws IOException;
}
