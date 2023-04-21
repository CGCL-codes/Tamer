package medi.db.deleter;

import javatools.db.DbException;
import medi.db.AbstractProvider;

/**
 *
 * @author  Antonio Petrelli
 */
public class VolumeDeleter extends medi.db.deleter.AbstractDeleter {

    /** Creates a new instance of DataDeleter */
    public VolumeDeleter(AbstractProvider pPrv) {
        super(pPrv);
    }

    public void deleteAll(Object[] ID) throws DbException {
        prv.removeVolumeClean((Integer) ID[0]);
    }

    public void deletePreserving(Object[] ID) throws DbException {
        prv.removeVolume((Integer) ID[0]);
    }
}
