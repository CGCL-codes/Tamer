package org.sd_network.vfs;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.sd_network.vfs.db.VfsFile;

/**
 * An abstract file session.
 *
 * @author Masatoshi Sato
 */
public abstract class FileSession {

    /** Logger. */
    private static final Logger _log = Logger.getLogger(FileSession.class.getName());

    /** Session mode. */
    public enum Mode {

        READ(1), WRITE(2), APPEND(3);

        private final int _value;

        Mode(int value) {
            _value = value;
        }

        public int getValue() {
            return _value;
        }
    }

    ;

    /** Session ID. */
    protected final String _sessionID;

    /** Instance of VfsFile. */
    protected VfsFile _vfsFile;

    /** FileSession mode. */
    protected final Mode _mode;

    /** Flag for this session closed. */
    protected boolean _closed;

    protected FileSession(String sessionID, VfsFile vfsFile, Mode mode) {
        _sessionID = sessionID;
        _vfsFile = vfsFile;
        _mode = mode;
        _closed = false;
    }

    public String getID() {
        return _sessionID;
    }

    public VfsFile getFile() {
        return _vfsFile;
    }

    public Mode getMode() {
        return _mode;
    }

    public void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    protected void checkClosed() {
        if (_closed) throw new IllegalStateException("The file session is already closed.");
    }

    public abstract void close() throws VfsIOException;

    public abstract void destroy() throws VfsIOException;
}
