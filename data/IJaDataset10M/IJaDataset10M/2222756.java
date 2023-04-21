package jogamp.graph.font.typecast.ot.table;

import java.io.DataInput;
import java.io.IOException;

/**
 * TODO: To be implemented
 * @author <a href="mailto:davidsch@dev.java.net">David Schweinsberg</a>
 * @version $Id: GposTable.java,v 1.2 2007-01-24 09:47:47 davidsch Exp $
 */
public class GposTable implements Table {

    private DirectoryEntry _de;

    protected GposTable(DirectoryEntry de, DataInput di) throws IOException {
        _de = (DirectoryEntry) de.clone();
        int version = di.readInt();
        int scriptList = di.readInt();
        int featureList = di.readInt();
        int lookupList = di.readInt();
    }

    /** Get the table type, as a table directory value.
     * @return The table type
     */
    public int getType() {
        return GPOS;
    }

    public String toString() {
        return "GPOS";
    }

    /**
     * Get a directory entry for this table.  This uniquely identifies the
     * table in collections where there may be more than one instance of a
     * particular table.
     * @return A directory entry
     */
    public DirectoryEntry getDirectoryEntry() {
        return _de;
    }
}
