package com.lingway.webapp.antui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWrapper {

    private String name;

    private String type;

    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Retrieves content from tmp-file.
	 */
    public byte[] getContent() throws IOException {
        File file = new File(path);
        byte[] content = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(content);
        inputStream.close();
        return content;
    }

    public File getFile() {
        return new File(path);
    }

    /**
	 * Stores content in a tmp-file.
	 * <p/>
	 * The current milliseconds are used as a "semi-unique" prefix for
	 * the temporary file. This works usually fine for demonstration
	 * purposes without much concurrency. Please use a synchronized
	 * "real" unique sequence for production use.
	 */
    public void setContent(byte[] content) throws IOException {
        File tmpFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), null);
        tmpFile.deleteOnExit();
        path = tmpFile.getCanonicalPath();
        FileOutputStream outputStream = new FileOutputStream(tmpFile);
        outputStream.write(content);
        outputStream.close();
    }

    public long getSize() {
        return new File(path).length();
    }

    public String toString() {
        return name + " (" + type + ")" + " [" + getSize() / 1024 + " KB]";
    }
}
