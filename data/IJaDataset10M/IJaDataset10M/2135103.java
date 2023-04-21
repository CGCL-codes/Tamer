package org.junithelper.core.file;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junithelper.core.config.Configuration;

class FileWriterCommonsIOImpl implements FileWriter {

    private File file;

    private String encoding = new Configuration().outputFileEncoding;

    public FileWriterCommonsIOImpl(File file) {
        setWriteTarget(file);
    }

    @Override
    public FileWriter setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @Override
    public FileWriter setWriteTarget(File file) {
        this.file = file;
        return this;
    }

    @Override
    public void writeText(String text) throws IOException {
        if (encoding != null) {
            FileUtils.writeStringToFile(file, text, encoding);
        } else {
            FileUtils.writeStringToFile(file, text);
        }
    }

    @Override
    public void writeText(String text, String encoding) throws IOException {
        if (encoding == null || encoding.length() == 0) {
            encoding = this.encoding;
        }
        FileUtils.writeStringToFile(file, text, encoding);
    }
}
