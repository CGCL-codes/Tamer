package org.vardb.blast;

import org.vardb.lists.dao.CPhiBlastQuery;

public class CPhiBlastResults extends CAbstractBlastResults {

    public CPhiBlastResults(CPhiBlastQuery query) {
        super(query);
    }

    public String getQuery() {
        return this.params.getQuery();
    }
}
