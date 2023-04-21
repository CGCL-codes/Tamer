package framework.persistence;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * This class represents the generic DAO pattern to acces application data.
 * @author <B>Marcos Tadeu Silva</B>
 *         e-mail: <I>zero.ufal@gmail.com</I>, 08/31/2007.
 * @version 0.1
 * @since first
 */
public class GenericDAO<P extends Persistentable> {

    private Session session;

    /**
     * Creates a new Instance of GenericDAO instanciating the Session.
     */
    public GenericDAO() {
        session = HibernateUtil.getSession();
    }

    /**
     * Saves a Persistentable object in the data repositorie.
     * @param p a persistentable object to be saved.
     * @throws DAOException if troubles occurs during the create action at the
     * data repositorie.
     */
    public void create(final P p) throws DAOException {
        try {
            System.out.println("se: " + session);
            session.save(p);
        } catch (HibernateException he) {
            throw new DAOException(he.getMessage(), he.getCause());
        }
    }

    /**
     * Removes a existent Persistentable object in the data repositorie.
     * @param p a persistentable object to be removed.
     * @throws DAOException if troubles occurs during the delete action at the
     * data repositorie.
     */
    public void remove(final P p) throws DAOException {
        try {
            session.delete(p);
        } catch (HibernateException he) {
            throw new DAOException(he.getMessage(), he.getCause());
        }
    }

    /**
     * Retrieves all existent Persistentable objects in the data repositorie.
     *
     * @param p a persistentable object template to retrieve all same objects.
     * @throws DAOException if troubles occurs during the retrieve action at the
     * data repositorie.
     * @return the list of all found objects in the data repositorie.
     */
    public List<P> retrieve(final P p) throws DAOException {
        try {
            List<P> result = (List<P>) session.createQuery("from " + p.getClass().getSimpleName()).list();
            return result;
        } catch (HibernateException he) {
            throw new DAOException(he.getMessage(), he.getCause());
        }
    }

    /**
     * Updates a existent Persistentable object in the data repositorie.
     * @param p a persistentable object to be updated.
     * @throws DAOException if troubles occurs during the update action at the
     * data repositorie.
     */
    public void update(final P p) throws DAOException {
        try {
            session.update(p);
        } catch (HibernateException he) {
            throw new DAOException(he.getMessage(), he.getCause());
        }
    }
}
