package tinlizard.dao;

import tinlizard.model.View;

public interface ViewDao extends PersistentObjectDao<View> {

    /** The Plexus role identifier. */
    String ROLE = ViewDao.class.getName();

    View findByName(String name);
}
