package ubs.persistence.evidence;

import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Rommel Carvalho
 *
 * This classe is responsible for 
 */
public interface LocalCaseHome extends EJBLocalHome {

    public LocalCase create(int id, String name, String description) throws CreateException;

    public LocalCase findByPrimaryKey(int id) throws FinderException;

    public Collection findByGroup(int groupId) throws FinderException;

    public Collection findByEvidence(int evidenceId) throws FinderException;

    public Collection findByName(String name) throws FinderException;

    public Collection findByDescription(String description) throws FinderException;

    public Collection findByGroupAndName(int groupId, String name) throws FinderException;

    public Collection findByGroupAndNameAndDescription(int groupId, String name, String description) throws FinderException;

    public Collection findByGroupAndDescription(int groupId, String description) throws FinderException;

    public Collection findByNameAndDescription(String name, String description) throws FinderException;

    public Collection findAll() throws FinderException;
}
