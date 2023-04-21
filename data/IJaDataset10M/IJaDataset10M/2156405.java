package org.systemsbiology.apps.gui.server.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.systemsbiology.apps.gui.domain.ATAQSProject;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorSetup;

/**
 * Data access object for the transition list validator setup stage
 *  
 * @author Mark Christiansen
 * 
 * @see TransitionListValidatorSetup
 */
public class TransitionListValidatorSetupDAO extends DAO {

    private static final Logger log = Logger.getLogger(TransitionListValidatorSetupDAO.class.getName());

    /**
	 * default constructor
	 */
    public TransitionListValidatorSetupDAO() {
        log.debug("ATAQSProjectDAO - Constructor ");
    }

    /**
	 * Get the transition list publisher setup object for a given project
	 * @param project project to get the transition list validator setup object for
	 * @return transition list publisher setup 
	 * @throws DAOException exception with the data access object layer
	 */
    public TransitionListValidatorSetup getTLVSetup(ATAQSProject project) throws DAOException {
        TransitionListValidatorSetup tlvSetup = null;
        try {
            begin();
            if (project.getTlvSetup() == null) {
                log.debug("Unable to get TLVSetup for Project: " + project.getId() + "\t Making project");
                tlvSetup = new TransitionListValidatorSetup();
                tlvSetup.setProject(project);
                s.save(tlvSetup);
                project.setTlvSetup(tlvSetup);
                s.saveOrUpdate(project);
                commit();
            } else {
                String q = "select tlvSetup from TransitionListValidatorSetup as tlvSetup where id = :id ";
                Query query = getSession().createQuery(q);
                query.setParameter("id", project.getTlvSetup().getId());
                tlvSetup = (TransitionListValidatorSetup) query.uniqueResult();
            }
            log.debug("Able to get TLVSetup: " + tlvSetup.getId() + " \tfor Project: " + project.getId());
            close();
        } catch (HibernateException e) {
            rollback();
            throw new DAOException("Could not get TLVSetup" + project.getTitle(), e);
        }
        return tlvSetup;
    }

    /**
	 * Save the transition list validator setup 
	 * @param tlvSetup transition list validator setup to save
	 * @throws DAOException exception with the data access object layer
	 */
    public void saveTLVSetup(TransitionListValidatorSetup tlvSetup) throws DAOException {
        try {
            begin();
            s.saveOrUpdate(tlvSetup);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            throw new DAOException("Could not save TLVSetup" + tlvSetup.toString(), e);
        }
    }

    /**
	 * Merge the transition list validator setup with a persistent object
	 * @param tlvSetup transition list validator setup to merge
	 * @throws DAOException exception with the data access object layer
	 */
    public void mergeTLVSetup(TransitionListValidatorSetup tlvSetup) throws DAOException {
        try {
            begin();
            s.merge(tlvSetup);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            throw new DAOException("Could not merge TLVSetup" + tlvSetup.toString(), e);
        }
    }

    /**
	 * Delete a transition list validator setup 
	 * @param tlvSetup transition list validator setup to delete
	 * @throws DAOException exception with the data access object layer
	 */
    public void deleteTLVSetup(TransitionListValidatorSetup tlvSetup) throws DAOException {
        try {
            begin();
            s.delete(tlvSetup);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            throw new DAOException("Could not delete ATAQSProject TLVSetup" + tlvSetup.toString(), e);
        }
    }
}
