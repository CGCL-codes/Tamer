package com.ingenta.clownbike;

import java.io.*;

public class TypedFile {

    private String _path;

    private String _contentType;

    public TypedFile(String path, String contentType) {
        _path = path;
        _contentType = contentType;
    }

    public TypedFile(File file, String contentType) throws IOException {
        _path = file.getCanonicalPath();
        _contentType = contentType;
    }

    public String getPath() {
        return _path;
    }

    public File getFile() {
        return new File(_path);
    }

    public String getContentType() {
        return _contentType;
    }
}
