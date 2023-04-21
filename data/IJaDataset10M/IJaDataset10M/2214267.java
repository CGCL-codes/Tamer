package com.locatead.proto.dao;

import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import org.springframework.stereotype.Component;
import com.locatead.proto.model.Cell;
import com.locatead.proto.util.datastore.RdbmsPMF;

@Component
public class CellDAOImpl implements CellDAO {

    public String getConnectionInfo() {
        return RdbmsPMF.get().getConnectionDriverName();
    }

    public String dbTest() {
        PersistenceManager pm = null;
        try {
            pm = RdbmsPMF.get().getPersistenceManager();
            Cell test = new Cell("", 0, 0, new Date(), new Date(), new Date(), "Sean Reuben", "LocateAd Inc.");
            pm.makePersistent(test);
        } finally {
            pm.close();
        }
        return "OK";
    }

    public Cell getCell(Integer x, Integer y) {
        Cell ret = null;
        PersistenceManager pm = null;
        try {
            PersistenceManagerFactory pmf = RdbmsPMF.get();
            pm = pmf.getPersistenceManager();
            Query q = pm.newQuery(Cell.class, "x==ix && y==iy");
            q.declareParameters("int ix, int iy");
            @SuppressWarnings("unchecked") List<Cell> lst = (List<Cell>) q.execute(x, y);
            ret = lst.get(0);
        } finally {
            pm.close();
        }
        return ret;
    }

    public void initCellDB(String id, String passwd) {
    }
}
