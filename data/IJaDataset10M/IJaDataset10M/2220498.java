package net.infordata.ifw2m.hbr;

import org.hibernate.dialect.DB2Dialect;

/**
 * An SQL dialect for DB2/400 and DB2/i5.
 *
 * <p>This class provides support for DB2 Universal Database for iSeries.</p>
 *
 * @author Peter DeGregorio (pdegregorio)
 * @author Carey Evans
 */
public class DB2400Dialect extends DB2Dialect {

    public String getSequenceNextValString(String sequenceName) {
        return "select nextval for " + sequenceName + " from sysibm.sysdummy1";
    }

    public String getQuerySequencesString() {
        return "select seqname from qsys2.syssequences";
    }

    public String getIdentitySelectString() {
        return "select identity_val_local() from sysibm.sysdummy1";
    }

    public boolean supportsLimit() {
        return true;
    }

    public boolean supportsLimitOffset() {
        return false;
    }

    public String getLimitString(String sql, int offset, int limit) {
        return new StringBuilder(sql.length() + 40).append(sql).append(" fetch first ").append(limit).append(" rows only ").toString();
    }

    public boolean useMaxForLimit() {
        return true;
    }

    public boolean supportsVariableLimit() {
        return false;
    }

    public String getCurrentTimestampSelectString() {
        return "select current timestamp from sysibm.sysdummy1";
    }

    public String getCurrentTimestampSQLFunctionName() {
        return "current_timestamp";
    }
}
