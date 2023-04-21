package com.ynhenc.comm.file;

import java.io.*;
import javax.servlet.*;
import com.ynhenc.comm.GisComLib;

public abstract class FileManager extends GisComLib {

    protected FileManager(ServletContext context) {
        super();
        this.context = context;
    }

    public String getRealPath(String src) {
        if (this.context != null) {
            return context.getRealPath(src);
        } else {
            return src;
        }
    }

    public FileInputStream getRealPathInputStream(String path) throws FileNotFoundException {
        File file = this.getRealPathFile(path);
        if (file != null) {
            return new FileInputStream(file);
        } else {
            return null;
        }
    }

    @Override
    public File getRealPathFile(String path) {
        if (!this.isGood(path)) {
            return null;
        } else {
            return new File(this.getRealPath(path));
        }
    }

    private boolean isFileExists(String path) {
        File file = this.getRealPathFile(path);
        return file != null && file.exists();
    }

    public File getTempDir() {
        return this.getForcedDir("/tmp");
    }

    private File getForcedDir(String dir) {
        File file = this.getRealPathFile(dir);
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                return file;
            } else {
                if (file.delete()) {
                    File temp = this.getRealPathFile(dir);
                    if (temp.mkdir()) {
                        return temp;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } else if (file != null) {
            file.mkdir();
            return file;
        } else {
            return null;
        }
    }

    private File getUsersTopDir() {
        return this.getForcedDir("/user");
    }

    private ServletContext context;
}
