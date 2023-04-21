package br.perfiman.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import br.perfiman.dao.UserDAOService;
import br.perfiman.model.User;

/**
 *
 * @author Wagner Costa
 * @since 27/12/2007
 * @version 1.0
 *
 */
@Stateless
public class UserDAOServiceBean extends AbstractDAOServiceBean<User> implements UserDAOService {

    @PersistenceContext(unitName = "perfiman")
    public EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
