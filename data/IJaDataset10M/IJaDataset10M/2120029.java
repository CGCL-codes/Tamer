package org.ozoneDB.core.DbRemote;

import org.ozoneDB.DxLib.*;
import org.ozoneDB.ObjectNotFoundException;
import org.ozoneDB.core.*;
import java.io.*;
import java.util.logging.Level;

/**
 * Determine the modification times of the specified database objects.
 *
 *
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.14 $Date: 2004/11/02 20:39:08 $
 */
public final class DbModTimes extends DbTransactionalCommand {

    private static final long serialVersionUID = 1L;

    private DxArrayBag objectIDs;

    public DbModTimes() {
        objectIDs = new DxArrayBag();
    }

    public void addObjectID(ObjectID id) {
        objectIDs.add(id);
    }

    public void perform(Transaction ta) throws Exception {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("DbModTimes.perform()");
        }
        ta.getServer().getTransactionManager().checkExclusion();
        DxMap map = new DxHashMap(objectIDs.count());
        for (int i = 0; i < objectIDs.count(); i++) {
            ObjectID id = (ObjectID) objectIDs.elementAtIndex(i);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("    id:" + id);
            }
            ObjectContainer container = ta.getServer().getStoreManager().containerForID(ta, id);
            if (container == null) {
                throw new ObjectNotFoundException("No such object.");
            }
            try {
                Long modTime = new Long(container.modTime());
                map.addForKey(modTime, id);
            } finally {
                container.unpin();
            }
        }
        setResult(map);
    }
}
