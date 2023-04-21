package net.sf.sail.webapp.dao.authentication;

import net.sf.sail.webapp.dao.SimpleDao;
import net.sf.sail.webapp.domain.authentication.MutableGrantedAuthority;

/**
 * Interface that extends <code>SimpleDao</code> used for
 * <code>GrantedAuthority</code>.
 * 
 * @author Cynick Young
 * 
 * @version $Id: GrantedAuthorityDao.java 257 2007-03-30 14:59:02Z cynick $
 * 
 */
public interface GrantedAuthorityDao<T extends MutableGrantedAuthority> extends SimpleDao<T> {

    /**
     * Given a string representing a role, determines if this granted authority
     * has this role.
     * 
     * @param authority
     *            The role string
     * @return True if the GrantedAuthority has this role, false otherwise.
     */
    public boolean hasRole(String authority);

    /**
     * Given an input string retrieve a corresponding record from data store.
     * 
     * @param name
     *            A string representing the name of the data in the data store.
     * @return A new instance of a data object.
     */
    public T retrieveByName(String name);
}
