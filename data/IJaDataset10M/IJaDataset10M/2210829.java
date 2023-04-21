package combinereport.view;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import sun.net.www.content.image.gif;
import dbmanager.DBManager;
import dbmanager.GetIdName;

public class Key implements Reportview {

    String key;

    DBManager database;

    GetIdName gid;

    String QUERY;

    String value;

    Hashtable<Object, Object> reportmap = new Hashtable<Object, Object>();

    public String getQuery() {
        return QUERY;
    }

    public int getQuerycount() {
        return 0;
    }

    public void getResult() {
        Vector<Object> condition = new Vector<Object>();
        Hashtable<Object, Object> conditionmap = new Hashtable<Object, Object>();
        if (reportmap.containsKey("condition")) {
            condition.addAll((Collection<? extends Object>) reportmap.get("condition"));
            conditionmap.put(combinereport.condition.Condition.LOGICALOPERATOR, "and");
        }
        conditionmap.put(combinereport.condition.Condition.CONDITIONFIELD, key);
        conditionmap.put(combinereport.condition.Condition.CONDITIONVALUE, reportmap.get("value"));
        conditionmap.put(combinereport.condition.Condition.CONDITIONOPERATOR, "=");
        condition.add(conditionmap);
        reportmap.put("condition", condition);
    }

    public void initializeData() {
    }

    public void setDbmanager(DBManager Sqldb) {
        this.database = Sqldb;
        gid = new GetIdName(Sqldb);
    }

    public void setMaxQuerycount(int count) {
    }

    public void setObject(String Object) {
        key = Object;
        key = gid.getItem(Object);
        if (key.equals("no$")) key = Object;
    }

    public void setQuery(String Query) {
        this.QUERY = Query;
    }

    public void setQueryCount(int count) {
    }

    public void setReportMap(Hashtable<Object, Object> reportMap) {
        this.reportmap.putAll(reportMap);
    }

    public void setReportName(String Reportname) {
    }

    public Hashtable<Object, Object> getReportMap() {
        return reportmap;
    }
}
