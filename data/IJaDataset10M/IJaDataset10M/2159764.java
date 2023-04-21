package dsb.bar.tks.server.dao.session;

import javax.ejb.Local;
import dsb.bar.tks.server.dao.AbstractEJBDAO;
import dsb.bar.tks.server.persistence.model.session.Session;

/**
 * DAO for Session objects.
 */
@Local
public interface SessionDAO extends AbstractEJBDAO<Session, Long> {

    /**
	 * Get a reference to the current Session.
	 * 
	 * @return A reference to the current Session, or <code>null</code> if there
	 *         is none.
	 */
    public Session getCurrentSession();
}
