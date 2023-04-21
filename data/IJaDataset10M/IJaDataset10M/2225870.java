package org.enerj.server.logentry;

import java.io.DataInput;
import java.io.IOException;
import org.enerj.server.pageserver.RedoLogServer;

/** 
 * Log Entry for "End Database Checkpoint".
 *
 * @version $Id: EndDatabaseCheckpointLogEntry.java,v 1.4 2006/05/01 00:28:39 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad</a>
 * @see RedoLogServer
 */
public class EndDatabaseCheckpointLogEntry extends LogEntry {

    /**
     * Constructs an empty EndDatabaseCheckpointLogEntry.
     */
    public EndDatabaseCheckpointLogEntry() {
    }

    /**
     * Gets the entry type of this log entry.
     *
     * @return END_DB_CHECKPOINT_ENTRY_TYPE.
     */
    public byte getEntryType() {
        return END_DB_CHECKPOINT_ENTRY_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    protected int loadFromLog(DataInput aDataInput) throws IOException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        String name = this.getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1) + "[]";
    }
}
