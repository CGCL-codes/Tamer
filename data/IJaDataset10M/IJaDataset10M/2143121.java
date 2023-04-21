package net.zcarioca.zscrum.domain.dao.impl;

import java.util.Collections;
import java.util.List;
import net.zcarioca.zscrum.domain.Release;
import net.zcarioca.zscrum.domain.dao.ReleaseDAO;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOReadException;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOWriteException;
import net.zcarioca.zscrum.domain.util.TaskGroupComparator;

/**
 * Implementation of the {@link ReleaseDAO}.
 * 
 * 
 * @author zcarioca
 */
public class ReleaseDAOImpl extends EntityDAOImpl implements ReleaseDAO {

    /**
    * {@inheritDoc}
    * 
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#save(java.lang.Object)
    */
    @Override
    public void save(Release entity) throws DAOWriteException {
        saveEntity(entity);
    }

    /**
    * {@inheritDoc}
    * 
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#delete(java.lang.Object)
    */
    @Override
    public void delete(Release entity) throws DAOWriteException {
        deleteEntity(entity);
    }

    /**
    * {@inheritDoc}
    * 
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#loadAll()
    */
    @Override
    public List<Release> loadAll() throws DAOReadException {
        return loadAllEntities(Release.class);
    }

    /**
    * {@inheritDoc}
    * 
    * @see net.zcarioca.zscrum.domain.dao.EntityDAO#loadById(int)
    */
    @Override
    public Release loadById(int entityId) throws DAOReadException {
        return loadEntityById(Release.class, entityId);
    }

    /**
    * {@inheritDoc}
    * 
    * @see net.zcarioca.zscrum.domain.dao.ReleaseDAO#loadAllByProjectId(int)
    */
    @Override
    public List<Release> loadAllByProjectId(int projectId) throws DAOReadException {
        List<Release> releases = loadEntitiesByProperty(Release.class, "parent_id", projectId);
        Collections.sort(releases, new TaskGroupComparator());
        return releases;
    }
}
