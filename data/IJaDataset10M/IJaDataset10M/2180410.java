package org.polepos.teams.jdo;

import org.polepos.circuits.monaco.MonacoDriver;
import org.polepos.teams.jdo.data.JdoLightObject;

/**
 * @author Christian Ernst
 */
public class MonacoJdo extends JdoDriver implements MonacoDriver {

    public void write() {
        int commitctr = 0;
        int commitInterval = 50000;
        int count = setup().getObjectCount();
        begin();
        for (int i = 1; i <= count; i++) {
            store(new JdoLightObject(i));
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
            store(new JdoLightObject(idbase + i));
            commit();
            begin();
        }
        commit();
    }
}
