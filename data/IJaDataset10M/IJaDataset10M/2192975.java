package org.spantus.core.extractor.dao;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import org.spantus.core.extractor.IExtractorInputReader;

public interface ReaderDao {

    public void write(IExtractorInputReader reader, File file);

    public void write(IExtractorInputReader holder, OutputStream outputStream);

    public IExtractorInputReader read(File file);

    public IExtractorInputReader read(InputStream inputStream);
}
