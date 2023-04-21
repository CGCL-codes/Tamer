package au.edu.educationau.opensource.rome.diskcache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.SyndFeedInfo;

public class DiskBasedFeedInfoCache implements FeedFetcherCache {

    private static final String FILE_PREFIX = "_feedinfo";

    Logger logger = Logger.getLogger(getClass().getName());

    private volatile String cachePath = null;

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public DiskBasedFeedInfoCache() {
        cachePath = System.getProperty("java.io.tmpdir") + File.separator + "feedinfocache" + File.separator;
        initCache();
    }

    /**
	 * MUST be called ONLY when lock.writeLock().lock() has been called
	 */
    private void initCacheLocked() {
        logger.info("Feed Info Cache path set to " + cachePath);
        File f = new File(cachePath);
        if (f.exists() && !f.isDirectory()) {
            throw new RuntimeException("Configured cache directory already exists as a file: " + cachePath);
        }
        if (!f.exists() && !f.mkdirs()) {
            throw new RuntimeException("Could not create directory " + cachePath);
        }
    }

    private void initCache() {
        lock.writeLock().lock();
        try {
            initCacheLocked();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getCachePath() {
        lock.readLock().lock();
        try {
            return cachePath;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setCachePath(String cachePath) {
        lock.writeLock().lock();
        try {
            this.cachePath = cachePath;
            initCacheLocked();
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected String buildCachePath(URL url) {
        return CacheUtils.buildCachePath(url, cachePath, FILE_PREFIX);
    }

    public SyndFeedInfo getFeedInfo(URL url) {
        lock.readLock().lock();
        try {
            SyndFeedInfo info = null;
            String fileName = buildCachePath(url);
            File file = new File(fileName);
            if (file.exists()) {
                FileInputStream fis = null;
                boolean deleteFile = false;
                try {
                    fis = new FileInputStream(fileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    info = (SyndFeedInfo) ois.readObject();
                } catch (InvalidClassException ice) {
                    logger.warn("Invalid class reading from cache - cached item will be ignored");
                    deleteFile = true;
                } catch (Exception e) {
                    logger.error("Attempting to read from cache", e);
                    throw new RuntimeException("Attempting to read from cache", e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            logger.warn("error closing file", e);
                        }
                    }
                }
                if (deleteFile) {
                    file.delete();
                }
                if (info == null) {
                    logger.info("Cache miss for url " + url.toExternalForm());
                }
            } else {
                logger.debug("Cache miss for " + url.toString());
            }
            return info;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setFeedInfo(URL url, SyndFeedInfo feedInfo) {
        lock.readLock().lock();
        try {
            String fileName = buildCachePath(url);
            FileOutputStream fos;
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(feedInfo);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                logger.error("Error writing cache", e);
                throw new RuntimeException("Attempting to write to cache", e);
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
	 * NOTE: <b>This will lock the cache while it is in progress. It maybe quite
	 * slow!</b>
	 */
    public void clear() {
        logger.info("Clearing feed info cache in " + cachePath);
        lock.writeLock().lock();
        try {
            File cacheDir = new File(this.cachePath);
            if (cacheDir.exists() && cacheDir.isDirectory()) {
                deleteAllCacheFiles(cacheDir);
            }
            initCacheLocked();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void deleteAllCacheFiles(File cacheDir) {
        String[] filenames = cacheDir.list();
        for (String name : filenames) {
            File f = new File(cacheDir.getAbsolutePath() + File.separator + name);
            if (f.isDirectory()) {
                deleteAllCacheFiles(f);
                if (f.list().length == 0) {
                    if (!f.delete()) {
                        logger.warn("Could not delete directory " + f.getAbsolutePath());
                    }
                }
            } else {
                if (name.startsWith(FILE_PREFIX)) {
                    if (!f.delete()) {
                        logger.warn("Could not delete file " + f.getAbsolutePath());
                    }
                }
            }
        }
    }

    public SyndFeedInfo remove(URL feedUrl) {
        lock.readLock().lock();
        try {
            SyndFeedInfo result = getFeedInfo(feedUrl);
            String fileName = buildCachePath(feedUrl);
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                File file = new File(fileName);
                if (file.exists() && !file.delete()) {
                    throw new RuntimeException("Could not delete file " + fileName);
                }
                return result;
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
