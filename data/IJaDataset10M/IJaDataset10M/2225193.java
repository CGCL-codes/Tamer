package gov.sns.jca;

import gov.sns.ca.StatusAdaptor;
import gov.aps.jca.dbr.*;

/**
 * Wrap a jca.dbr.DBR_STS record for high level access
 *
 * @author  tap
 */
class DbrStatusAdaptor extends DbrValueAdaptor implements StatusAdaptor {

    /** Creates a new instance of StatusAdaptor */
    DbrStatusAdaptor(DBR dbr) {
        super(dbr);
        if (!(dbr instanceof STS)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get the channel status
     * @return channel status
     */
    public int status() {
        return ((STS) _dbr).getStatus().getValue();
    }

    /**
     * Get the channel severity
     * @return channel severity
     */
    public int severity() {
        return ((STS) _dbr).getSeverity().getValue();
    }
}
