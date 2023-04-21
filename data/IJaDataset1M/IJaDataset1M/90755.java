package basicprocess.inheritance;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;
import pool.tablename.Property_details;
import pool.tablename.Pool;
import utility.ConvertToLower;
import utility.Input;
import dbmanager.DBManager;
import dbmanager.GetIdName;
import file.SearchRecord;

public class Iu implements Inheritance {

    DBManager database;

    Hashtable<Object, Object> Mastermap = new Hashtable<Object, Object>();

    Vector<Object> propertyVector = new Vector<Object>();

    GetIdName gid;

    String INHERITANCE = "inheritance";

    String SCOPE = "scope";

    String IU = "iu";

    Pool pool;

    SearchRecord sr;

    Vector<Object> finalvector = new Vector<Object>();

    public void getInsert() {
        Property_details pdobject = new Property_details();
        LinkedList<Object> linkedlist = new LinkedList<Object>();
        for (int i = 0; i < pool.getpropertyVector().size(); i++) {
            Property_details pd = (Property_details) pool.getpropertyVector().get(i);
            if (!pd.PropertyMap.get("pid").equals(INHERITANCE)) {
                linkedlist.add(pd.PropertyMap.get("pid"));
                pdobject.PropertyMap.putAll(pd.PropertyMap);
                finalvector.add(pdobject);
            }
        }
        for (int i = 0; i < pool.getpropertyVector().size(); i++) {
            Property_details pd = (Property_details) pool.getpropertyVector().get(i);
            try {
                if (pd.PropertyMap.get("pid").equals(INHERITANCE)) {
                    String parentobject = pd.PropertyMap.get("pv").toString();
                    Vector<Object> vector = new Vector<Object>();
                    Hashtable<Object, Object> temptable = new Hashtable<Object, Object>();
                    String path = Input.MASTERREPORTSPATH + parentobject + "//" + parentobject + "_mid" + Input.FILRFORMAT;
                    sr = new SearchRecord(path);
                    sr.fileOpen();
                    temptable.put("mid", parentobject);
                    vector = sr.getVectorSet(temptable);
                    sr.fileClose();
                    Scope scope = new Scope();
                    scope.setDbmanager(database);
                    vector = scope.checkforscope(vector, parentobject);
                    System.out.println(pd.PropertyMap);
                    finalvector.add(pd);
                    for (int j = 0; j < vector.size(); j++) {
                        temptable.clear();
                        temptable = (Hashtable<Object, Object>) vector.get(j);
                        temptable = ConvertToLower.convertHashKey(temptable);
                        String propertyid = temptable.get("pid").toString();
                        if (!linkedlist.contains(propertyid)) {
                            pdobject = new Property_details();
                            if (temptable.containsKey("mid")) temptable.remove("mid");
                            pdobject.PropertyMap.putAll(temptable);
                            finalvector.add(pdobject);
                            System.out.println(pdobject.PropertyMap);
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.setPropertyVector(finalvector);
    }

    public void getUpdate() {
        for (int i = 0; i < pool.getpropertyVector().size(); i++) {
            Property_details pd = (Property_details) pool.getpropertyVector().get(i);
            pd.PropertyMap = gid.convertMasterName(pd.PropertyMap, "property_details");
        }
    }

    public Vector<Object> getpropertydetailsVector() {
        return null;
    }

    public void setDbmanager(DBManager database) {
        this.database = database;
        gid = new GetIdName(database);
        INHERITANCE = gid.getId(INHERITANCE);
        SCOPE = gid.getId(SCOPE);
        IU = gid.getId(IU);
    }

    public void setobject(Object object) {
        pool = (Pool) object;
    }
}
