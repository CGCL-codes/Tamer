package org.polepos.teams.jpa;

import org.polepos.circuits.monaco.MonacoDriver;
import org.polepos.teams.jpa.data.JpaLightObject;

/**
 * @author Christian Ernst
 */
public class MonacoJpa extends JpaDriver implements MonacoDriver {

    public void write() {
        int commitctr = 0;
        int commitInterval = 50000;
        int count = setup().getObjectCount();
        begin();
        for (int i = 1; i <= count; i++) {
            store(new JpaLightObject(i));
            if (commitInterval > 0 && ++commitctr >= commitInterval) {
                commitctr = 0;
                commit();
                begin();
            }
        }
        commit();
    }

    public void commits() {
        int idbase = setup().getObjectCount() + 1;
        int count = setup().getCommitCount();
        begin();
        for (int i = 1; i <= count; i++) {
            store(new JpaLightObject(idbase + i));
            commit();
            begin();
        }
        commit();
    }
}
