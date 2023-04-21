package query.condition.typereport;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import logger.PoolLogger;
import utility.ConvertToLower;
import utility.Input;
import database.RecordSearch;
import dbmanager.Column;
import dbmanager.DBManager;
import dbmanager.GetIdName;

public class Autoupdate implements Typereport {

    String path = "path";

    DBManager datbase;

    GetIdName gid;

    Vector<Object> Condition = new Vector<Object>();

    String value;

    String key;

    Hashtable<Object, Object> Reportmap = new Hashtable<Object, Object>();

    Hashtable<Object, Object> ConditionMap = new Hashtable<Object, Object>();

    PoolLogger pl;

    public Autoupdate() {
        pl = (PoolLogger) PoolLogger.getLogger(this.getClass().getName());
    }

    public Vector<Object> getConditionMap() {
        return Condition;
    }

    public String getPath(String ReportName) {
        Reportmap = ConvertToLower.convertHashKey(Reportmap);
        Hashtable<Object, Object> temptable = new Hashtable<Object, Object>();
        temptable.put("pid", path);
        temptable.put("td", "null");
        RecordSearch rs = new RecordSearch();
        rs.setConditionMap(temptable);
        String line[] = rs.getArrayFromResultSet();
        for (int i = 0; i < line.length; i++) {
            String splitline[] = line[i].split("\t");
            path = splitline[Column.pv_index - 1];
            if (path.equals("0")) path = splitline[Column.vt_index - 1]; else path = gid.getItem(path);
        }
        path = Input.ROOT + path;
        File file = new File(path);
        file.mkdirs();
        path = path + (ReportName.toLowerCase()) + Input.FILRFORMAT;
        return path;
    }

    public void getResult() {
        if (!key.equals("no$")) {
            ConditionMap = new Hashtable<Object, Object>();
            ConditionMap.put("Conditionfield", key);
            ConditionMap.put("Conditionvalue", value);
            ConditionMap.put("CONDITIONOPERATOR", "=");
            ConditionMap.put("Logicaloperator", "AND");
            ConditionMap = ConvertToLower.convertHashKey(ConditionMap);
            Condition.add(ConditionMap);
        }
    }

    public void setData(String key, String Value, Hashtable<Object, Object> Reportmap) {
        gid = new GetIdName();
        this.Reportmap.putAll(Reportmap);
        path = gid.getId(path);
        this.key = key;
        this.value = Value;
    }

    public Vector<Object> getFieldVector() {
        return Condition;
    }
}
