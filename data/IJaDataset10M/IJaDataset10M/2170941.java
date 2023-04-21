package org.polepos.teams.jpa;

import java.util.Iterator;
import javax.persistence.Query;
import org.polepos.circuits.bahrain.BahrainDriver;
import org.polepos.teams.jpa.data.JpaIndexedPilot;

/**
 * @author Christian Ernst
 */
public class BahrainJpa extends JpaDriver implements BahrainDriver {

    public void write() {
        begin();
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
        int commitctr = 0;
        for (int i = 1; i <= numobjects; i++) {
            JpaIndexedPilot p = new JpaIndexedPilot("Pilot_" + i, "Jonny_" + i, i, i);
            db().persist(p);
            if (commitinterval > 0 && ++commitctr >= commitinterval) {
                commitctr = 0;
                commit();
                begin();
                Log.logger.fine("commit while writing at " + i + 1);
            }
            addToCheckSum(i);
        }
        commit();
    }

    public void queryIndexedString() {
        begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mName = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, "Pilot_" + i);
        }
        commit();
    }

    public void queryString() {
        begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mFirstName = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, "Jonny_" + i);
        }
        commit();
    }

    public void queryIndexedInt() {
        begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mLicenseID = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, new Integer(i));
        }
        commit();
    }

    public void queryInt() {
        begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mPoints = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, new Integer(i));
        }
        commit();
    }

    public void update() {
        begin();
        int updateCount = setup().getUpdateCount();
        Iterator it = db().createQuery("SELECT this FROM JpaIndexedPilot this").getResultList().iterator();
        for (int i = 1; i <= updateCount; i++) {
            JpaIndexedPilot p = (JpaIndexedPilot) it.next();
            p.setName(p.getName().toUpperCase());
            addToCheckSum(1);
        }
        commit();
    }

    public void delete() {
        begin();
        Iterator it = db().createQuery("SELECT this FROM JpaIndexedPilot this").getResultList().iterator();
        while (it.hasNext()) {
            db().remove(it.next());
            addToCheckSum(1);
        }
        commit();
    }
}
