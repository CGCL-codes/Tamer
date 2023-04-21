package de.fhg.igd.semoa.audit;

import java.sql.Timestamp;

public class TimesTable extends AbstractAuditTables {

    private Object lock_ = new Object();

    public TimesTable(DBCon dbc) {
        super(dbc);
    }

    public void write(int agentkey, Timestamp arrival, long arrivalmilli, Timestamp start) {
        synchronized (lock_) {
            try {
                String query = "insert into times (Timeskey,Agentskey,arrival,arrivalmilli,start) values (nextval('timesseq')," + agentkey + ",'" + arrival + "','" + arrivalmilli + "','" + start + "');";
                execute(query);
            } catch (Exception e) {
                System.out.println("ERROR: cannot write into table TImes " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public void update(String columnName, Timestamp ts, int key) {
        synchronized (lock_) {
            try {
                String query = "update times set " + columnName + "='" + ts + "' where Timeskey=" + key + ";";
                execute(query);
            } catch (Exception e) {
                System.out.println("ERROR: cannot update table Times " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
