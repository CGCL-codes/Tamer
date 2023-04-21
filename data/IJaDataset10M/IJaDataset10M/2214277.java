package tinlizard.dao.jpa;

import tinlizard.model.PersistentObject;
import tinlizard.util.logging.Logger;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class AbstractBaseDao<E extends PersistentObject> {

    private final JpaDao dao;

    protected final Logger logger = Logger.getLogger(getClass());

    public AbstractBaseDao() {
        dao = JpaDao.getInstance();
    }

    protected abstract Class<E> getManagedClass();

    public final EntityManager getEm() {
        return dao.getEm();
    }

    public final void handleInfo(final EntityTransaction tx, final String msg, final Exception e) {
        dao.handleInfo(tx, msg, e);
    }

    public final void handleError(final EntityTransaction tx, final String msg, final Exception e) {
        dao.handleError(tx, msg, e);
    }

    public void add(final E obj) {
        dao.add(obj);
    }

    public void update(final E obj) {
        dao.update(obj);
    }

    public void delete(final E obj) {
        dao.delete(obj.getClass(), obj.getId());
    }

    public List<E> findAll() {
        return dao.findAll(getManagedClass());
    }

    public E findByPrimaryKey(final Integer id) {
        return dao.findByPrimaryKey(getManagedClass(), id);
    }

    public List<E> findByNamedQuery(final String name, final Object... params) {
        return dao.findByNamedQuery(getManagedClass(), name, params);
    }

    public E findSingleByNamedQuery(final String name, final Object... params) {
        return dao.findSingleByNamedQuery(getManagedClass(), name, params);
    }
}
