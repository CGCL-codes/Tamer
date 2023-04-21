package com.entelience.provider.probe;

import com.entelience.sql.Db;
import com.entelience.util.Logs;
import com.entelience.probe.ForeignDbInfo;
import com.entelience.probe.ProbeSourceDb;

/**
 * A probes source db is an external database (ie ForeignDB) from
 * which a probe import data
 */
public class DbProbeSource {

    public static int getProbeSourceId(Db db, ForeignDbInfo fdi) throws Exception {
        ProbeSourceDb psdb = new ProbeSourceDb();
        psdb.setDb(db);
        return psdb.addOrGetSourceId(fdi);
    }
}
