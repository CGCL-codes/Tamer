package org.apache.jackrabbit.core.query.lucene;

import org.apache.lucene.util.Constants;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.IndexInput;
import com.simconomy.server.content.GenericDao;
import com.simconomy.server.content.hibernate.LuceneIndex;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * This is a wrapper class to provide lock creation in the index directory.
 * <p/>
 * As of lucene 1.3 lock files are created in the users temp directory, which is
 * not very user friendly when the system crashed. One has to look up the temp
 * directory and find out which lock file belongs to which lucene index.
 * <p/>
 * This wrapper class delegates most operations to the default FSDirectory
 * implementation of lucene but has its own makeLock() implementation.
 */
class FSDirectory extends Directory {

    /**
     * Flag indicating whether locks are disabled
     */
    private static final boolean DISABLE_LOCKS = Boolean.getBoolean("disableLuceneLocks") || Constants.JAVA_1_1;

    /**
     * The directory where this index is located
     */
    private final String directory;

    /**
     * internal ref count for cached FSDirectories
     */
    private int refCount = 0;

    private GenericDao genericDao = null;

    /**
     * map where the cached FSDirectories are stored
     */
    private static final Map<String, FSDirectory> directories = new HashMap<String, FSDirectory>();

    /**
     * Creates a new FSDirectory based on a lucene FSDirectory instance.
     *
     * @param delegatee the lucene FSDirectory instance.
     * @param directory the directory where this index is located.
     */
    private FSDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Returns the directory instance for the named location.
     * <p/>
     * <p>Directories are cached, so that, for a given canonical path, the same
     * FSDirectory instance will always be returned.  This permits
     * synchronization on directories.
     *
     * @param file   the path to the directory.
     * @param create if true, create, or erase any existing contents.
     * @return the FSDirectory for the named file.
     */
    public static FSDirectory getDirectory(File file, boolean create) throws IOException {
        FSDirectory dir;
        String result = file.getCanonicalPath().replaceAll("/", ".");
        result = result.replaceAll("\\", ".");
        if (result.startsWith(".")) {
            result = result.substring(1);
        }
        if (result.endsWith(".")) {
            result = result.substring(result.length(), result.length());
        }
        synchronized (directories) {
            dir = (FSDirectory) directories.get(result);
            if (dir == null) {
                dir = new FSDirectory(result);
                directories.put(result, dir);
            }
        }
        synchronized (dir) {
            dir.refCount++;
        }
        return dir;
    }

    /**
     * Creates a lock file in the current index directory.
     *
     * @param name the name of the lock file.
     * @return a Lock object with the given name.
     */
    public Lock makeLock(final String name) {
        return new Lock() {

            public boolean obtain() throws IOException {
                if (DISABLE_LOCKS) {
                    return true;
                }
                LuceneIndex luceneIndex = (LuceneIndex) genericDao.load(LuceneIndex.class, name);
                luceneIndex.setLocked(true);
                genericDao.saveOrUpdate(luceneIndex);
                return true;
            }

            public void release() {
                if (DISABLE_LOCKS) {
                    return;
                }
                LuceneIndex luceneIndex = (LuceneIndex) genericDao.load(LuceneIndex.class, name);
                luceneIndex.setLocked(false);
                genericDao.saveOrUpdate(luceneIndex);
            }

            public boolean isLocked() {
                if (DISABLE_LOCKS) {
                    return false;
                }
                LuceneIndex luceneIndex = (LuceneIndex) genericDao.load(LuceneIndex.class, name);
                return luceneIndex.isLocked();
            }

            public String toString() {
                LuceneIndex luceneIndex = (LuceneIndex) genericDao.load(LuceneIndex.class, name);
                return "Lock@" + luceneIndex.getId();
            }
        };
    }

    /**
     * @inheritDoc
     */
    public synchronized void close() throws IOException {
        if (--refCount <= 0) {
            synchronized (directories) {
                directories.remove(directory);
            }
        }
    }

    private String createId(String name) {
        String result = null;
        result = directory;
        result = result + "." + name;
        return result;
    }

    /**
     * @inheritDoc
     */
    public IndexOutput createOutput(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        HibernateIndexOutput indexOutput = new HibernateIndexOutput(luceneIndex);
        return indexOutput;
    }

    /**
     * @inheritDoc
     */
    public void deleteFile(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        if (luceneIndex == null) {
            return;
        }
        genericDao.delete(luceneIndex);
    }

    /**
     * @inheritDoc
     */
    public boolean fileExists(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        if (luceneIndex == null) {
            return false;
        }
        return true;
    }

    /**
     * @inheritDoc
     */
    public long fileLength(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        if (luceneIndex == null) {
            return 0;
        }
        return luceneIndex.getSize();
    }

    /**
     * @inheritDoc
     */
    public long fileModified(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        return luceneIndex.getLastModified().getTime();
    }

    /**
     * @inheritDoc
     */
    public String[] list() throws IOException {
        List<LuceneIndex> indexList = (List<LuceneIndex>) genericDao.findAllByParam(LuceneIndex.class, "", directory);
        String[] list = new String[indexList.size()];
        for (int i = 0; i < indexList.size(); i++) {
            LuceneIndex luceneIndex = (LuceneIndex) indexList.get(i);
            String id = luceneIndex.getId();
            list[i] = id.substring(directory.length() + 2);
        }
        return list;
    }

    /**
     * @inheritDoc
     */
    public IndexInput openInput(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        HibernateIndexInput indexInput = new HibernateIndexInput(luceneIndex);
        return indexInput;
    }

    /**
     * @inheritDoc
     */
    public void renameFile(String from, String to) throws IOException {
        LuceneIndex fromLuceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(from));
        createOutput(to);
        LuceneIndex toLuceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(to));
        toLuceneIndex.setData(fromLuceneIndex.getData());
        toLuceneIndex.setLastModified(fromLuceneIndex.getLastModified());
        toLuceneIndex.setSize(fromLuceneIndex.getSize());
        toLuceneIndex.setLastModified(new Date());
        fromLuceneIndex.setDeleted(true);
        fromLuceneIndex.setLastModified(new Date());
        genericDao.update(toLuceneIndex);
        genericDao.update(fromLuceneIndex);
        genericDao.flush();
        genericDao.delete(fromLuceneIndex);
    }

    /**
     * @inheritDoc
     */
    public void touchFile(String name) throws IOException {
        LuceneIndex luceneIndex = (LuceneIndex) genericDao.get(LuceneIndex.class, createId(name));
        if (luceneIndex != null) {
            return;
        }
        luceneIndex = new LuceneIndex(createId(name), directory);
        genericDao.save(luceneIndex);
    }
}
