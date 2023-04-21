package net.sourceforge.oracle.jutils;

import java.sql.ResultSet;

/**
 *
 * @author asales
 */
public class StoredCtx {

    ResultSet rset;

    public StoredCtx(ResultSet rs) {
        rset = rs;
    }

    public ResultSet getResultSet() {
        return rset;
    }
}
