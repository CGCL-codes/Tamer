package com.coldcore.coloradoftp.filesystem.impl;

import com.coldcore.coloradoftp.filesystem.FailedActionException;
import com.coldcore.coloradoftp.filesystem.FailedActionReason;
import com.coldcore.coloradoftp.filesystem.FileSystem;
import com.coldcore.coloradoftp.filesystem.ListingFile;
import com.coldcore.coloradoftp.session.Session;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * @see com.coldcore.coloradoftp.filesystem.FileSystem
 *
 * A dummy filesystem implementation which does nothing.
 * This is required to start up the FTP server without additional file system plugins.
 */
public class DummyFileSystem implements FileSystem {

    public String getCurrentDirectory(Session userSession) throws FailedActionException {
        return "/";
    }

    public String getParent(String path, Session userSession) throws FailedActionException {
        return "/";
    }

    public String toAbsolute(String path, Session userSession) throws FailedActionException {
        return "/";
    }

    public ListingFile getPath(String path, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public Set<ListingFile> listDirectory(String dir, Session userSession) throws FailedActionException {
        return new HashSet<ListingFile>();
    }

    public String changeDirectory(String dir, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public void deletePath(String path, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public String createDirectory(String dir, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public String renamePath(String from, String to, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public ReadableByteChannel readFile(String filename, long position, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public WritableByteChannel saveFile(String filename, boolean append, Session userSession) throws FailedActionException {
        throw new FailedActionException(FailedActionReason.NOT_IMPLEMENTED);
    }

    public String getFileSeparator() {
        return "/";
    }
}
