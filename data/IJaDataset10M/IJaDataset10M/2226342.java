package com.neurogrid.middle;

import java.util.*;
import org.apache.log4j.Category;
import com.neurogrid.database.Store;
import com.neurogrid.database.StoreFactory;
import com.neurogrid.database.SQLDataBase;
import com.neurogrid.database.SQLTableData;
import com.neurogrid.database.DBAccess;
import com.neurogrid.database.SearchResult;
import com.neurogrid.database.SearchCriteria;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Keyword Persistence Engine <br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   4/March/2001   sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */
public class KeywordPersistenceEngine extends Keyword implements PersistenceEngine {

    private static final String cvsInfo = "$Id: KeywordPersistenceEngine.java,v 1.3 2002/03/26 11:23:57 samjoseph Exp $";

    public static String getCvsInfo() {
        return cvsInfo;
    }

    private static Category o_cat = Category.getInstance(KeywordPersistenceEngine.class.getName());

    private static Hashtable x_persistor = new Hashtable(100);

    private static Hashtable x_id_persistor = new Hashtable(100);

    private static long x_object_id = 0;

    private static long nextObjectID() {
        return ++x_object_id;
    }

    private static boolean o_created = false;

    private static KeywordPersistenceEngine o_persistence_engine = null;

    public static PersistenceEngine getPersistenceEngine() {
        if (o_created == false) {
            o_persistence_engine = new KeywordPersistenceEngine();
            o_created = true;
        }
        return o_persistence_engine;
    }

    /**
   * create an object
   *
   * @param p_object             an object array that contains the necessary components for this item
   * 
   * @return Object              the retrieved object
   */
    public Object createObject(Object[] p_object) throws Exception {
        return null;
    }

    public Object retrieveObject(Object[] p_object, boolean p_create) throws Exception {
        o_cat.debug("Retrieving keyword id");
        Long x_long = (Long) (x_persistor.get(p_object[0]));
        if (x_long != null) {
            return new Keyword((String) (p_object[0]), x_long.longValue());
        } else if (p_create == true) {
            x_persistor.put(p_object[0], new Long(nextObjectID()));
            x_id_persistor.put(new Long(nextObjectID()), p_object[0]);
            x_long = (Long) (x_persistor.get(p_object[0]));
            return new Keyword((String) (p_object[0]), x_long.longValue());
        } else {
            throw new Exception("No id for this keyword");
        }
    }

    public Object retrieveObjectFromID(long p_id) throws Exception {
        o_cat.debug("Retrieving keyword");
        String x_keyword = (String) (x_id_persistor.get(new Long(p_id)));
        if (x_keyword != null) {
            return new Keyword(x_keyword, p_id);
        } else {
            throw new Exception("No keyword of this id");
        }
    }

    public void update(Object p_object) throws Exception {
        Long x_long = (Long) (x_persistor.get(p_object));
        x_persistor.put(p_object, x_long);
        x_id_persistor.put(x_long, p_object);
    }

    public SearchResult search(Object p_query, SearchCriteria p_criteria) throws Exception {
        return null;
    }

    /**
   * count objects
   *
   * @param p_query         an array of objects that specify full or partial object matching criteria
   * @param p_criteria      search criteria
   * 
   * @return long           the number of items
   */
    public long count(Object p_query, SearchCriteria p_criteria) throws Exception {
        return 0;
    }
}
