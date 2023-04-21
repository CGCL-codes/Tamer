package net.sf.refactorit.refactorings.undo;

import net.sf.refactorit.vfs.Source;

public class SourceHeader extends SourceInfo {

    int fileLength;

    long lastModified;

    String fileAbsolutePath;

    public String getAbsolutePath() {
        return fileAbsolutePath;
    }

    public SourceHeader(final Source dir, String newName) {
        super(dir, newName);
        fileAbsolutePath = dir.getAbsolutePath();
        lastModified = dir.lastModified();
    }

    public SourceHeader(final Source source) {
        super(source);
        fileAbsolutePath = source.getAbsolutePath();
        fileLength = (int) source.length();
        lastModified = source.lastModified();
    }

    public boolean equals(Object obj) {
        SourceHeader header = (SourceHeader) obj;
        return getAbsolutePath().equals(header.getAbsolutePath()) && fileLength == header.fileLength;
    }

    public String toString() {
        return getAbsolutePath() + " length: " + fileLength;
    }
}
