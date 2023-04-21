package jgnash.engine.db4o;

import com.db4o.ObjectContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import jgnash.engine.TrashObject;
import jgnash.engine.dao.TrashDAO;

/**
 * Db4o trash DAO
 *
 * @author Craig Cavanaugh
 * @version $Id: Db4oTrashDAO.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
class Db4oTrashDAO extends AbstractDb4oDAO implements TrashDAO {

    private final Logger logger = Logger.getLogger(Db4oTrashDAO.class.getName());

    Db4oTrashDAO(final ObjectContainer container, final boolean isRemote) {
        super(container, isRemote);
    }

    @Override
    public List<TrashObject> getTrashObjects() {
        return new ArrayList<TrashObject>(container.query(TrashObject.class));
    }

    @Override
    public void add(TrashObject trashObject) {
        container.set(trashObject);
        commit();
    }

    @Override
    public void remove(TrashObject trashObject) {
        container.delete(trashObject.getObject());
        container.ext().purge(trashObject.getObject());
        container.delete(trashObject);
        container.ext().purge(trashObject);
        commit();
        logger.info("Removed TrashObject");
    }
}
