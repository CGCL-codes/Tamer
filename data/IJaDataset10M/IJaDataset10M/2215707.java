package net.sf.contrail.vfs.cow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;

/**
 * Metadata stored at the root of a COW file system.
 * Contains...
 * ...the number of the last committed revision
 * 
 * @author Ted Stockwell
 */
public class CowRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String COWROOT = ".cowroot";

    private long _lastCommittedRevision = 0;

    private long _lastCreatedRevision = 0;

    private HashSet<Long> _uncommittedRevisions = new HashSet<Long>();

    private transient FileObject _file;

    public static CowRoot openCowRoot(FileObject parentLayer) throws IOException {
        CowRoot cowRoot = new CowRoot();
        FileObject file = parentLayer.resolveFile(COWROOT);
        if (file.exists()) {
            long maxTime = System.currentTimeMillis() + 30 * 1000;
            for (boolean success = false; !success; ) {
                FileContent content = null;
                ObjectInputStream in = null;
                try {
                    content = file.getContent();
                    in = new ObjectInputStream(content.getInputStream());
                    try {
                        cowRoot = (CowRoot) in.readObject();
                        success = true;
                    } catch (ClassNotFoundException x) {
                        throw new RuntimeException("Error reading root file", x);
                    }
                } catch (IOException x) {
                    if (maxTime < System.currentTimeMillis()) throw x;
                } finally {
                    try {
                        in.close();
                    } catch (Throwable t) {
                    }
                    try {
                        content.close();
                    } catch (Throwable t) {
                    }
                }
            }
        } else {
            file.createFile();
        }
        file.close();
        cowRoot._file = file;
        return cowRoot;
    }

    private CowRoot() {
    }

    public long getLastCommittedRevision() {
        return _lastCommittedRevision;
    }

    public void save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(_file.getContent().getOutputStream());
        try {
            out.writeObject(this);
            out.flush();
        } finally {
            out.close();
        }
    }

    public boolean getIsRevisionWritable(long _revision) {
        return _uncommittedRevisions.contains(_revision);
    }

    public long createNewRevision() throws IOException {
        _lastCreatedRevision++;
        _uncommittedRevisions.add(_lastCommittedRevision);
        save();
        return _lastCreatedRevision;
    }

    public void commit(long revision) throws IOException {
        _uncommittedRevisions.remove(revision);
        save();
    }

    public void rollback(long revision) throws IOException {
        _uncommittedRevisions.remove(revision);
        save();
    }
}
