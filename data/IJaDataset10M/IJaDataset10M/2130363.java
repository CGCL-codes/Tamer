package gov.lanl.util;

import java.io.File;
import org.apache.log4j.Logger;

public class ConcurrentEvictionFileDelete<K, V> implements ConcurrentLinkedHashMap.EvictionListener<String, String> {

    static Logger logger = Logger.getLogger(ConcurrentEvictionFileDelete.class);

    public void onEviction(java.lang.String key, java.lang.String value) {
        File f = new File(value);
        f.delete();
        logger.debug("deleted " + value);
    }
}
