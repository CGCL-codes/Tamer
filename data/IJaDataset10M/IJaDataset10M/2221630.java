package org.apache.commons.vfs.cache;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.AbstractLinkedMap;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VfsLog;
import org.apache.commons.vfs.util.Messages;

/**
 * This implementation caches every file using {@link LRUMap}.<br>
 * The default constructor uses a LRU size of 100 per filesystem.
 *
 * @author <a href="mailto:imario@apache.org">Mario Ivankovits</a>
 * @version $Revision: 480428 $ $Date: 2006-11-28 22:15:24 -0800 (Tue, 28 Nov 2006) $
 */
public class LRUFilesCache extends AbstractFilesCache {

    /**
     * The logger to use.
     */
    private Log log = LogFactory.getLog(LRUFilesCache.class);

    private final Map filesystemCache = new HashMap(10);

    private final int lruSize;

    private class MyLRUMap extends LRUMap {

        final FileSystem filesystem;

        public MyLRUMap(final FileSystem filesystem, int size) {
            super(size, true);
            this.filesystem = filesystem;
        }

        protected boolean removeLRU(final AbstractLinkedMap.LinkEntry linkEntry) {
            synchronized (LRUFilesCache.this) {
                FileObject file = (FileObject) linkEntry.getValue();
                if (file.isAttached() || file.isContentOpen()) {
                    return false;
                }
                if (super.removeLRU(linkEntry)) {
                    try {
                        file.close();
                    } catch (FileSystemException e) {
                        VfsLog.warn(getLogger(), log, Messages.getString("vfs.impl/LRUFilesCache-remove-ex.warn"), e);
                    }
                    Map files = (Map) filesystemCache.get(filesystem);
                    if (files.size() < 1) {
                        filesystemCache.remove(filesystem);
                    }
                    return true;
                }
                return false;
            }
        }
    }

    /**
     * Default constructor. Uses a LRU size of 100 per filesystem.
     */
    public LRUFilesCache() {
        this(100);
    }

    /**
     * Set the desired LRU size.
     *
     * @param lruSize the LRU size
     */
    public LRUFilesCache(int lruSize) {
        this.lruSize = lruSize;
    }

    public void putFile(final FileObject file) {
        synchronized (this) {
            Map files = getOrCreateFilesystemCache(file.getFileSystem());
            files.put(file.getName(), file);
        }
    }

    public FileObject getFile(final FileSystem filesystem, final FileName name) {
        synchronized (this) {
            Map files = getOrCreateFilesystemCache(filesystem);
            return (FileObject) files.get(name);
        }
    }

    public void clear(final FileSystem filesystem) {
        synchronized (this) {
            Map files = getOrCreateFilesystemCache(filesystem);
            files.clear();
            filesystemCache.remove(filesystem);
        }
    }

    protected Map getOrCreateFilesystemCache(final FileSystem filesystem) {
        Map files = (Map) filesystemCache.get(filesystem);
        if (files == null) {
            files = new MyLRUMap(filesystem, lruSize);
            filesystemCache.put(filesystem, files);
        }
        return files;
    }

    public void close() {
        super.close();
        synchronized (this) {
            filesystemCache.clear();
        }
    }

    public void removeFile(final FileSystem filesystem, final FileName name) {
        synchronized (this) {
            Map files = getOrCreateFilesystemCache(filesystem);
            files.remove(name);
            if (files.size() < 1) {
                filesystemCache.remove(filesystem);
            }
        }
    }

    public void touchFile(final FileObject file) {
        getFile(file.getFileSystem(), file.getName());
    }
}
