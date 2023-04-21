package whf.framework.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import whf.framework.dao.DAO;
import whf.framework.entity.Entity;
import whf.framework.exception.CreateException;
import whf.framework.exception.DAOException;
import whf.framework.exception.DuplicateException;
import whf.framework.exception.FindException;
import whf.framework.exception.NotFoundException;
import whf.framework.exception.RemoveException;
import whf.framework.exception.ServiceException;
import whf.framework.exception.UpdateException;
import whf.framework.i18n.ApplicationResource;
import whf.framework.log.Log;
import whf.framework.log.LogFactory;
import whf.framework.meta.Meta;
import whf.framework.meta.MetaFactory;
import whf.framework.meta.MetaNotFoundException;
import whf.framework.util.BeanFactory;
import whf.framework.util.BeanUtils;
import whf.framework.util.DateUtils;
import whf.framework.util.StringUtils;
import whf.framework.util.ThreadContext;
import whf.framework.util.Utils;

/**
 * @author wanghaifeng
 *
 */
@SuppressWarnings("unchecked")
public class ServiceImp<T extends Entity> extends SpringService implements Service<T> {

    private static Log log = LogFactory.getLog(ServiceImp.class);

    protected Class<T> entityClass;

    protected DAO<T> entityDAO;

    private Meta meta;

    /**
	 * @return 对应的dao对象
	 */
    protected DAO<T> getEntityDAO() {
        try {
            if (this.entityDAO == null) {
                this.entityDAO = BeanFactory.getDao(this.getClass());
            }
        } catch (Exception e) {
            log.error(e);
            throw new DaoNotFoundException(e);
        }
        return this.entityDAO;
    }

    /**
	 * @return 对应的元数据
	 */
    public Meta getMeta() {
        if (this.meta == null) {
            this.meta = MetaFactory.findByServiceClass(getClass());
        }
        if (this.meta == null) {
            MetaNotFoundException e = new MetaNotFoundException();
            log.error(e);
            throw e;
        }
        return this.meta;
    }

    /**
	 * @modify wanghaifeng Aug 25, 2006 12:55:47 PM
	 * @return 当前业务对象的boClass
	 */
    public Class<T> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = this.getMeta().getModelClass();
        }
        return this.entityClass;
    }

    public void create(T entity) throws CreateException {
        if (entity.getCreateBy() == null) {
            entity.setCreateBy(ThreadContext.getUserInUserContext());
        }
        entity.setCreateDate(DateUtils.getNow());
        this.getEntityDAO().create(entity);
    }

    public T duplicate(T entity, int deepth) throws DuplicateException, CreateException {
        if (entity == null) throw new DuplicateException(ApplicationResource.get("exception.illegal_arguments"));
        Map<String, Object> properties = Utils.newHashMap();
        properties.put("createBy", ThreadContext.getUserInUserContext());
        properties.put("id", new Long(0));
        T target = (T) BeanUtils.duplicate(entity, properties, deepth);
        this.entityDAO.create(target);
        return target;
    }

    public T duplicate(T entity) throws DuplicateException, CreateException {
        return this.duplicate(entity, 0);
    }

    public List<T> findAll() throws FindException {
        return this.getEntityDAO().findAll();
    }

    public List<T> findAll(int start, int rowsPerPage) throws FindException {
        return this.getEntityDAO().findAll(start, rowsPerPage);
    }

    public T findByPrimaryKey(Serializable key) throws NotFoundException {
        return this.getEntityDAO().findByPrimaryKey(key);
    }

    public T findByCode(String code) throws FindException {
        return this.getEntityDAO().findByCode(code);
    }

    public List<T> find(String query) throws FindException {
        return this.getEntityDAO().find(query);
    }

    public List<T> find(String query, Object arg) throws FindException {
        return this.getEntityDAO().find(query, arg);
    }

    public List<T> find(String query, Object[] args) throws FindException {
        return this.getEntityDAO().find(query, args);
    }

    public void remove(T entity) throws RemoveException {
        this.getEntityDAO().remove(entity);
    }

    public void removeByPrimaryKey(Serializable key) throws RemoveException, NotFoundException {
        T entity = this.getEntityDAO().findByPrimaryKey(key);
        this.remove(entity);
    }

    public void removeByPrimaryKey(Serializable[] keys) throws RemoveException, NotFoundException {
        for (Serializable key : keys) {
            T entity = this.getEntityDAO().findByPrimaryKey(key);
            this.remove(entity);
        }
    }

    public void update(T entity) throws UpdateException {
        if (entity.getUpdateBy() == null) {
            entity.setUpdateBy(ThreadContext.getUserInUserContext());
        }
        entity.setUpdateDate(DateUtils.getNow());
        this.getEntityDAO().update(entity);
    }

    public List<T> list(String queryString, int start, int rowsPerPage) {
        return getEntityDAO().list(queryString, start, rowsPerPage);
    }

    public List<T> list(String queryString, Object[] paramValues, int start, int rowsPerPage) {
        return getEntityDAO().list(queryString, paramValues, start, rowsPerPage);
    }

    public List<T> list(String queryString, String[] paramNames, Object[] paramValues, int start, int rowsPerPage) {
        return getEntityDAO().list(queryString, paramNames, paramValues, start, rowsPerPage);
    }

    public int getRowCount(String conditionString) throws DAOException {
        return getEntityDAO().getRowCount(conditionString);
    }

    public int getRowCount(String fromAndWhere, Object[] params) throws FindException {
        return getEntityDAO().getRowCount(fromAndWhere, params);
    }

    public Object invokeMethod(String methodName, Class[] types, Object[] params) throws ServiceException, NoSuchMethodException {
        Class clazz = this.getClass();
        try {
            Method method = clazz.getMethod(methodName, types);
            Object result = method.invoke(this, params);
            return result;
        } catch (NoSuchMethodException ne) {
            log.error(this, ne);
            throw ne;
        } catch (Exception e) {
            log.error(this, e);
            throw new ServiceException(e);
        }
    }

    public List<T> query(String propertyString, int start, int rowsPerPage) throws FindException {
        return getEntityDAO().query(propertyString, start, rowsPerPage);
    }

    public List<T> query(String propertyString) throws FindException {
        return getEntityDAO().query(propertyString);
    }

    public List<T> queryWithoutLazy(String queryString) throws FindException {
        return getEntityDAO().queryWithoutLazy(queryString);
    }

    public List<T> queryWithoutLazy(String queryString, int start, int pageSize) throws FindException {
        return getEntityDAO().queryWithoutLazy(queryString, start, pageSize);
    }

    public int getQueryRowCount(String propertyString) throws FindException {
        return getEntityDAO().getQueryRowCount(propertyString);
    }

    public void removeByPrimaryKey(long key) throws RemoveException {
        try {
            T entity = this.getEntityDAO().findByPrimaryKey(key);
            this.remove(entity);
        } catch (Exception e) {
            throw new RemoveException(e);
        }
    }

    public void removeByPrimaryKey(long[] keys) throws RemoveException, NotFoundException {
        try {
            for (long key : keys) {
                T entity = this.getEntityDAO().findByPrimaryKey(key);
                this.remove(entity);
            }
        } catch (Exception e) {
            throw new RemoveException(e);
        }
    }

    public List<T> findWithoutLazy(String queryString) throws FindException {
        return this.getEntityDAO().findWithoutLazy(queryString);
    }

    public List<T> findWithoutLazy(String queryString, Object[] params) throws FindException {
        return this.getEntityDAO().findWithoutLazy(queryString, params);
    }

    public T findByPrimaryKeyWithoutLazy(Serializable key) throws NotFoundException {
        return this.getEntityDAO().findByPrimaryKeyWithoutLazy(key);
    }

    public List<T> findByProperty(Map<String, Object> properties, int start, int pageSize) throws FindException {
        return this.getEntityDAO().findByProperties(properties, start, pageSize);
    }

    public List<T> findWithoutLazy(String queryString, int start, int pageSize) throws FindException {
        return this.getEntityDAO().findWithoutLazy(queryString, start, pageSize);
    }

    public List<T> findWithoutLazy(String queryString, Object arg, int start, int pageSize) throws FindException {
        return this.getEntityDAO().findWithoutLazy(queryString, arg, start, pageSize);
    }

    public List<T> findWithoutLazy(String queryString, Object arg) throws FindException {
        return this.getEntityDAO().findWithoutLazy(queryString, arg);
    }

    public List<T> findWithoutLazy(String queryString, Object[] params, int start, int pageSize) throws FindException {
        return this.getEntityDAO().findWithoutLazy(queryString, params, start, pageSize);
    }

    public List<T> findAllWithoutLazy() throws FindException {
        return this.getEntityDAO().findWithoutLazy(" from " + this.getEntityClass().getName());
    }

    public List<T> findAllWithoutLazy(int start, int pageSize) throws FindException {
        return this.getEntityDAO().findWithoutLazy(" from " + this.getEntityClass().getName(), start, pageSize);
    }

    /**
	 * 一个缺省的树实现方法，并非所有的service均可以使用这个方法，
	 * 只有那些业务对象实现了ITreeItem的对象的服务才可以
	 * @param parent
	 * @param queryString
	 * @return
	 */
    public List<T> findChildren(T parent, String queryString) {
        List<T> list = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (parent == null) {
                sb.append(" where t.parent is null ");
                if (!StringUtils.isEmpty(queryString)) {
                    sb.append(" and ").append(queryString);
                }
                list = find(sb.toString());
            } else {
                sb.append(" where t.parent = ?");
                if (!StringUtils.isEmpty(queryString)) {
                    sb.append(" and ").append(queryString);
                }
                list = find(sb.toString(), parent);
            }
        } catch (FindException e) {
            log.error(this, e);
        }
        return list;
    }
}
