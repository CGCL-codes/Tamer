package combinereport.report;

import java.util.Hashtable;
import java.util.Vector;
import dbmanager.DBManager;
import dbmanager.GetIdName;

public class Storage implements ReportInterface {

    String ReportName;

    String process;

    String value;

    DBManager database;

    String buffer[];

    String Object;

    GetIdName gid;

    String PATH = "path";

    String key;

    String typeofreport;

    Hashtable<Object, Object> Reportmap = new Hashtable<Object, Object>();

    public void setProcess(String process) {
        this.process = process;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getBuffer() {
        return buffer;
    }

    public void getResult() {
        Vector<Object> Field = new Vector<Object>();
        String heading[] = null;
        if (buffer.length > 0) {
            heading = buffer[0].split("\t");
            for (int i = 0; i < heading.length; i++) Field.add(heading[i]);
        }
        try {
            if (process.equals("mod")) database.createTable(ReportName, Field);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        for (int i = buffer.length - 1; i > 0; i--) {
            Hashtable<Object, Object> table = new Hashtable<Object, Object>();
            try {
                String splitbuffer[] = buffer[i].split("\t");
                table = new Hashtable<Object, Object>();
                for (int k = 0; k < splitbuffer.length; k++) {
                    try {
                        table.put(heading[k].toLowerCase(), splitbuffer[k]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                database.getInsert(ReportName, table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setObject(String Object) {
        this.Object = Object;
    }

    public void setDbmanager(DBManager database) {
        this.database = database;
        gid = new GetIdName(database);
        PATH = gid.getId(PATH);
    }

    public String getReportName() {
        return ReportName;
    }

    public void setReportName(String ReportName) {
        this.ReportName = ReportName;
    }

    public void setBuffer(String[] buffer) {
        this.buffer = buffer;
    }

    public void addReport() {
        getResult();
    }

    public void updateReport() {
        getResult();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void initializeData() {
    }

    public Hashtable<java.lang.Object, java.lang.Object> getReportMap() {
        return Reportmap;
    }

    public void setReportmap(Hashtable<java.lang.Object, java.lang.Object> table) {
        this.Reportmap = table;
    }

    public String gettypeofreport() {
        return typeofreport;
    }

    public void settypeofreport(String typeofreport) {
        if (!typeofreport.equals(null)) this.typeofreport = typeofreport; else typeofreport = "autoupdate";
    }

    public void setrepoton(String reporton) {
    }

    public String getProcess() {
        return process;
    }
}
