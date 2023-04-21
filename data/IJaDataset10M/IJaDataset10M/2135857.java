package com.genia.toolbox.basics.bean.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.genia.toolbox.basics.bean.DataContainer;
import com.genia.toolbox.basics.bean.DataContainerCache;
import com.genia.toolbox.basics.exception.technical.TechnicalException;
import com.genia.toolbox.basics.manager.ExceptionManager;
import com.genia.toolbox.basics.manager.FileManager;
import com.genia.toolbox.basics.manager.StreamManager;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.base.events.CacheEntryEventListener;
import com.opensymphony.oscache.base.events.CacheGroupEvent;
import com.opensymphony.oscache.base.events.CachePatternEvent;
import com.opensymphony.oscache.base.events.CachewideEvent;

/**
 * implementation of {@link DataContainerCache}.
 */
public class DataContainerCacheImpl implements DataContainerCache, CacheEntryEventListener {

    /**
   * the {@link Cache} to use.
   */
    private Cache cache;

    /**
   * the {@link ExceptionManager} to use.
   */
    private ExceptionManager exceptionManager;

    /**
   * the {@link FileManager} to use.
   */
    private FileManager fileManager;

    /**
   * the {@link StreamManager} to use.
   */
    private StreamManager streamManager;

    /**
   * Event fired when an entry is added to the cache.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cacheEntryAdded(com.opensymphony.oscache.base.events.CacheEntryEvent)
   */
    public void cacheEntryAdded(CacheEntryEvent event) {
    }

    /**
   * Event fired when an entry is flushed from the cache.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cacheEntryFlushed(com.opensymphony.oscache.base.events.CacheEntryEvent)
   */
    public void cacheEntryFlushed(CacheEntryEvent event) {
        cleanEntry(event.getEntry());
    }

    /**
   * Event fired when an entry is removed from the cache.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cacheEntryRemoved(com.opensymphony.oscache.base.events.CacheEntryEvent)
   */
    public void cacheEntryRemoved(CacheEntryEvent event) {
        cleanEntry(event.getEntry());
    }

    /**
   * remove the {@link File} associated to a removed entry.
   * 
   * @param entry
   *          the entry begin removed
   */
    private void cleanEntry(CacheEntry entry) {
        if (entry == null || entry.getContent() == null) {
            return;
        }
        ((FileDataContainer) entry.getContent()).getFile().delete();
    }

    /**
   * Event fired when an entry is updated in the cache.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cacheEntryUpdated(com.opensymphony.oscache.base.events.CacheEntryEvent)
   */
    public void cacheEntryUpdated(CacheEntryEvent event) {
    }

    /**
   * An event that is fired when an entire cache gets flushed.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cacheFlushed(com.opensymphony.oscache.base.events.CachewideEvent)
   */
    public void cacheFlushed(CachewideEvent event) {
    }

    /**
   * Event fired when a group is flushed from the cache.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cacheGroupFlushed(com.opensymphony.oscache.base.events.CacheGroupEvent)
   */
    public void cacheGroupFlushed(CacheGroupEvent event) {
    }

    /**
   * Event fired when a key pattern is flushed from the cache. Note that this
   * event will <em>not</em> be fired if the pattern is <code>null</code> or
   * an empty string, instead the flush request will silently be ignored.
   * 
   * @param event
   *          the object describing the event
   * @see com.opensymphony.oscache.base.events.CacheEntryEventListener#cachePatternFlushed(com.opensymphony.oscache.base.events.CachePatternEvent)
   */
    public void cachePatternFlushed(CachePatternEvent event) {
    }

    /**
   * getter for the cache property.
   * 
   * @return the cache
   */
    public Cache getCache() {
        return cache;
    }

    /**
   * returns the {@link DataContainer} associated to the given cache entry, or
   * <code>null</code> if no such element is in the cache.
   * 
   * @param cacheEntry
   *          the key to retrieve the {@link DataContainer} from the cache.
   * @return the {@link DataContainer} associated to the given cache entry, or
   *         <code>null</code> if no such element is in the cache
   * @see com.genia.toolbox.basics.bean.DataContainerCache#getDataContainer(java.lang.String)
   */
    public DataContainer getDataContainer(String cacheEntry) {
        try {
            FileDataContainer fileDataContainer = (FileDataContainer) getCache().getFromCache(cacheEntry);
            if (!fileDataContainer.getFile().exists()) {
                getCache().removeEntry(cacheEntry);
                return null;
            }
            return fileDataContainer;
        } catch (NeedsRefreshException e) {
            getCache().cancelUpdate(cacheEntry);
            return null;
        }
    }

    /**
   * getter for the exceptionManager property.
   * 
   * @return the exceptionManager
   */
    public ExceptionManager getExceptionManager() {
        return exceptionManager;
    }

    /**
   * getter for the fileManager property.
   * 
   * @return the fileManager
   */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
   * getter for the streamManager property.
   * 
   * @return the streamManager
   */
    public StreamManager getStreamManager() {
        return streamManager;
    }

    /**
   * setter for the cache property.
   * 
   * @param cache
   *          the cache to set
   */
    public void setCache(Cache cache) {
        this.cache = cache;
        cache.addCacheEventListener(this);
    }

    /**
   * set the value associated to a given cache entry.
   * 
   * @param cacheEntry
   *          the key to retrieve the {@link DataContainer} from the cache
   * @param dataContainer
   *          the {@link DataContainer} to keep in the cache
   * @throws TechnicalException
   *           if an error occured
   * @see com.genia.toolbox.basics.bean.DataContainerCache#setDataContainer(java.lang.String,
   *      com.genia.toolbox.basics.bean.DataContainer)
   */
    public void setDataContainer(String cacheEntry, DataContainer dataContainer) throws TechnicalException {
        try {
            getCache().removeEntry(cacheEntry);
            File file = getFileManager().createAutoDeletableTempFile("cachedFile", ".bin");
            getStreamManager().copyStream(dataContainer.getInputStream(), new FileOutputStream(file));
            FileDataContainer fd = new FileDataContainer();
            fd.setFile(file);
            fd.setName(dataContainer.getName());
            fd.setContentType(dataContainer.getContentType());
            getCache().putInCache(cacheEntry, fd);
        } catch (FileNotFoundException e) {
            throw getExceptionManager().convertException(e);
        }
    }

    /**
   * setter for the exceptionManager property.
   * 
   * @param exceptionManager
   *          the exceptionManager to set
   */
    public void setExceptionManager(ExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }

    /**
   * setter for the fileManager property.
   * 
   * @param fileManager
   *          the fileManager to set
   */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
   * setter for the streamManager property.
   * 
   * @param streamManager
   *          the streamManager to set
   */
    public void setStreamManager(StreamManager streamManager) {
        this.streamManager = streamManager;
    }
}
