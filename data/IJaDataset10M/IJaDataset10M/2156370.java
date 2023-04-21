package saadadb.api;

import saadadb.cache.CacheManager;
import saadadb.cache.CacheManagerRelationIndex;
import saadadb.cache.CacheMeta;
import saadadb.database.Database;
import saadadb.exceptions.SaadaException;
import saadadb.util.Messenger;

/**
 * @author michel
 * * @version $Id: SaadaDB.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class SaadaDB {

    private static boolean loaded = false;

    /**
	 * @throws SaadaException 
	 * 
	 */
    public static void init(String db_name) {
        if (SaadaDB.loaded == true) {
            return;
        }
        Database.init(db_name);
        SaadaDB.loaded = true;
    }

    /**
	 * @return
	 */
    public static String getName() {
        return Database.getName();
    }

    /**
	 * @return
	 */
    public static CacheMeta getCacheMeta() {
        return Database.getCachemeta();
    }

    /**
	 * @return
	 */
    public static CacheManagerRelationIndex getCacheIndex() {
        return Database.getCacheindex();
    }

    /**
	 * @return
	 */
    public static CacheManager getCache() {
        return Database.getCache();
    }

    /**
	 * @return
	 */
    public static String getRepository() {
        return Database.getRepository();
    }

    /**
	 * @return
	 */
    public static String getRoot_dir() {
        return Database.getRoot_dir();
    }

    /**
	 * @return
	 */
    public static String getBase_url() {
        return Database.getUrl_root();
    }

    /**
	 * @return
	 */
    public static String[] getCollectionNames() {
        return Database.getCachemeta().getCollection_names();
    }

    /**
	 * @param coll_name
	 * @return
	 */
    public static SaadaCollection getCollection(String coll_name) {
        try {
            return new SaadaCollection(coll_name);
        } catch (SaadaException e) {
            Messenger.printStackTrace(e);
            return null;
        }
    }

    /**
	 * @param class_name
	 * @return
	 */
    public static SaadaClass getClass(String class_name) {
        try {
            return new SaadaClass(class_name);
        } catch (SaadaException e) {
            Messenger.printStackTrace(e);
            return null;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
