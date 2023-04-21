package net.sf.bootstrap.framework.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Contract for Data Access Objects (DAO).
 *
 * @author Mark Moloney
 */
public interface DAO {

    public void refresh(Object obj);

    public void store(Object obj);

    public Object load(Class target, Serializable id);

    public List findAll(Class target);

    public void remove(Class target, Serializable id);
}
