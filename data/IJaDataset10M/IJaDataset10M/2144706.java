package com.armatiek.infofuze.stream.filesystem.csv;

import java.io.IOException;
import java.io.InputStream;
import com.armatiek.infofuze.source.extractor.CSVFileExtractor;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.stream.filesystem.ProxyFile;

/**
 * A CSV (comma separated values) file.
 * 
 * @author Maarten Kroon
 */
public class CSVFile extends ProxyFile {

    protected CSVFileExtractor extractor;

    public CSVFile(FileIf proxy, CSVFileExtractor extractor) {
        super(proxy);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return super.getInputStream();
    }
}
