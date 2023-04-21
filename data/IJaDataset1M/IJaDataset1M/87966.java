package net.sf.xclh.xcl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.xclh.BaseClass;
import net.sf.xclh.XclhException;
import net.sf.xclh.XclhObject;
import net.sf.xclh.XclhValidationException;
import net.sf.xclh.XclhConstants;
import net.sf.xclh.XclhContext;
import net.sf.xclh.ElementValidator;
import net.sf.xclh.XmlUtil;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class FileBasedADPCache extends XclhObject implements Cache {

    static final Logger LOG = Logger.getLogger(FileBasedADPCache.class.getPackage().getName());

    File cacheFile;

    File tempFile;

    public static final String TEMP_SUFFIX = ".temp";

    class XOCacheEntry implements Comparable<XOCacheEntry> {

        long modificationTime;

        String dpId;

        @SuppressWarnings("unchecked")
        List content;

        BaseClass builtClass = null;

        @SuppressWarnings("unchecked")
        public XOCacheEntry(String dpId, List content) {
            super();
            this.modificationTime = System.currentTimeMillis();
            this.dpId = dpId;
            this.content = content;
        }

        public int compareTo(XOCacheEntry o) {
            return dpId.compareTo(o.dpId);
        }

        public String getDpId() {
            return dpId;
        }

        public long getModificationTime() {
            return modificationTime;
        }

        public void updateModificationTime() {
            this.modificationTime = System.currentTimeMillis();
        }

        public BaseClass getBuiltClass() {
            if (builtClass == null) {
                LOG.finer("Building class from cache: " + dpId);
                try {
                    builtClass = new BaseClass(content, getLocationInformation());
                } catch (XclhValidationException e) {
                    throw new CacheException("Failed to build class " + dpId + " from cache", e);
                }
                LOG.finer("Built class " + dpId + " from cache");
            }
            return builtClass;
        }

        @SuppressWarnings("unchecked")
        public List getContent() {
            return content;
        }
    }

    Map<String, XOCacheEntry> cacheEntries = new HashMap<String, XOCacheEntry>();

    public FileBasedADPCache(File cacheFile) throws XclhValidationException {
        super(null, cacheFile.getAbsolutePath());
        this.cacheFile = cacheFile.getAbsoluteFile();
        tempFile = new File(cacheFile.getParentFile(), cacheFile.getName() + TEMP_SUFFIX);
        reload();
    }

    public BaseClass getBase(String dpId) {
        XOCacheEntry entry = cacheEntries.get(dpId);
        if (entry == null) return null;
        return entry.getBuiltClass();
    }

    public long getModificationTime(String dpId) {
        XOCacheEntry entry = cacheEntries.get(dpId);
        if (entry == null) return -1;
        return entry.getModificationTime();
    }

    public void reload() {
        LOG.entering(FileBasedADPCache.class.getName(), "reload");
        LOG.finer("Clearing XCl cache");
        cacheEntries.clear();
        if (!cacheFile.exists()) {
            LOG.finer("XCl cache file does not exist. Cache empty");
            LOG.exiting(FileBasedADPCache.class.getName(), "reload");
            return;
        }
        LOG.finest("XCl cache file exists, parsing");
        org.jdom.input.SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(cacheFile);
        } catch (JDOMException e) {
            throw new CacheException("JDOM exception while reading cache", e);
        } catch (IOException e) {
            throw new CacheException("IO exception while reading cache", e);
        }
        LOG.finest("XCl cache file parsed, verifying and building cache");
        Element rootEl = doc.getRootElement();
        XclhContext ctxt = null;
        LOG.finest("Building XCl context for cache content");
        try {
            ctxt = XmlUtil.makeInitialContext(rootEl);
        } catch (XclhException e) {
            throw new CacheException("XCl cache content problem - cache build failed", e);
        }
        if (ctxt == null || (!XclhConstants.NAMESPACE_JDOM.equals(rootEl.getNamespace())) || (!XclhConstants.ELEM_DATA.equals(rootEl.getName()))) {
            throw new CacheException("XCl cache possibly corrupt - non-XCl root element");
        }
        LOG.finest("Validating root element");
        try {
            ElementValidator.validateElement(rootEl, ctxt);
        } catch (XclhValidationException e) {
            throw new CacheException("Failed to validate root element", e);
        }
        LOG.finest("Reading, validating and building cache elements");
        long defaultLastModified = cacheFile.lastModified();
        for (Object dpElObj : rootEl.getChildren(XclhConstants.ELEM_DATA_PART, XclhConstants.NAMESPACE_JDOM)) {
            Element dpEl = (Element) dpElObj;
            LOG.finest("Analyzing data part - validating");
            try {
                ElementValidator.validateElement(dpEl, ctxt);
            } catch (XclhValidationException e) {
                throw new CacheException("Failed to validate cache object", e);
            }
            LOG.finest("Building cache entry");
            XOCacheEntry entry = new XOCacheEntry(XmlUtil.getXoAttributeValue(dpEl, XclhConstants.ATTR_ID), dpEl.getContent());
            if (cacheEntries.containsKey(entry.dpId)) {
                throw new CacheException("Duplicate cache dp-id: " + entry.dpId + ", cache loading failed");
            }
            String mtValue = XmlUtil.getXoAttributeValue(dpEl, XclhConstants.ATTR_LAST_MODIFIED);
            if (mtValue != null) {
                try {
                    entry.modificationTime = Long.parseLong(mtValue);
                } catch (NumberFormatException e) {
                    throw new CacheException("Bad modification time in cache object " + entry.dpId, e);
                }
            } else {
                entry.modificationTime = defaultLastModified;
            }
            cacheEntries.put(entry.dpId, entry);
            LOG.finer("Read cache entry for class " + entry.dpId + ", last modified: " + entry.modificationTime);
        }
        LOG.finer("Cache built - contains " + cacheEntries.size() + " entries");
    }

    @SuppressWarnings("unchecked")
    public void saveBase(BaseClass obj) {
        LOG.entering(FileBasedADPCache.class.getName(), "saveBase", obj);
        List adp = null;
        LOG.finest("Getting ADP for class " + obj.getId());
        try {
            adp = obj.getADP();
        } catch (XclhException e) {
            throw new CacheException("Failed to get ADP for object to save", e);
        }
        LOG.finest("Creating entry and saving to cache");
        XOCacheEntry entry = new XOCacheEntry(obj.getId(), adp);
        cacheEntries.put(obj.getId(), entry);
        LOG.exiting(FileBasedADPCache.class.getName(), "saveBase");
    }

    public void store() {
        LOG.entering(FileBasedADPCache.class.getName(), "store");
        LOG.finest("Sorting keys");
        List<String> keys = new Vector<String>();
        keys.addAll(cacheEntries.keySet());
        Collections.sort(keys);
        LOG.finest("Creating output cache document");
        Element rootEl = new Element(XclhConstants.ELEM_DATA, XclhConstants.NAMESPACE_JDOM);
        Document doc = new Document(rootEl);
        LOG.finer("Building cache in memory");
        for (String key : keys) {
            LOG.finest("Building cache entry for: " + key);
            XOCacheEntry entry = cacheEntries.get(key);
            Element entryEl = new Element(XclhConstants.ELEM_DATA_PART, XclhConstants.NAMESPACE_JDOM);
            XmlUtil.setXoAttribute(entryEl, XclhConstants.ATTR_ID, entry.getDpId());
            XmlUtil.setXoAttribute(entryEl, XclhConstants.ATTR_LAST_MODIFIED, Long.toString(entry.getModificationTime()));
            for (Object contentObj : entry.getContent()) {
                Content content = (Content) contentObj;
                content = (Content) content.clone();
                entryEl.addContent(content);
            }
            rootEl.addContent(entryEl);
            LOG.finest("Built cache entry for: " + key);
        }
        LOG.finer("Saving cache to file");
        try {
            if (!tempFile.createNewFile()) {
                throw new CacheException("Cache temporary file exists!");
            }
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            OutputStream ostream = null;
            try {
                ostream = new FileOutputStream(tempFile);
                outputter.output(doc, ostream);
                ostream.close();
                ostream = null;
                if (!tempFile.renameTo(cacheFile)) {
                    throw new IOException("Failed to rename " + tempFile.getAbsolutePath() + " to " + cacheFile.getAbsolutePath());
                }
            } finally {
                try {
                    if (ostream != null) {
                        ostream.close();
                    }
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to close output stream", e);
                }
            }
        } catch (IOException e) {
            throw new CacheException("Problems with cache file IO, cache writing failed", e);
        } finally {
            if (tempFile.exists()) {
                if (!tempFile.delete()) {
                    LOG.warning("Failed to delete file " + tempFile.getAbsolutePath());
                }
            }
        }
    }
}
