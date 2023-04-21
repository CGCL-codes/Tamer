package com.ecyrd.jspwiki.providers;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ecyrd.jspwiki.*;
import com.ecyrd.jspwiki.attachment.Attachment;
import com.ecyrd.jspwiki.attachment.AttachmentManager;
import com.ecyrd.jspwiki.util.ClassUtil;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

public class CachingAttachmentProvider implements WikiAttachmentProvider {

    private static final Logger log = Logger.getLogger(CachingAttachmentProvider.class.getName());

    private WikiAttachmentProvider m_provider;

    public static final String WIKI_PAGE_ATTACHMENTS_MAP = "WIKI_PAGE_ATTACHMENTS";

    public static final String ATTACHMENT_NAME_ATTACHMENT_MAP = "ATTACHMENT_NAME_ATTACHMENT";

    /**
     *  The cache contains Collection objects which contain Attachment objects.
     *  The key is the parent wiki page name (String).
     */
    private Map<String, Collection<Attachment>> m_cache;

    private long m_cacheMisses = 0;

    private long m_cacheHits = 0;

    /**
     * This cache contains Attachment objects and is keyed by attachment name.
     * This provides for quickly giving recently changed attachments (for the RecentChanges plugin)
     */
    private Map<String, Attachment> m_attCache;

    private Cache cache;

    /** The extension to append to directory names to denote an attachment directory. */
    public static final String DIR_EXTENSION = "-att";

    /** Property that supplies the directory used to store attachments. */
    public static final String PROP_STORAGEDIR = "jspwiki.basicAttachmentProvider.storageDir";

    private int m_refreshPeriod = 60 * 10;

    private boolean m_gotall = false;

    /**
     * {@inheritDoc}
     */
    public void initialize(WikiEngine engine, Properties properties) throws NoRequiredPropertyException, IOException {
        log.log(Level.INFO, "Initing CachingAttachmentProvider");
        CacheFactory cacheFactory = null;
        try {
            cacheFactory = CacheManager.getInstance().getCacheFactory();
            Map props = new HashMap();
            props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
            cache = cacheFactory.createCache(props);
        } catch (CacheException e) {
            log.log(Level.SEVERE, "Exception in getting the Cache", e);
        }
        m_cache = (Map<String, Collection<Attachment>>) cache.get(WIKI_PAGE_ATTACHMENTS_MAP);
        if (m_cache == null) {
            m_cache = new HashMap<String, Collection<Attachment>>();
        }
        m_attCache = (Map<String, Attachment>) cache.get(ATTACHMENT_NAME_ATTACHMENT_MAP);
        if (m_attCache == null) {
            m_attCache = new HashMap<String, Attachment>();
        }
        String classname = WikiEngine.getRequiredProperty(properties, AttachmentManager.PROP_PROVIDER);
        try {
            Class providerclass = ClassUtil.findClass("com.ecyrd.jspwiki.providers", classname);
            m_provider = (WikiAttachmentProvider) providerclass.newInstance();
            log.log(Level.INFO, "Initializing real provider class " + m_provider);
            m_provider.initialize(engine, properties);
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Unable to locate provider class " + classname, e);
            throw new IllegalArgumentException("no provider class");
        } catch (InstantiationException e) {
            log.log(Level.SEVERE, "Unable to create provider class " + classname, e);
            throw new IllegalArgumentException("faulty provider class");
        } catch (IllegalAccessException e) {
            log.log(Level.SEVERE, "Illegal access to provider class " + classname, e);
            throw new IllegalArgumentException("illegal provider class");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void putAttachmentData(Attachment att, InputStream data) throws ProviderException, IOException {
        m_provider.putAttachmentData(att, data);
        m_cache.remove(att.getParentName());
        att.setLastModified(new Date());
        m_attCache.put(att.getName(), att);
        cache.put(WIKI_PAGE_ATTACHMENTS_MAP, m_cache);
        cache.put(ATTACHMENT_NAME_ATTACHMENT_MAP, m_attCache);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getAttachmentData(Attachment att) throws ProviderException, IOException {
        return m_provider.getAttachmentData(att);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Collection listAttachments(WikiPage page) throws ProviderException {
        log.log(Level.INFO, "Listing attachments for " + page);
        Collection<Attachment> c = (Collection<Attachment>) m_cache.get(page.getName());
        if (c != null) {
            log.log(Level.INFO, "LIST from cache, " + page.getName() + ", size=" + c.size());
            m_cacheHits++;
            return cloneCollection(c);
        }
        log.log(Level.INFO, "list NOT in cache, " + page.getName());
        refresh(page);
        return new ArrayList();
    }

    private <T> Collection<T> cloneCollection(Collection<T> c) {
        ArrayList<T> list = new ArrayList<T>();
        list.addAll(c);
        return list;
    }

    /**
     * {@inheritDoc}
     */
    public Collection findAttachments(QueryItem[] query) {
        return m_provider.findAttachments(query);
    }

    /**
     * {@inheritDoc}
     */
    public List listAllChanged(Date timestamp) throws ProviderException {
        List all = null;
        if (m_gotall == false) {
            all = m_provider.listAllChanged(timestamp);
            synchronized (this) {
                for (Iterator i = all.iterator(); i.hasNext(); ) {
                    Attachment att = (Attachment) i.next();
                    m_attCache.put(att.getName(), att);
                    cache.put(ATTACHMENT_NAME_ATTACHMENT_MAP, m_attCache);
                }
                m_gotall = true;
            }
        } else {
        }
        return all;
    }

    /**
     *  Simply goes through the collection and attempts to locate the
     *  given attachment of that name.
     *
     *  @return null, if no such attachment was in this collection.
     */
    private Attachment findAttachmentFromCollection(Collection c, String name) {
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            Attachment att = (Attachment) i.next();
            if (name.equals(att.getFileName())) {
                return att;
            }
        }
        return null;
    }

    /**
     *  Refreshes the cache content and updates counters.
     *
     *  @return The newly fetched object from the provider.
     */
    @SuppressWarnings("unchecked")
    private final Collection<Attachment> refresh(WikiPage page) throws ProviderException {
        m_cacheMisses++;
        Collection<Attachment> c = m_provider.listAttachments(page);
        m_cache.put(page.getName(), c);
        cache.put(WIKI_PAGE_ATTACHMENTS_MAP, m_cache);
        return c;
    }

    /**
     * {@inheritDoc}
     */
    public Attachment getAttachmentInfo(WikiPage page, String name, int version) throws ProviderException {
        if (log.isLoggable(Level.INFO)) {
            log.log(Level.INFO, "Getting attachments for " + page + ", name=" + name + ", version=" + version);
        }
        if (version != WikiProvider.LATEST_VERSION) {
            log.log(Level.INFO, "...we don't cache old versions");
            return m_provider.getAttachmentInfo(page, name, version);
        }
        Collection c = (Collection) m_cache.get(page.getName());
        if (c == null) {
            log.log(Level.INFO, "...wasn't in the cache");
            c = refresh(page);
            if (c == null) return null;
        } else {
            log.log(Level.INFO, "...FOUND in the cache");
            m_cacheHits++;
        }
        return findAttachmentFromCollection(c, name);
    }

    /**
     * {@inheritDoc}
     */
    public List getVersionHistory(Attachment att) {
        return m_provider.getVersionHistory(att);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteVersion(Attachment att) throws ProviderException {
        m_cache.remove(att.getParentName());
        cache.put(WIKI_PAGE_ATTACHMENTS_MAP, m_cache);
        m_provider.deleteVersion(att);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAttachment(Attachment att) throws ProviderException {
        m_cache.remove(att.getParentName());
        cache.put(WIKI_PAGE_ATTACHMENTS_MAP, m_cache);
        m_attCache.remove(att.getName());
        cache.put(ATTACHMENT_NAME_ATTACHMENT_MAP, m_attCache);
        m_provider.deleteAttachment(att);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String getProviderInfo() {
        return "Real provider: " + m_provider.getClass().getName() + ".  Cache misses: " + m_cacheMisses + ".  Cache hits: " + m_cacheHits;
    }

    /**
     *  Returns the WikiAttachmentProvider that this caching provider delegates to.
     * 
     *  @return The real provider underneath this one.
     */
    public WikiAttachmentProvider getRealProvider() {
        return m_provider;
    }

    /**
     * {@inheritDoc}
     */
    public void moveAttachmentsForPage(String oldParent, String newParent) throws ProviderException {
        m_provider.moveAttachmentsForPage(oldParent, newParent);
        m_cache.remove(newParent);
        m_cache.remove(oldParent);
        String checkName = oldParent + "/";
        Collection<String> names = m_attCache.keySet();
        for (String name : names) {
            if (name.startsWith(checkName)) {
                m_attCache.remove(name);
            }
        }
        cache.put(WIKI_PAGE_ATTACHMENTS_MAP, m_cache);
        cache.put(ATTACHMENT_NAME_ATTACHMENT_MAP, m_attCache);
    }
}
