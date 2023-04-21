package com.jcraft.jsch;

public interface SftpProgressMonitor {

    public static final int PUT = 0;

    public static final int GET = 1;

    void init(int op, String src, String dest, long max);

    boolean count(long count);

    void end();
}
