package com.ibm.aglets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PersistentEntry {

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
