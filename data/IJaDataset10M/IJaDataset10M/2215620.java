package jsr203.sun.nio.fs;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import jsr203.nio.file.FileRef;
import jsr203.nio.file.attribute.NamedAttributeView;

/**
 * Base implementation of NamedAttributeView
 */
abstract class AbstractNamedAttributeView implements NamedAttributeView {

    private volatile FileInfo fileInfo;

    protected FileInfo getFileInfo() {
        FileInfo res = fileInfo;
        if (res == null) throw new IllegalStateException();
        return fileInfo;
    }

    protected void setFileInfo(FileRef file, boolean followLinks) {
        fileInfo = (file == null) ? null : new FileInfo(file, followLinks);
    }

    protected void checkAccess(String file, boolean checkRead, boolean checkWrite) {
        assert checkRead || checkWrite;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            if (checkRead) sm.checkRead(file);
            if (checkWrite) sm.checkWrite(file);
            sm.checkPermission(new RuntimePermission("accessNamedAttributes"));
        }
    }

    @Override
    public final NamedAttributeView bind(FileRef obj) {
        return bind(obj, true);
    }

    /**
     * Returns an Iterator for the list with a remove implementation that
     * attempts to delete the last named attribute returned.
     */
    protected Iterator<String> newIterator(final FileInfo info, final List<String> list) {
        return new Iterator<String>() {

            private int pos = 0;

            private String last = null;

            @Override
            public synchronized boolean hasNext() {
                return pos < list.size();
            }

            @Override
            public String next() {
                synchronized (this) {
                    if (pos < list.size()) {
                        last = list.get(pos++);
                        return last;
                    }
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                String name;
                synchronized (this) {
                    if (last == null) throw new IllegalStateException();
                    name = last;
                    last = null;
                }
                Throwable t = null;
                try {
                    implDelete(info, name);
                } catch (IOException x) {
                    t = x;
                } catch (SecurityException x) {
                    t = x;
                }
                if (t != null) {
                    ConcurrentModificationException cme = new ConcurrentModificationException();
                    cme.initCause(t);
                    throw cme;
                }
            }
        };
    }

    /**
     * Deletes named attribute of the given file.
     */
    abstract void implDelete(FileInfo info, String name) throws IOException;

    @Override
    public final NamedAttributeView delete(String name) throws IOException {
        FileInfo info = getFileInfo();
        implDelete(info, name);
        return this;
    }
}
