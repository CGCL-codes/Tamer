package simpleorm.drivers;

import simpleorm.dataset.SFieldScalar;
import simpleorm.dataset.SRecordMeta;
import simpleorm.sessionjdbc.SGenerator;

/**
 * Open source verion of Interbase.
 * 
 * @author John Price, Augustin
 */
public class SDriverFirebird extends SDriverInterbase {

    protected String driverName() {
        return "Jaybird JCA/JDBC driver";
    }

    public int maxIdentNameLength() {
        return 31;
    }

    public boolean supportsKeySequences() {
        return true;
    }

    /** Specializes SDriver.generateKeySequence using Firebird SEQUENCEs. */
    protected long generateKeySequence(SRecordMeta<?> rec, SFieldScalar keyFld) {
        Object sequenceName = ((SGenerator) keyFld.getGenerator()).getName();
        String qry = "SELECT GEN_ID(" + (String) sequenceName + ", 1) FROM RDB$DATABASE";
        Object next = getSession().rawQuerySingle(qry, false);
        return Long.parseLong((String) next);
    }

    protected String createSequenceDDL(String name) {
        return "CREATE GENERATOR " + name;
    }

    protected String dropSequenceDDL(String name) {
        return "DROP GENERATOR " + name;
    }
}
