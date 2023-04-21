package org.why;

import org.why.parser.StackTrace;
import org.why.parser.StackTraceParser;

public class DefaultFileSystem implements FileSystem {

    private StackTraceParser stackTraceParser;

    private FileReader reader;

    private SourceFileParser sourceParser;

    public DefaultFileSystem(StackTraceParser stackTraceParser, FileReader reader, SourceFileParser sourceParser) {
        this.stackTraceParser = stackTraceParser;
        this.reader = reader;
        this.sourceParser = sourceParser;
    }

    public StackTrace readStackTrace(String path) {
        return stackTraceParser.parse(reader.read(path));
    }

    public SourceFile readSourceFile(FileName filename) {
        return sourceParser.parse(reader.read(filename.toString()));
    }
}
