package combinereport.query;

import java.util.Hashtable;
import java.util.Vector;
import property.Order;
import utility.CapitalChar;
import utility.ConvertToLower;
import utility.Input;
import utility.Splitstring;
import database.RecordSearch;
import dbmanager.Column;
import dbmanager.DBManager;
import dbmanager.GetIdName;
import file.SearchRecord;

public class Fieldlist implements Queryinterface {

    DBManager database;

    String tablename;

    String object;

    String[] buffer;

    String[] queryBuffer;

    String key = "no$";

    String value = "no$";

    String packagename = "combinereport.fieldlist.";

    String FIELD = "field";

    String Reportname;

    String Query;

    String tid;

    Hashtable<Object, Object> QueryMap = new Hashtable<Object, Object>();

    Hashtable<Object, Object> ReportMap = new Hashtable<Object, Object>();

    String reporton;

    GetIdName gid;

    Vector<Object> Fieldlist = new Vector<Object>();

    public void getResult() {
        try {
            combinereport.fieldlist.Fieldlist fieldlist;
            Class C = Class.forName(packagename + CapitalChar.makeFirstCharCapital(tablename.trim()));
            fieldlist = (combinereport.fieldlist.Fieldlist) C.newInstance();
            fieldlist.setDbmanager(database);
            fieldlist.setFieldVector(Fieldlist);
            fieldlist.setQuery(Query);
            fieldlist.setbuffer(buffer);
            fieldlist.getResult();
            buffer = fieldlist.getbuffer();
            Query = fieldlist.getQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void initializeData() {
        Hashtable<Object, Object> table = new Hashtable<Object, Object>();
        table.put("mid", object);
        table.put("pid", FIELD);
        table.put("td", "null");
        RecordSearch rs = new RecordSearch(database);
        rs.setConditionMap(table);
        String line[] = rs.getArrayFromResultSet();
        for (int i = 0; i < line.length; i++) {
            String splitline[] = line[i].split("\t");
            table.clear();
            table.put("mid", splitline[Column.pv_index - 1]);
            table.put("td", "null");
            rs.setConditionMap(table);
            String result[] = rs.getArrayFromResultSet();
            Hashtable<Object, Object> fieldproperty = new Hashtable<Object, Object>();
            for (int j = 0; j < result.length; j++) {
                String splitresult[] = result[j].split("\t");
                String value = splitresult[Column.pv_index - 1];
                if (value.equals("0")) value = splitresult[Column.vt_index - 1];
                fieldproperty.put(gid.getItem(splitresult[Column.pid_index - 1]), value);
            }
            fieldproperty = ConvertToLower.convertHashKey(fieldproperty);
            Fieldlist.add(fieldproperty);
        }
        try {
            combinereport.condition.typereport.Typereport type;
            Class C = Class.forName("combinereport.condition.typereport." + CapitalChar.makeFirstCharCapital(this.typeofreport.trim()));
            type = (combinereport.condition.typereport.Typereport) C.newInstance();
            type.setData("", value, database, ReportMap);
            Fieldlist.addAll(type.getFieldVector());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDbmanager(DBManager database) {
        this.database = database;
        gid = new GetIdName(database);
        FIELD = gid.getId(FIELD);
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setQuerybuffer(String[] Querybuffer) {
        queryBuffer = Querybuffer;
    }

    public void setbuffer(String[] Querybuffer) {
        this.buffer = Querybuffer;
    }

    public void settablename(String tablename) {
        this.tablename = tablename;
    }

    public String gettablename() {
        return tablename;
    }

    public String[] getbuffer() {
        return buffer;
    }

    public String[] getQuerybuffer() {
        return queryBuffer;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getkey() {
        return key;
    }

    public String getvalue() {
        return value;
    }

    public String getQueryName() {
        return null;
    }

    public void setQueryName(String QueryName) {
    }

    public Hashtable<Object, Object> getQueryMap() {
        return QueryMap;
    }

    public void setQueryMap(Hashtable<Object, Object> QueryMap) {
        this.QueryMap = QueryMap;
    }

    public String gettid() {
        return tid;
    }

    public void settid(String tid) {
        this.tid = tid;
    }

    public void addReport() {
        try {
            combinereport.fieldlist.Fieldlist fieldlist;
            Class C = Class.forName(packagename + CapitalChar.makeFirstCharCapital(tablename.trim()));
            fieldlist = (combinereport.fieldlist.Fieldlist) C.newInstance();
            fieldlist.setDbmanager(database);
            fieldlist.setFieldVector(Fieldlist);
            fieldlist.setbuffer(buffer);
            fieldlist.addReport();
            buffer = fieldlist.getbuffer();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateReport() {
        try {
            combinereport.fieldlist.Fieldlist fieldlist;
            Class C = Class.forName(packagename + CapitalChar.makeFirstCharCapital(tablename.trim()));
            fieldlist = (combinereport.fieldlist.Fieldlist) C.newInstance();
            fieldlist.setDbmanager(database);
            fieldlist.setFieldVector(Fieldlist);
            fieldlist.setQuerybuffer(queryBuffer);
            fieldlist.setQuery(Query);
            fieldlist.setbuffer(buffer);
            fieldlist.updateReport();
            buffer = fieldlist.getbuffer();
            Query = fieldlist.getQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getReportname() {
        return Reportname;
    }

    public void setReportName(String Reportname) {
        this.Reportname = Reportname;
    }

    public void setreporton(String reporton) {
        this.reporton = reporton;
    }

    public void setReportmap(Hashtable<Object, Object> ReportMap) {
        this.ReportMap.putAll(ReportMap);
    }

    String typeofreport;

    public void settypeofreport(String typeofreport) {
        this.typeofreport = typeofreport;
    }

    public void setViewObject(String Object, char ch) {
        Splitstring sp = new Splitstring();
        String splitobject[] = sp.split(Object, Character.toString(ch));
        for (int i = 0; i < splitobject.length; i++) {
            Hashtable<Object, Object> table = new Hashtable<Object, Object>();
            table.put("field", splitobject[i]);
            Fieldlist.add(table);
        }
    }

    public void setViewObject(Vector<Object> PropertyMap) {
        this.Fieldlist.addAll(PropertyMap);
    }

    public void setQuery(String Query) {
        this.Query = Query;
    }

    public String getQuery() {
        return Query;
    }

    public int getcount() {
        return 0;
    }

    public void setCount(int count) {
    }

    public void setMaxcount(int count) {
    }

    public void buildQuery() {
        try {
            combinereport.fieldlist.Fieldlist fieldlist;
            Class C = Class.forName(packagename + CapitalChar.makeFirstCharCapital(tablename.trim()));
            fieldlist = (combinereport.fieldlist.Fieldlist) C.newInstance();
            fieldlist.setDbmanager(database);
            fieldlist.setFieldVector(Fieldlist);
            fieldlist.setQuery(Query);
            fieldlist.getResult();
            Query = fieldlist.getQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setProcess(String process) {
    }
}
