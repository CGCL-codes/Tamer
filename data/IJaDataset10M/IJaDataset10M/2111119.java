package org.geoforge.wrpsql;

import org.geoforge.sql.database.GfrDtbSpcAbs;
import org.geoforge.sql.field.FldAbs;
import org.geoforge.sql.table.GfrTblAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public final class ActDtbGetRecordingKeyValueTargets extends ActDtbAbs {

    private static ActDtbGetRecordingKeyValueTargets _INSTANCE_ = null;

    private ActDtbGetRecordingKeyValueTargets() throws Exception {
        super();
    }

    public static ActDtbGetRecordingKeyValueTargets s_getInstance() throws Exception {
        if (ActDtbGetRecordingKeyValueTargets._INSTANCE_ == null) ActDtbGetRecordingKeyValueTargets._INSTANCE_ = new ActDtbGetRecordingKeyValueTargets();
        return ActDtbGetRecordingKeyValueTargets._INSTANCE_;
    }

    public String[] doJob(String user, String password, String strUrl, GfrDtbSpcAbs dtb, GfrTblAbs tbl, FldAbs[] fldsTarget, FldAbs fldKey, String strValue) throws Exception {
        return _language.getElement(user, password, strUrl, dtb, tbl, fldsTarget, fldKey, strValue);
    }
}
