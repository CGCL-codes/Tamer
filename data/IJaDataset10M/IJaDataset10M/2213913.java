package shiva.session.persister;

import java.util.List;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 */
@SuppressWarnings("unchecked")
public interface EntityPersister {

    /**
	 * 
	 * 
	 * @param ldapEntity
	 */
    public boolean exists(Object ldapEntity);

    /**
	 * 
	 * 
	 * @param ldapEntity
	 */
    public void persist(Object ldapEntity);

    /**
	 * 
	 * 
	 * @param ldapEntity
	 */
    public void update(Object ldapEntity);

    /**
	 * 
	 * 
	 * @param ldapEntity
	 */
    public void delete(Object ldapEntity);

    /**
	 * 
	 * @param clazz
	 * @return
	 */
    public List getAll(Class clazz);
}
