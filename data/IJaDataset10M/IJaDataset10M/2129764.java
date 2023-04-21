package ocumed.persistenz.dao;

import java.util.List;
import ocumed.persistenz.hibernate.OcPosition;

/**
 * DAO interface for OcPosition
 */
public interface PositionDAO extends DAOUserTransaction {

    /**
     * Table mapping
     */
    public static final char bestimmtSchatten = '1';

    /**
     * Table mapping
     */
    public static final char bestimmtKeinenSchatten = '0';

    /**
     * Table mapping
     */
    public static final char bestimmtFraktrometer = '1';

    /**
     * Table mapping
     */
    public static final char bestimmtKeinFraktrometer = '0';

    /**
     * Table mapping
     */
    public static final char bestimmtPhoropter = '1';

    /**
     * Table mapping
     */
    public static final char bestimmtKeinPhoropter = '0';

    /**
     * persist a object, and return the generated id
     *
     * @param transientInstance
     * @return the generated id
     */
    public Integer persist(OcPosition transientInstance);

    /**
     * save or update object.
     *
     * @param instance
     */
    public void attachDirty(OcPosition instance);

    /**
     * has to do with locking.
     * 
     * @param instance
     */
    public void attachClean(OcPosition instance);

    /**
     * delete an instance.
     * 
     * @param persistentInstance
     */
    public void delete(OcPosition persistentInstance);

    /**
     * merge instances 
     *
     * @param detachedInstance
     * @return
     */
    public OcPosition merge(OcPosition detachedInstance);

    /**
     * find by primary key.
     *
     * @param id
     * @return
     */
    public OcPosition findById(int id);

    /**
     * find all entries.
     *
     * @return
     */
    public List<OcPosition> findAll();
}
