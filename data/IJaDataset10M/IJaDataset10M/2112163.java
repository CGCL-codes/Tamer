package DE.FhG.IGD.semoa.audit;

import DE.FhG.IGD.util.*;
import DE.FhG.IGD.semoa.server.*;
import java.util.*;
import java.io.*;
import java.security.*;
import java.lang.reflect.*;
import java.sql.*;
import java.lang.*;

public class ExceptionsTable extends AbstractAuditTables {

    private Object lock_ = new Object();

    public ExceptionsTable(DBCon dbc) {
        super(dbc);
    }

    public void write(String name, String message, String stacktrace, Timestamp time, int agentkey) {
        synchronized (lock_) {
            try {
                String query = "insert into exceptions values (nextval('exceptionsseq'),'" + name + "','" + message + "','" + stacktrace + "','" + time + "'," + agentkey + ");";
                execute(query);
            } catch (Exception e) {
                System.out.println("ERROR: cannot write into table Exceptions " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
