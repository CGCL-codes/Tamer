package com.armatiek.infofuze.stream.filesystem.compression.gzip;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.input.AutoCloseInputStream;
import com.armatiek.infofuze.error.InfofuzeException;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.stream.filesystem.compression.ArchiveFile;

/**
 * An GZIP archive file.
 * 
 * @author Maarten Kroon
 */
public class GzipArchiveFile extends ArchiveFile {

    public GzipArchiveFile(FileIf file) {
        super(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new AutoCloseInputStream(new GzipCompressorInputStream(file.getInputStream()));
    }

    @Override
    public String getMimeType() {
        return "application/x-gzip";
    }

    @Override
    public Iterator<FileIf> listFiles() {
        try {
            return new GzipArchiveEntryIterator(this, getInputStream());
        } catch (Exception e) {
            throw new InfofuzeException("Could not create GzipArchiveEntryIterator", e);
        }
    }
}
