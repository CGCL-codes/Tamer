package org.systemsbiology.apps.gui.server.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.systemsbiology.apps.gui.domain.ScoredTransition;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorSetup;

/**
 * Data access object for scored transitions
 * 
 * @author Mark Christiansen
 * @author Chris Kwok
 * 
 * @see ScoredTransition
 */
public class ScoredTransitionDAO extends DAO {

    private static final Logger log = Logger.getLogger(ScoredTransitionDAO.class.getName());

    /**
	 * Null constructor
	 */
    public ScoredTransitionDAO() {
        log.debug("ScoredTransitionDAO - Constructor ");
    }

    /**
	 * Get a list of scored transitions by association with an identifier for the transition list validator setup
	 * @param id identifier of a transition list validator setup
	 * @return List of Scored Transitions
	 * @throws DAOException exception with the data access object layer
	 * 
	 * @see TransitionListValidatorSetup
	 */
    @SuppressWarnings("unchecked")
    public List<ScoredTransition> getByTlvSetupId(Long id) throws DAOException {
        List<ScoredTransition> allScoredTransitions = null;
        try {
            begin();
            Query q = getSession().createQuery("from ScoredTransition where tlvSetup = :id");
            q.setLong("id", id);
            log.debug("ScoredTransitionDao getBySetupId() - getting result list");
            allScoredTransitions = new ArrayList(q.list());
            close();
        } catch (HibernateException e) {
            throw new DAOException("Could not get ScoredTransition list ", e);
        }
        return allScoredTransitions;
    }

    /**
	 * Get transition list validator setup by association with an identifier for the transition list validator setup 
	 * @param id identifier of a transition list validator setup 
	 * @param decoy if <code>true</code> then the scored transition is a decoy
	 * @return list of scored transitions
	 * @throws DAOException exception with the data access object layer
	 */
    @SuppressWarnings("unchecked")
    public List<ScoredTransition> getByTlvSetupId(Long id, Boolean decoy) throws DAOException {
        List<ScoredTransition> allScoredTransitions = null;
        try {
            begin();
            Query q = getSession().createQuery("from ScoredTransition where tlvSetup = :id and decoy = :decoy");
            q.setLong("id", id);
            q.setBoolean("decoy", decoy);
            log.debug("ScoredTransitionDao getBySetupId() & DECOY - getting result list");
            allScoredTransitions = new ArrayList(q.list());
            close();
        } catch (HibernateException e) {
            throw new DAOException("Could not get ScoredTransition list ", e);
        }
        return allScoredTransitions;
    }

    /**
	 * Delete all Scored Transition associated with a transition list validator setup with the given id
	 * @param id identifier for a transition list validator setup 
	 * @throws DAOException exception with the data access object layer
	 */
    public void deleteByTlvSetupId(Long id) throws DAOException {
        try {
            log.debug("ScoredTransitionDao deleteBySetupId()");
            List<ScoredTransition> scoredTransitions = getByTlvSetupId(id);
            if (scoredTransitions != null) {
                begin();
                for (ScoredTransition scoredTransition : scoredTransitions) s.delete(scoredTransition);
                commit();
                close();
            }
        } catch (HibernateException e) {
            rollback();
            throw new DAOException("Could not delete ScoredTransition list ", e);
        }
    }
}
