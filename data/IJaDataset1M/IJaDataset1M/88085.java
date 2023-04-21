package banking_system.hibernate.dao.iface;

import java.io.Serializable;

public interface DefaultAccountDAO {

    public banking_system.hibernate.DefaultAccount get(banking_system.hibernate.DefaultAccountPK key);

    public banking_system.hibernate.DefaultAccount load(banking_system.hibernate.DefaultAccountPK key);

    public java.util.List<banking_system.hibernate.DefaultAccount> findAll();

    /**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param defaultAccount a transient instance of a persistent class 
	 * @return the class identifier
	 */
    public banking_system.hibernate.DefaultAccountPK save(banking_system.hibernate.DefaultAccount defaultAccount);

    /**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param defaultAccount a transient instance containing new or updated state 
	 */
    public void saveOrUpdate(banking_system.hibernate.DefaultAccount defaultAccount);

    /**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param defaultAccount a transient instance containing updated state
	 */
    public void update(banking_system.hibernate.DefaultAccount defaultAccount);

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
    public void delete(banking_system.hibernate.DefaultAccountPK id);

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param defaultAccount the instance to be removed
	 */
    public void delete(banking_system.hibernate.DefaultAccount defaultAccount);
}
