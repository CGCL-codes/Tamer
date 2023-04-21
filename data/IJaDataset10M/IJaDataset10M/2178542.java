package news_rack.database;

import java.util.Map;
import java.util.List;
import news_rack.GlobalConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class NR_Collection {

    protected static Log _log = LogFactory.getLog("news_rack.database.NR_Collection.class");

    public static NR_SourceCollection GetSourceCollection(String uid, String name) {
        return GlobalConstants.GetDBInterface().GetSourceCollection(uid, name);
    }

    public static NR_ConceptCollection GetConceptCollection(String uid, String name) {
        return GlobalConstants.GetDBInterface().GetConceptCollection(uid, name);
    }

    public static NR_CategoryCollection GetCategoryCollection(String uid, String name) {
        return GlobalConstants.GetDBInterface().GetCategoryCollection(uid, name);
    }

    public String _uid;

    public String _name;

    public List _collection;

    transient Map _map;

    transient boolean _allAddedToMap;

    public NR_Collection(String uid, String name, List coll) {
        _uid = uid;
        _name = name;
        _collection = coll;
        _allAddedToMap = false;
        GlobalConstants.GetDBInterface().AddProfileCollection(this);
    }
}
