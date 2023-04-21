package com.dalsemi.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 *
 * @author Yannick Poirier 
 */
public class DSFile extends File implements Serializable {

    public DSFile(java.lang.String path) {
        super(path);
    }

    public int executeFile(InputStream stdin, OutputStream stdout, OutputStream stderr, String[] args, String[] env, boolean foreground, java.lang.String processName) throws IOException {
        Process proc = Runtime.getRuntime().exec(this.getAbsolutePath(), env);
        byte[] buffer = new byte[300];
        int length = proc.getInputStream().read(buffer, 0, 300);
        while (length != -1) {
            stdout.write(buffer, 0, length);
            length = proc.getInputStream().read(buffer, 0, 300);
        }
        stdout.flush();
        return 0;
    }
}
