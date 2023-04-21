package org.vardb.blast;

public class CGenomeBlastParams extends CBlastParams {

    public CGenomeBlastParams() {
        super();
    }

    public CGenomeBlastParams(String database, String query) {
        super(query);
        setDatabase(database);
    }
}
