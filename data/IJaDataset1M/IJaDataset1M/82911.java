package es.eside.deusto.pfc.kernel.ifc.dao.h3;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import es.eside.deusto.pfc.kernel.ifc.bo.IPersistable;

public interface IDAOSupport {

    public Connection connection();

    public void setFlushMode(int mode);

    public void flush();

    public void persist(IPersistable po);

    public void delete(IPersistable po);

    public void update(IPersistable po);

    public void refresh(IPersistable po);

    public Object findByPK(Class clazz, Serializable pk);

    public List genericFind(Class po, Method[] methods, Object[] reqResults);

    public List findByCriteria(final Class target, final Collection criterion);

    public List paginatedFindByCriteria(final Class target, final Collection criterion, final int first, final int max);

    public List findByNamedQuery(final String queryName, final Collection params);

    public Object findUniqueByNamedQuery(final String query, final Collection params);

    public List findByNamedQuery(final String queryName);

    public void saveOrUpdate(IPersistable po);
}
