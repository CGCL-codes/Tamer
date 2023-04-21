package br.com.bafonline.model.dao.implementation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import br.com.bafonline.model.dao.GenericDAO;
import br.com.bafonline.model.dto.BaseDTO;
import br.com.bafonline.model.hibernate.HibernateSessionFactory;
import br.com.bafonline.util.exception.dao.DAOException;
import br.com.bafonline.util.exception.dao.DeleteByIdException;
import br.com.bafonline.util.exception.dao.DeleteException;
import br.com.bafonline.util.exception.dao.FindAllException;
import br.com.bafonline.util.exception.dao.FindByExampleException;
import br.com.bafonline.util.exception.dao.FindByIdException;
import br.com.bafonline.util.exception.dao.FindException;
import br.com.bafonline.util.exception.dao.SaveException;
import br.com.bafonline.util.exception.dao.UpdateException;
import br.com.bafonline.util.exception.hibernate.HibernateSessionFactoryException;

/**
 * Classe DAO (Data Access Object) que tem por finalidade persistir e pesquisar dados 
 * de qualquer entidade do sistema, desde que a mesma estenda a entidade b�sica (BaseDTO). <br>
 * Implementa a interface <code>GenericDAO</code>.
 * @see br.com.bafonline.model.dao.GenericDAO
 * @see br.com.bafonline.model.dao.implementation.GenericDAOImpl
 * @author bafonline
 *
 */
public class GenericDAOImpl extends HibernateSessionFactory implements GenericDAO {

    private static final Log log = LogFactory.getLog(GenericDAOImpl.class);

    private int scope = REQUEST_SCOPE;

    public GenericDAOImpl() {
        super();
    }

    public Session getSession() throws HibernateSessionFactoryException {
        return currentSession();
    }

    public Session currentSession() throws HibernateSessionFactoryException {
        return super.getCurrentSession();
    }

    public void closeSession() throws HibernateSessionFactoryException {
        super.doCloseSession();
    }

    public void beginTransaction() throws HibernateSessionFactoryException {
        super.doBeginTransaction();
    }

    public void rollback() throws HibernateSessionFactoryException {
        super.doRollback();
    }

    public void commit() throws HibernateSessionFactoryException {
        super.doCommit();
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public boolean isLocalScope() {
        return this.scope == LOCAL_SCOPE;
    }

    public BaseDTO save(BaseDTO dto) throws DAOException {
        try {
            beginTransaction();
            currentSession().save(dto);
            currentSession().flush();
            if (isLocalScope()) commit();
            return dto;
        } catch (HibernateException e) {
            throw new SaveException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new SaveException(e);
        } catch (Exception e) {
            throw new SaveException(e);
        }
    }

    public BaseDTO update(BaseDTO dto) throws DAOException {
        try {
            beginTransaction();
            currentSession().update(dto);
            currentSession().flush();
            if (isLocalScope()) commit();
            return dto;
        } catch (HibernateException e) {
            throw new UpdateException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new UpdateException(e);
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }

    public boolean delete(BaseDTO dto) throws DAOException {
        try {
            beginTransaction();
            currentSession().delete(dto);
            currentSession().flush();
            if (isLocalScope()) commit();
            return true;
        } catch (HibernateException e) {
            throw new DeleteException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new DeleteException(e);
        } catch (Exception e) {
            throw new DeleteException(e);
        }
    }

    public boolean deleteById(Class<?> obj, Long id) throws DAOException {
        try {
            beginTransaction();
            currentSession().delete(findById(obj, id));
            currentSession().flush();
            if (isLocalScope()) commit();
            return true;
        } catch (HibernateException e) {
            throw new DeleteByIdException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new DeleteByIdException(e);
        } catch (DAOException e) {
            throw new DeleteByIdException(e);
        } catch (Exception e) {
            throw new DeleteByIdException(e);
        }
    }

    public boolean deleteById(Class<?> obj, String id) throws DAOException {
        try {
            beginTransaction();
            currentSession().delete(findById(obj, id));
            currentSession().flush();
            if (isLocalScope()) commit();
            return true;
        } catch (HibernateException e) {
            throw new DeleteByIdException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new DeleteByIdException(e);
        } catch (DAOException e) {
            throw new DeleteByIdException(e);
        } catch (Exception e) {
            throw new DeleteByIdException(e);
        }
    }

    public BaseDTO findById(Class<?> obj, Integer id) throws DAOException {
        try {
            Object rObj = currentSession().createCriteria(obj).add(Expression.idEq(id)).uniqueResult();
            if (isLocalScope()) closeSession();
            return (BaseDTO) rObj;
        } catch (HibernateException e) {
            throw new FindByIdException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindByIdException(e);
        } catch (Exception e) {
            throw new FindByIdException(e);
        }
    }

    public BaseDTO findById(Class<?> obj, Long id) throws DAOException {
        try {
            Object rObj = currentSession().createCriteria(obj).add(Expression.idEq(id)).uniqueResult();
            if (isLocalScope()) closeSession();
            return (BaseDTO) rObj;
        } catch (HibernateException e) {
            throw new FindByIdException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindByIdException(e);
        } catch (Exception e) {
            throw new FindByIdException(e);
        }
    }

    public BaseDTO findById(Class<?> obj, String id) throws DAOException {
        try {
            Object rObj = currentSession().createCriteria(obj).add(Expression.idEq(id)).uniqueResult();
            if (isLocalScope()) closeSession();
            return (BaseDTO) rObj;
        } catch (HibernateException e) {
            throw new FindByIdException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindByIdException(e);
        } catch (Exception e) {
            throw new FindByIdException(e);
        }
    }

    public List<?> findAll(Class<?> obj) throws DAOException {
        try {
            BaseDTO dto = (BaseDTO) obj.newInstance();
            List<?> list = null;
            if (dto.getNaturalOrder() != null) {
                list = currentSession().createCriteria(obj).addOrder(Order.asc(dto.getNaturalOrder())).list();
            } else {
                list = currentSession().createCriteria(obj).list();
            }
            if (isLocalScope()) closeSession();
            return list;
        } catch (HibernateException e) {
            throw new FindAllException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindAllException(e);
        } catch (InstantiationException e) {
            throw new FindAllException(e);
        } catch (IllegalAccessException e) {
            throw new FindAllException(e);
        } catch (Exception e) {
            throw new FindAllException(e);
        }
    }

    public List<?> findAll(Class<?> obj, String orderField) throws DAOException {
        try {
            Criteria criteria = currentSession().createCriteria(obj);
            if (orderField != null && !orderField.equals("")) {
                criteria.addOrder(Order.asc(orderField));
            }
            List<?> list = criteria.list();
            if (isLocalScope()) closeSession();
            return list;
        } catch (HibernateException e) {
            throw new FindAllException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindAllException(e);
        } catch (Exception e) {
            throw new FindAllException(e);
        }
    }

    public List<?> findByExample(BaseDTO dto, int matchMode, boolean ignoreCase, String orderField) throws DAOException {
        Example example = Example.create(dto);
        switch(matchMode) {
            case PERC_LIKE_PERC:
                example.enableLike(MatchMode.ANYWHERE);
                break;
            case LIKE_PERC:
                example.enableLike(MatchMode.END);
                break;
            case PERC_LIKE:
                example.enableLike(MatchMode.START);
                break;
            case LIKE:
                example.enableLike(MatchMode.EXACT);
                break;
            default:
                example.enableLike(MatchMode.ANYWHERE);
        }
        if (ignoreCase) {
            example.ignoreCase();
        }
        try {
            Criteria criteria = currentSession().createCriteria(dto.getClass());
            Field[] campos = dto.getClass().getDeclaredFields();
            if (campos != null) {
                if (campos.length > 0) {
                    for (int i = 0; i < campos.length; i++) {
                        String fieldName = campos[i].getName();
                        if (!fieldName.equals("serialVersionUID")) {
                            Class<?> cl = dto.getClass();
                            Class<?>[] par = new Class[0];
                            Method mthd = cl.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), par);
                            @SuppressWarnings("unused") Object output = mthd.invoke(dto, null);
                            if (output != null) {
                                if (output instanceof String) {
                                    if (!"".equals(output)) {
                                        criteria.add(Expression.like(campos[i].getName(), "'" + output + "'"));
                                    }
                                } else {
                                    criteria.add(Expression.eq(campos[i].getName(), output));
                                }
                            }
                        }
                    }
                }
            }
            if (orderField != null && !orderField.equals("")) {
                criteria.addOrder(Order.asc(orderField));
            } else {
                if (dto.getNaturalOrder() != null) {
                    criteria.addOrder(Order.asc(dto.getNaturalOrder()));
                }
            }
            List<?> list = criteria.list();
            if (isLocalScope()) closeSession();
            return list;
        } catch (HibernateException e) {
            throw new FindByExampleException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindByExampleException(e);
        } catch (Exception e) {
            throw new FindByExampleException(e);
        }
    }

    public List<?> findByExample(BaseDTO dto) throws DAOException {
        return findByExample(dto, DEFAULT_MATCH_MODE, DEFAULT_IGNORE_CASE, null);
    }

    public List<?> findByExample(BaseDTO dto, String orderField) throws DAOException {
        return findByExample(dto, DEFAULT_MATCH_MODE, DEFAULT_IGNORE_CASE, orderField);
    }

    public List<?> find(Class<?> obj, Criterion[] expressionList, Order[] orderList) throws DAOException {
        try {
            BaseDTO dto = (BaseDTO) obj.newInstance();
            Criteria criteria = currentSession().createCriteria(obj);
            if (expressionList != null) {
                for (int el = 0; el < expressionList.length; el++) {
                    criteria.add(expressionList[el]);
                }
            }
            if (orderList != null) {
                for (int ol = 0; ol < orderList.length; ol++) criteria.addOrder(orderList[ol]);
            } else {
                criteria.addOrder(Order.asc(dto.getNaturalOrder()));
            }
            List<?> list = criteria.list();
            if (isLocalScope()) closeSession();
            return list;
        } catch (HibernateException e) {
            throw new FindException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindException(e);
        } catch (InstantiationException e) {
            throw new FindException(e);
        } catch (IllegalAccessException e) {
            throw new FindException(e);
        } catch (Exception e) {
            throw new FindException(e);
        }
    }

    public List<?> find(Class<?> obj, Criterion[] expressionList) throws DAOException {
        return find(obj, expressionList, null);
    }

    public List<?> findByHQL(String hql) throws DAOException {
        return findByHQL(hql, null, null);
    }

    public List<?> findByHQL(String hql, Object[] values, Type[] types) throws DAOException {
        List<?> list = null;
        try {
            Query query = currentSession().createQuery(hql);
            if (values != null) {
                if (values.length != types.length) {
                    throw new DAOException("Quantidade de valores divergente da quantidade de tipos.");
                }
                for (int i = 0; i < values.length; i++) {
                    if (types[i] == Hibernate.BOOLEAN) query.setBoolean(i, ((Boolean) values[i]).booleanValue());
                    if (types[i] == Hibernate.BYTE) query.setByte(i, ((Byte) values[i]).byteValue());
                    if (types[i] == Hibernate.CHARACTER) query.setCharacter(i, ((String) values[i]).charAt(0));
                    if (types[i] == Hibernate.STRING) query.setString(i, (String) values[i]);
                    if (types[i] == Hibernate.SHORT) query.setShort(i, ((Short) values[i]).shortValue());
                    if (types[i] == Hibernate.INTEGER) query.setInteger(i, ((Integer) values[i]).intValue());
                    if (types[i] == Hibernate.LONG) query.setLong(i, ((Long) values[i]).longValue());
                    if (types[i] == Hibernate.FLOAT) query.setFloat(i, ((Float) values[i]).floatValue());
                    if (types[i] == Hibernate.BIG_DECIMAL) query.setBigDecimal(i, ((BigDecimal) values[i]).abs());
                    if (types[i] == Hibernate.DOUBLE) query.setDouble(i, ((Double) values[i]).doubleValue());
                    if (types[i] == Hibernate.DATE) query.setDate(i, (Date) values[i]);
                    if (types[i] == Hibernate.TIMESTAMP) query.setTimestamp(i, (Date) values[i]);
                    if (types[i] == Hibernate.CALENDAR) query.setCalendar(i, (Calendar) values[i]);
                    if (types[i] == Hibernate.CALENDAR_DATE) query.setCalendarDate(i, (Calendar) values[i]);
                    if (types[i] == Hibernate.BLOB) query.setBinary(i, (byte[]) values[i]);
                    if (types[i] == Hibernate.BINARY) query.setBinary(i, (byte[]) values[i]);
                }
            }
            list = query.list();
        } catch (HibernateException e) {
            throw new FindException(e);
        } catch (HibernateSessionFactoryException e) {
            throw new FindException(e);
        }
        return list;
    }

    /**
	 * Metodo que retorna uma lista de objetos do tipo BaseDTO, pesquisados pelos campos <i>propertyNames</i>
	 * com valores iguais a <i>values</i>.<br>
	 * Operador condicional padrao: GenericDAO.<b>AND</b><br>
	 * Operador de comparacao padrao: GenericDAO.<b>EQUAL</b>
	 * @param dto : Objeto BaseDTO utilizado com exemplo (filtro)
	 * @return List<?> : Lista com todos os objetos que satisfazem o filtro.
	 * @throws DAOException : Erro
	 */
    public List<?> findByProperties(BaseDTO dto) throws DAOException {
        return findByProperties(dto, GenericDAO.AND);
    }

    /**
	 * Metodo que retorna uma lista de objetos do tipo BaseDTO, pesquisados pelos campos <i>propertyNames</i>
	 * com valores iguais a <i>values</i>.<br>
	 * Operador de comparacao padrao: GenericDAO.<b>EQUAL</b>
	 * @param dto : Objeto BaseDTO utilizado com exemplo (filtro)
	 * @param condicional : Operador condicional utilizado na pesquisa (GenericDAO.AND ou GenericDAO.OR)
	 * @return List<?> : Lista com todos os objetos que satisfazem o filtro.
	 * @throws DAOException : Erro
	 */
    public List<?> findByProperties(BaseDTO dto, String condicional) throws DAOException {
        return findByProperties(dto, condicional, GenericDAO.EQUAL);
    }

    public List<?> findByProperties(BaseDTO dto, String condicional, String comparador) throws DAOException {
        return findByProperties(dto, condicional, comparador, dto.getNaturalOrder());
    }

    /**
	 * Metodo que retorna uma lista de objetos do tipo BaseDTO, pesquisados pelos campos <i>propertyNames</i>
	 * com valores iguais a <i>values</i>.<br>
	 * O operador condicional utilizado na pesquisa e <i>operador</i> (GenericDAO.AND ou GenericDAO.OR)
	 * @param dto : Objeto BaseDTO utilizado com exemplo (filtro)
	 * @param condicional : Operador condicional utilizado na pesquisa (GenericDAO.AND ou GenericDAO.OR)
	 * @param comparador : Operador comparador utilizado na pesquisa (GenericDAO.EQUAL ou GenericDAO.LIKE_DESC)
	 * @return List<?> : Lista com todos os objetos que satisfazem o filtro.
	 * @throws DAOException : Erro
	 */
    public List<?> findByProperties(BaseDTO dto, String condicional, String comparador, String orderField) throws DAOException {
        log.debug("Localizando registro de " + dto.getClass().getSimpleName() + ":");
        try {
            String queryString = "from " + dto.getClass().getSimpleName() + " as model where";
            Method[] methods = dto.getClass().getDeclaredMethods();
            Map<String, Object> parameters = new HashMap<String, Object>(0);
            Map<String, Type> parameterTypes = new HashMap<String, Type>(0);
            Criteria criteria = currentSession().createCriteria(dto.getClass());
            try {
                for (int i = 0; i < methods.length; i++) {
                    if (!"set".equals(methods[i].getName().substring(0, 3))) {
                        String name = methods[i].getName().substring(3);
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        Object output = methods[i].invoke(dto, null);
                        if (output != null && !(output instanceof Set) && !(output instanceof HashSet) && !name.equals("naturalOrder") && !name.equals("primaryKey")) {
                            log.debug("     Propriedade: " + name + ", Valor: " + output);
                            if (comparador.equals(GenericDAO.EQUAL)) {
                                queryString = queryString + " model." + name + " " + GenericDAO.EQUAL + " :" + name + " " + condicional;
                                parameters.put(name, output);
                                criteria.add(Restrictions.eq(name, output));
                            } else if (comparador.equals(GenericDAO.LIKE_DESC) || comparador.equals(GenericDAO.LIKE) || comparador.equals(GenericDAO.LIKE_SYMBOL) || comparador.equals(GenericDAO.LIKE_PERC)) {
                                if (output instanceof String) {
                                    if (!"".equals(output)) {
                                        queryString = queryString + " model." + name + " " + GenericDAO.LIKE_DESC + " :" + name + " " + condicional;
                                        parameters.put(name, output);
                                        parameterTypes.put(name, Hibernate.STRING);
                                        criteria.add(Restrictions.like(name, GenericDAO.LIKE_SYMBOL + output + GenericDAO.LIKE_SYMBOL));
                                    }
                                } else {
                                    queryString = queryString + " model." + name + " " + GenericDAO.EQUAL + " :" + name + " " + condicional;
                                    parameters.put(name, output);
                                    criteria.add(Restrictions.eq(name, output));
                                    if (output instanceof Short) {
                                        parameterTypes.put(name, Hibernate.SHORT);
                                    } else if (output instanceof Integer) {
                                        parameterTypes.put(name, Hibernate.INTEGER);
                                    } else if (output instanceof Long) {
                                        parameterTypes.put(name, Hibernate.LONG);
                                    } else if (output instanceof Float) {
                                        parameterTypes.put(name, Hibernate.FLOAT);
                                    } else if (output instanceof Double) {
                                        parameterTypes.put(name, Hibernate.DOUBLE);
                                    } else if (output instanceof Date) {
                                        parameterTypes.put(name, Hibernate.DATE);
                                    } else if (output instanceof Boolean) {
                                        parameterTypes.put(name, Hibernate.BOOLEAN);
                                    } else if (output instanceof Byte) {
                                        parameterTypes.put(name, Hibernate.BYTE);
                                    } else if (output instanceof BigDecimal) {
                                        parameterTypes.put(name, Hibernate.BIG_DECIMAL);
                                    } else if (output instanceof BigInteger) {
                                        parameterTypes.put(name, Hibernate.BIG_INTEGER);
                                    } else if (output instanceof Character) {
                                        parameterTypes.put(name, Hibernate.CHARACTER);
                                    } else if (output instanceof Object) {
                                        parameterTypes.put(name, Hibernate.OBJECT);
                                    }
                                }
                            }
                        }
                    }
                }
                if (queryString.trim().endsWith(condicional)) {
                    queryString = queryString.substring(0, queryString.length() - condicional.length()).trim();
                } else if (queryString.trim().endsWith("where")) {
                    queryString = queryString.substring(0, queryString.length() - "where".length()).trim();
                }
                if (orderField != null && !"".equals(orderField)) {
                    criteria.addOrder(Order.asc(orderField));
                }
                Query queryObject = getSession().createQuery(queryString);
                for (Iterator<String> it = parameters.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    if (comparador.equals(GenericDAO.EQUAL)) {
                        queryObject.setParameter(key, parameters.get(key));
                    } else {
                        if (parameters.get(key) instanceof String) {
                            queryObject.setParameter(key, GenericDAO.LIKE_SYMBOL + parameters.get(key) + GenericDAO.LIKE_SYMBOL, Hibernate.STRING);
                        } else {
                            queryObject.setParameter(key, parameters.get(key), parameterTypes.get(key));
                        }
                    }
                }
                return criteria.list();
            } catch (IllegalAccessException e) {
                throw new FindException(e);
            } catch (InvocationTargetException e) {
                throw new FindException(e);
            }
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find by property name failed", e);
        }
        return null;
    }

    public List<?> findByProperty(BaseDTO dto, String propertyName, Object value) throws DAOException {
        log.debug("Localizando registro de " + dto.getClass().getSimpleName() + " com propriedade: " + propertyName + ", valor: " + value);
        try {
            String queryString = "from " + dto.getClass().getSimpleName() + " as model where model." + propertyName + " = ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find by property name failed", e);
        }
        return null;
    }

    public List<?> findByProperties(BaseDTO dto, String[] propertyName, Object[] value, String operador) throws DAOException {
        log.debug("Localizando registro de " + dto.getClass().getSimpleName() + " com propriedade: " + propertyName + ", valor: " + value);
        if (propertyName == null || value == null || propertyName.length == 0 || value.length == 0) {
            throw new DAOException("Propriedades e/ou Valores inexistentes!");
        }
        try {
            String queryString = "from " + dto.getClass().getSimpleName() + " as model where ";
            for (int i = 0; i < propertyName.length; i++) {
                queryString = queryString + "model." + propertyName[i] + " = ?";
                if (propertyName.length > (i + 1)) {
                    queryString = queryString + " " + operador + " ";
                }
            }
            Query queryObject = getSession().createQuery(queryString);
            for (int i = 0; i < propertyName.length; i++) {
                queryObject.setParameter(i, value[i]);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find by property name failed", e);
        }
        return null;
    }
}
