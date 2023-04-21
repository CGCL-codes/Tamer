package org.jdiagnose.remote.file;

import java.io.File;

/**
 * @author jamie
 */
public class TmpFileFactory implements FileFactory {

    public static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");

    public File getFile(String fileName) {
        return new File(JAVA_IO_TMPDIR, fileName);
    }
}
