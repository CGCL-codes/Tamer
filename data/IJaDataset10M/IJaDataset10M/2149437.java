package com.armatiek.infofuze.stream.filesystem.compression.tar;

import java.util.Iterator;
import com.armatiek.infofuze.error.InfofuzeException;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.stream.filesystem.compression.ArchiveFile;

/**
 * An TAR archive file.
 * 
 * @author Maarten Kroon
 */
public class TarArchiveFile extends ArchiveFile {

    public TarArchiveFile(FileIf file) {
        super(file);
    }

    @Override
    public String getMimeType() {
        return "application/x-tar";
    }

    @Override
    public Iterator<FileIf> listFiles() {
        try {
            return new TarArchiveEntryIterator(this, getInputStream());
        } catch (Exception e) {
            throw new InfofuzeException("Could not create TarArchiveEntryIterator", e);
        }
    }
}
