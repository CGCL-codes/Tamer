package org.unitils.core.dbsupport;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class Oracle10DbSupport extends OracleDbSupport {

    @Override
    protected Integer getOracleMajorVersionNumber() {
        return 10;
    }
}
