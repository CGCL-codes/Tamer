package fr.sportes.secrets;

import java.util.ConcurrentModificationException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;

public class DBctx {

    private DatastoreService datastore;

    private int retries;

    public DatastoreService getDS() {
        return datastore;
    }

    public Transaction getTR() {
        return datastore.getCurrentTransaction();
    }

    public DBctx() {
        datastore = DatastoreServiceFactory.getDatastoreService();
        retries = 3;
    }

    public boolean retry() {
        if (retries > 0) {
            try {
                datastore.beginTransaction();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return true;
        } else return false;
    }

    public void abort() {
        Transaction transaction = datastore.getCurrentTransaction();
        if (transaction != null && transaction.isActive()) transaction.rollback();
        retries = 0;
    }

    public void commit() {
        try {
            Transaction transaction = datastore.getCurrentTransaction();
            if (transaction != null && transaction.isActive()) transaction.commit();
            retries = 0;
        } catch (ConcurrentModificationException e) {
            rethrow(e);
        }
    }

    public void rethrow(ConcurrentModificationException e) {
        Transaction transaction = datastore.getCurrentTransaction();
        if (transaction != null && transaction.isActive()) transaction.rollback();
        --retries;
        if (retries == 0) throw e;
    }
}
