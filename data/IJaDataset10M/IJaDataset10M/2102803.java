package net.sf.jaybox.ftp;

import java.io.*;
import java.net.*;
import net.sf.jaybox.iso.*;
import net.sf.jaybox.common.*;

public class FtpExtractReader implements ReaderCallback, FileReaderCallback {

    private String base;

    private int depth;

    private FtpConnection connection;

    private FtpUpload uploader;

    private Progress progress;

    public FtpExtractReader(String base, String target, int port, String username, String password) throws IOException {
        this(base, target, port, username, password, null);
    }

    public FtpExtractReader(String base, String target, int port, String username, String password, InetAddress local_bind) throws IOException {
        this.base = base;
        this.depth = 0;
        this.progress = null;
        connection = new FtpConnection(target, port, username, password, local_bind);
        connection.createAndEnter(base);
        uploader = null;
    }

    public void onEnter(String path, String dir) throws IOException {
        if (dir.equals("")) return;
        depth++;
        connection.mkdir(dir);
        if (!connection.cwd(dir)) throw new IOException("Unable to enter directort " + dir);
    }

    public void onLeave(String path, String dir) throws IOException {
        if (dir.equals("")) return;
        if (depth == 0) throw new IOException("INTERNAL ERROR: directory pop underflow");
        depth--;
        if (!connection.cwd("..")) throw new IOException("Server error");
    }

    public void onFile(ReadHelper rh, String path, DirHeader dh) throws IOException {
        System.out.println("   " + dh.name + ", " + dh.size + " bytes" + ", offset = " + dh.start);
        new FileCopier(rh, dh, this);
    }

    public void onExit() {
        try {
            connection.logout();
        } catch (IOException _) {
        }
    }

    public void setProgress(Progress p) {
        progress = p;
    }

    public boolean startFile(String filename, long size) throws IOException {
        uploader = new FtpUpload(connection, filename);
        if (progress != null) {
            progress.current_files++;
            progress.current_name = filename;
        }
        return true;
    }

    public void writeChunk(byte[] data, int len) throws IOException {
        uploader.write(data, 0, len);
        if (progress != null) progress.current_bytes += len;
    }

    public void endFile() throws IOException {
        uploader.close();
    }
}
