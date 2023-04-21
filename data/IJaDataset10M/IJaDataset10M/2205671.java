package ocumed.persistenz.dao;

import java.util.List;
import ocumed.persistenz.hibernate.OcBehandlung;

/**
 * DAO interface for OcBehandlung
 */
public interface BehandlungDAO extends DAOUserTransaction {

    /**
     * persist a object, and return the generated id
     *
     * @param transientInstance
     * @return the generated id
     */
    public Integer persist(OcBehandlung transientInstance);

    /**
     * save or update object.
     *
     * @param instance
     */
    public void attachDirty(OcBehandlung instance);

    /**
     * has to do with locking.
     * 
     * @param instance
     */
    public void attachClean(OcBehandlung instance);

    /**
     * delete an instance.
     * 
     * @param persistentInstance
     */
    public void delete(OcBehandlung persistentInstance);

    /**
     * merge instances 
     *
     * @param detachedInstance
     * @return
     */
    public OcBehandlung merge(OcBehandlung detachedInstance);

    /**
     * find by primary key.
     *
     * @param id
     * @return
     */
    public OcBehandlung findById(int id);

    /**
     * find all entries.
     *
     * @return
     */
    public List<OcBehandlung> findAll();
}
