package com.goodcodeisbeautiful.archtea.cache;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manage some objects as a local file.
 * @author hata
 * 
 */
public class FileCacheManager {

    private static final Object LOCK = new Object();

    private static final String DEFAULT_NAME = "_default";

    protected static volatile Map<String, FileCacheManager> m_managers;

    private String m_name;

    private String m_cacheRootPath;

    private FileCacheMap m_fileCacheList;

    private boolean m_autoFlush;

    /**
     * Get default FileCacheManager instance.
     * @return an instance.
     */
    public static FileCacheManager getInstance() {
        return getInstance(DEFAULT_NAME);
    }

    /**
     * Get FileCacheManager instance. This method will manage
     * multi FileCacheManager instances.
     * @param name is a name of returned instance.
     * @return an instance. If there is not any instance
     * which have the name as <code>name</code>, a new instance
     * will be created.
     */
    public static FileCacheManager getInstance(String name) {
        if (m_managers == null) {
            synchronized (LOCK) {
                if (m_managers == null) {
                    m_managers = new HashMap<String, FileCacheManager>();
                }
            }
        }
        Object o = m_managers.get(name);
        if (o == null) {
            synchronized (LOCK) {
                o = m_managers.get(name);
                if (o == null) {
                    m_managers.put(name, (FileCacheManager) (o = new FileCacheManager(name)));
                }
            }
        }
        return (FileCacheManager) o;
    }

    /**
     * Call once to initialize this instance.
     * @param configPath is a configuration file path to
     * initialize this instance.
     */
    public void init(String configPath) throws FileCacheException {
        if (configPath == null) return;
        File f = new File(configPath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                throw new FileCacheException("archtea.cache.CannotCreateDirectory", new Object[] { configPath });
            }
        }
        m_cacheRootPath = configPath;
        m_autoFlush = true;
        m_fileCacheList = new FileCacheMap(m_cacheRootPath + File.separator + m_name);
    }

    /**
     * Get a cached object as File . If this method returns null,
     * it means the cached file are expired, removed, or not exist.
     * In this case, client will call put and then tried to get it again.
     * @param key is a key object to get an object.
     * @return File instance if a object for a key was found .
     * Otherwise returns null.
     */
    public FileCacheObject get(FileCacheKey key) {
        String keyString = key.getKeyString();
        FileCacheObject fco = (FileCacheObject) m_fileCacheList.get(keyString);
        if (fco != null && key.getTimestamp().equals(fco.getTimestamp())) {
            return fco;
        } else if (fco != null) {
            m_fileCacheList.remove(keyString);
        }
        return null;
    }

    /**
     * Put a key and contents object as an InputStream instance.
     * @param key is a key object to store contents.
     * @param in is an instance of InputStream to store contents.
     */
    public void put(FileCacheKey key, InputStream in) throws FileCacheException {
        String keyString = key.getKeyString();
        int lastIndex = keyString.lastIndexOf("/");
        String name = (lastIndex != -1) ? keyString.substring(lastIndex + 1, keyString.length()) : keyString;
        FileCacheObject fco = FileCacheObject.createFileCacheObject(m_fileCacheList, name, key.getTimestamp(), in);
        m_fileCacheList.put(keyString, fco);
        if (m_autoFlush) m_fileCacheList.flush();
    }

    public void clearAll() throws FileCacheException {
        clearAll(false);
    }

    public void clearAll(boolean force) throws FileCacheException {
        m_fileCacheList.clear();
        m_fileCacheList.clearTrash(force);
        if (m_autoFlush) m_fileCacheList.flush();
    }

    /**
     * This class has a private constructor not to be created other classes.
     */
    private FileCacheManager() {
        this(DEFAULT_NAME);
    }

    /**
     * This class has a private constructor not to be created other classes.
     * @param name is a name for this cache manager.
     */
    private FileCacheManager(String name) {
        m_name = name;
    }
}
