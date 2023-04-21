package net.sourceforge.fsync.message;

import net.sourceforge.fsync.filesystem.FileSubstitute;

public class FileNew implements Message {

    private static final long serialVersionUID = 6934550464111273155L;

    private FileSubstitute fileSubstitute;

    public FileNew(FileSubstitute fileSubstitute) {
        super();
        this.fileSubstitute = fileSubstitute;
    }

    public final FileSubstitute getFileSubstitute() {
        return fileSubstitute;
    }
}
