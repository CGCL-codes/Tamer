package ocumed.persistenz.dao;

import java.util.List;
import ocumed.persistenz.hibernate.OcRezept;

/**
 * DAO interface for OcRezept
 */
public interface RezeptDAO extends DAOUserTransaction {

    /**
     * persist a object, and return the generated id
     *
     * @param transientInstance
     * @return the generated id
     */
    public Integer persist(OcRezept transientInstance);

    /**
     * save or update object.
     *
     * @param instance
     */
    public void attachDirty(OcRezept instance);

    /**
     * has to do with locking.
     * 
     * @param instance
     */
    public void attachClean(OcRezept instance);

    /**
     * delete an instance.
     * 
     * @param persistentInstance
     */
    public void delete(OcRezept persistentInstance);

    /**
     * merge instances 
     *
     * @param detachedInstance
     * @return
     */
    public OcRezept merge(OcRezept detachedInstance);

    /**
     * find by primary key.
     *
     * @param id
     * @return
     */
    public OcRezept findById(int id);

    /**
     * find all entries.
     *
     * @return
     */
    public List<OcRezept> findAll();
}
