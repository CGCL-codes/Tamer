package cw.roommanagementmodul.persistence;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import cw.boardingschoolmanagement.persistence.CWPersistenceManager;
import cw.boardingschoolmanagement.persistence.HibernateUtil;
import cw.customermanagementmodul.customer.persistence.Customer;

/**
 *
 * @author Dominik
 */
public class PMGebuehrenKat extends CWPersistenceManager<GebuehrenKategorie> {

    private static PMGebuehrenKat instance;

    private static Logger logger = Logger.getLogger(PMGebuehrenKat.class.getName());

    private PMGebuehrenKat() {
        super(GebuehrenKategorie.class);
    }

    public static PMGebuehrenKat getInstance() {
        if (instance == null) {
            instance = new PMGebuehrenKat();
        }
        return instance;
    }

    public GebuehrenKategorie create(EntityManager entityManager) {
        GebuehrenKategorie gebuehrenKategorie = new GebuehrenKategorie(entityManager);
        entityManager.persist(gebuehrenKategorie);
        return gebuehrenKategorie;
    }

    public List<GebuehrenKategorie> getAll(EntityManager entityManager) {
        return setEntityManager(entityManager.createQuery("FROM " + GebuehrenKategorie.ENTITY_NAME).getResultList(), entityManager);
    }

    @Override
    public int countAll(EntityManager entityManager) {
        return ((Long) entityManager.createQuery("SELECT " + "COUNT(*)" + " FROM " + GebuehrenKategorie.ENTITY_NAME).getResultList().iterator().next()).intValue();
    }
}
