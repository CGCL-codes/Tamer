package com.goodcodeisbeautiful.archtea.io.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 *
 * @author hata
 *
 */
public interface DataContainer {

    public InputStream getInputStream() throws IOException;

    public Reader getReader(DataContainerReaderType type) throws IOException;

    public String getContentType();

    public String getCharset();
}
