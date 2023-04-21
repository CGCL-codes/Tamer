package cn.edu.wuse.musicxml.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileInputContext extends InputContext {

    public FileInputContext(String filepath) {
        super(filepath);
    }

    public String getFilename() {
        return urlString.substring(urlString.lastIndexOf(File.separator) + 1);
    }

    public InputStream getStream() throws Exception {
        return new FileInputStream(urlString);
    }

    public String getBasePath() {
        return urlString.substring(0, urlString.lastIndexOf(File.pathSeparator)) + File.separator;
    }
}
