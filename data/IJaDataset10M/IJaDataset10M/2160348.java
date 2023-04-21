package com.loribel.tools.file;

import java.io.File;

/**
 * @author Gregory Borelli
 */
public class GB_File2Impl implements GB_File2 {

    private String id;

    private File file;

    private File fileDest;

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public File getDestFile() {
        return fileDest;
    }
}
