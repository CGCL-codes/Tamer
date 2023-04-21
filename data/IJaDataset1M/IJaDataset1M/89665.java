package jp.ne.nifty.iga.midori.shell.dir;

import java.io.*;

/**
 * JMichelle (Java Midori Shell) : path class
 */
public abstract class MdShellDirFileAbstract extends MdShellDirNodeInfo {

    public abstract String getShortName();

    public abstract MdShellDirDirectoryAbstract getPath();

    public abstract InputStream getInputStream();

    public abstract OutputStream getOutputStream();

    public abstract OutputStream getOutputStream(boolean flag);
}
