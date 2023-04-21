package de.dirkdittmar.flickr.group.comment.spring;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import de.dirkdittmar.flickr.group.comment.GlobalConstants;
import de.dirkdittmar.flickr.group.comment.cache.PhotoCache;
import de.dirkdittmar.flickr.group.comment.cache.PhotoCacheImpl;
import de.dirkdittmar.flickr.group.comment.domain.PhotoCacheObject;
import de.dirkdittmar.flickr.group.comment.domain.PhotoCacheStore;

public class PersistentCacheFactoryBean implements FactoryBean {

    private static final Logger log = Logger.getLogger(PersistentCacheFactoryBean.class);

    private static final String CACHE_FILE = "cache.xml";

    private PhotoCacheImpl photoCache;

    public PersistentCacheFactoryBean() {
        String userHomeStr = System.getProperty("user.home");
        String propertiesDirName = userHomeStr + File.separator + GlobalConstants.SETTINGS_DIR;
        String cacheFileName = propertiesDirName + File.separator + CACHE_FILE;
        File cacheFile = new File(cacheFileName);
        try {
            if (cacheFile.exists()) {
                JAXBContext jaxbContext = JAXBContext.newInstance(PhotoCacheStore.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                PhotoCacheStore cacheStore = (PhotoCacheStore) unmarshaller.unmarshal(cacheFile);
                cleanStore(cacheStore);
                Map<URI, PhotoCacheObject> map = buildMap(cacheStore);
                photoCache = new PhotoCacheImpl(map);
            } else {
                photoCache = new PhotoCacheImpl(new HashMap<URI, PhotoCacheObject>());
            }
        } catch (JAXBException e) {
            log.warn("could not load the photo cache ", e);
            photoCache = new PhotoCacheImpl(new HashMap<URI, PhotoCacheObject>());
        }
    }

    private Map<URI, PhotoCacheObject> buildMap(PhotoCacheStore cacheStore) {
        Map<URI, PhotoCacheObject> result = new HashMap<URI, PhotoCacheObject>();
        List<PhotoCacheObject> cacheObjects = cacheStore.getCacheObjects();
        for (PhotoCacheObject photoCacheObject : cacheObjects) {
            result.put(photoCacheObject.getUri(), photoCacheObject);
        }
        return result;
    }

    private void cleanStore(PhotoCacheStore cacheStore) {
        List<PhotoCacheObject> cacheObjects = cacheStore.getCacheObjects();
        for (Iterator<PhotoCacheObject> iterator = cacheObjects.iterator(); iterator.hasNext(); ) {
            PhotoCacheObject cacheObject = iterator.next();
            if (cacheObject.getCount() <= 0 || !cacheObject.isSurvivor()) {
                iterator.remove();
            } else {
                cacheObject.setCount(cacheObject.getCount() - 1);
                cacheObject.setSurvivor(false);
            }
        }
    }

    @Override
    public Object getObject() throws Exception {
        return photoCache;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getObjectType() {
        return PhotoCache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void close() {
        PhotoCacheStore store = new PhotoCacheStore();
        List<PhotoCacheObject> cacheObjects = store.getCacheObjects();
        Map<URI, PhotoCacheObject> cacheData = photoCache.getCacheData();
        for (Entry<URI, PhotoCacheObject> entry : cacheData.entrySet()) {
            cacheObjects.add(entry.getValue());
        }
        String userHomeStr = System.getProperty("user.home");
        String dirName = userHomeStr + File.separator + GlobalConstants.SETTINGS_DIR;
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String cacheFileName = dirName + File.separator + CACHE_FILE;
        File cacheFile = new File(cacheFileName);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PhotoCacheStore.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(store, cacheFile);
        } catch (JAXBException e) {
            log.warn("could not store the photo cache ", e);
        }
    }
}
