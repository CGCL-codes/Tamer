package org.geoforge.wrpsql;

import org.geoforge.sql.database.GfrDtbSpcAbs;
import org.geoforge.sql.table.GfrTblAbs;
import org.geoforge.sql.field.FldAbs;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public final class ActDtbGetRecordingsKeyValueTarget extends ActDtbAbs {

    private static ActDtbGetRecordingsKeyValueTarget _INSTANCE_ = null;

    private ActDtbGetRecordingsKeyValueTarget() throws Exception {
        super();
    }

    public static ActDtbGetRecordingsKeyValueTarget s_getInstance() throws Exception {
        if (ActDtbGetRecordingsKeyValueTarget._INSTANCE_ == null) ActDtbGetRecordingsKeyValueTarget._INSTANCE_ = new ActDtbGetRecordingsKeyValueTarget();
        return ActDtbGetRecordingsKeyValueTarget._INSTANCE_;
    }

    public String[] doJob(String user, String password, String strUrl, GfrDtbSpcAbs dtb, GfrTblAbs tbl, FldAbs fldTarget, FldAbs fldKey, String strValue) throws Exception {
        return _language.getList(user, password, strUrl, dtb, tbl, fldTarget, fldKey, strValue);
    }
}
