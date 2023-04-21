package org.objectstyle.cayenne.unit;

import java.sql.Connection;
import java.sql.Types;
import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.DbAttribute;
import org.objectstyle.cayenne.map.DbEntity;

public class IngresStackAdapter extends AccessStackAdapter {

    public IngresStackAdapter(DbAdapter adapter) {
        super(adapter);
    }

    public boolean supportsBinaryPK() {
        return false;
    }

    /**
     * Ingres doesn't support LONGVARCHAR comparisions ('like', '=', etc.)
     */
    public void willCreateTables(Connection con, DataMap map) {
        DbEntity paintingInfo = map.getDbEntity("PAINTING_INFO");
        if (paintingInfo != null) {
            DbAttribute textReview = (DbAttribute) paintingInfo.getAttribute("TEXT_REVIEW");
            textReview.setType(Types.VARCHAR);
            textReview.setMaxLength(255);
        }
    }
}
