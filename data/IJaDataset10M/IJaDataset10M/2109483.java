package org.bitdrive.jlan.impl;

import org.alfresco.jlan.server.filesys.AccessDeniedException;
import org.alfresco.jlan.server.filesys.NetworkFile;
import org.alfresco.jlan.smb.SeekType;
import java.io.IOException;

public abstract class ReadOnlyFile extends NetworkFile {

    public void openFile(boolean createFlag) throws IOException {
        if (createFlag) throw new AccessDeniedException("File " + this.getFullName() + " is read-only");
    }

    public void closeFile() throws IOException {
    }

    public ReadOnlyFile(String name) {
        super(name);
    }

    public void writeFile(byte[] buf, int len, int pos, long fileOff) throws IOException {
        throw new AccessDeniedException("File " + this.getFullName() + " is read-only");
    }

    public void truncateFile(long siz) throws IOException {
        throw new AccessDeniedException("File " + this.getFullName() + " is read-only");
    }

    public void flushFile() throws IOException {
        throw new AccessDeniedException("File " + this.getFullName() + " is read-only");
    }

    public long seekFile(long pos, int typ) throws IOException {
        switch(typ) {
            case SeekType.StartOfFile:
                return pos;
            case SeekType.CurrentPos:
                return pos;
            case SeekType.EndOfFile:
                return this.getFileSize();
            default:
                return 0;
        }
    }
}
