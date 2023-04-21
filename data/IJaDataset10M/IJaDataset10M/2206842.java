package org.geoforge.wrpsql;

import org.geoforge.sql.database.GfrDtbSpcAbs;
import org.geoforge.sql.table.GfrTblAbs;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public final class ActDtbGetTableRecordings extends ActDtbAbs {

    private static ActDtbGetTableRecordings _INSTANCE_ = null;

    private ActDtbGetTableRecordings() throws Exception {
        super();
    }

    public static ActDtbGetTableRecordings s_getInstance() throws Exception {
        if (ActDtbGetTableRecordings._INSTANCE_ == null) ActDtbGetTableRecordings._INSTANCE_ = new ActDtbGetTableRecordings();
        return ActDtbGetTableRecordings._INSTANCE_;
    }

    public String[][] doJob(String user, String password, String strUrl, GfrDtbSpcAbs dtb, GfrTblAbs tbl) throws Exception {
        return _language.getList(user, password, strUrl, dtb, tbl);
    }
}
