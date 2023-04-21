package org.h2.store.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.h2.util.FileUtils;

/**
 * This class is extends a java.io.RandomAccessFile.
 */
public class FileObjectDisk extends RandomAccessFile implements FileObject {

    private final String name;

    FileObjectDisk(String fileName, String mode) throws FileNotFoundException {
        super(fileName, mode);
        this.name = fileName;
    }

    public void sync() throws IOException {
        getFD().sync();
    }

    public void setFileLength(long newLength) throws IOException {
        FileUtils.setLength(this, newLength);
    }

    public String getName() {
        return name;
    }
}
