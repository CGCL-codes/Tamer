package org.junithelper.core.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileReader {

    InputStream getResourceAsStream(String name);

    String readAsString(File file) throws IOException;

    String getDetectedEncoding(File file) throws IOException;
}
