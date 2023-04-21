package org.directdemocracyportal.democracy.service.dao.hibernate;

import org.directdemocracyportal.democracy.model.application.Event;
import org.directdemocracyportal.democracy.service.dao.EventDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class HibernateEventDAO.
 */
@Transactional(propagation = Propagation.MANDATORY)
public class HibernateEventDAO extends GenericHibernateDAO<Event, Long> implements EventDAO {

    /**
     * Instantiates a new hibernate event dao.
     */
    public HibernateEventDAO() {
        super(Event.class);
    }
}
