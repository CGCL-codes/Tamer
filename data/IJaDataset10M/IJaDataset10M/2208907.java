package net.sourceforge.transumanza.resource.file;

import java.io.File;

public abstract class AbstractFileResource implements FileResource {

    protected String fileName;

    protected File file;

    public File getFile() throws Exception {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
