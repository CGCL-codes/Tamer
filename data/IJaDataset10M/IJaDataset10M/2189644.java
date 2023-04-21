package com.mindbright.sshcommon;

import java.io.InputStream;
import java.io.OutputStream;

public interface SSHConsoleRemote {

    public boolean command(String command);

    public boolean shell();

    public void close();

    public void changeStdOut(OutputStream out);

    public OutputStream getStdIn();

    public InputStream getStdOut();
}
