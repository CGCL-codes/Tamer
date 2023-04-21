package com.armatiek.infofuze.stream.filesystem.webdav;

import java.util.Date;
import java.util.List;
import com.armatiek.infofuze.config.Definitions.TransformMode;
import com.armatiek.infofuze.source.extractor.FileExtractor;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.stream.filesystem.FileSystemReader;

/**
 * @author Maarten Kroon
 *
 */
public class WebDAVReader extends FileSystemReader {

    public WebDAVReader(FileIf[] files, List<FileExtractor> fileExtractors, TransformMode transformMode, long lastIndexed, String systemId, String publicId) {
        super(files, fileExtractors, transformMode, lastIndexed, systemId, publicId);
    }

    @Override
    protected void addFileAttributes(FileIf file, StringBuilder buffer) {
        WebDAVFile webDAVFile = (WebDAVFile) file;
        buffer.append(" created=\"" + dateFormat.format(new Date(webDAVFile.created())) + "\"");
    }
}
