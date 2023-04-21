package edu.mit.wi.omnigene.das;

/** superclass for DAS response objects.  Knows its DSN
 *
 * @author Alex Rolfe
 */
public abstract class DASResult {

    private DSN dsn;

    public DASResult() {
    }

    public DASResult(DSN dsn) {
        this.dsn = dsn;
    }

    public DSN getDSN() {
        return dsn;
    }

    public void setDSN(DSN d) {
        dsn = d;
    }
}
