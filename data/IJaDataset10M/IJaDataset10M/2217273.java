package br.com.bafonline.model.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;
import br.com.bafonline.model.dto.BaseDTO;
import br.com.bafonline.util.exception.dao.DAOException;
import br.com.bafonline.util.exception.hibernate.HibernateSessionFactoryException;

/**
 * Classe DAO (Data Access Object) gen�rica que tem por finalidade persistir e pesquisar dados 
 * da entidade b�sica <code>BaseDTO</code>. <br>
 * @see br.com.bafonline.model.dto.BaseDTO
 * @see br.com.bafonline.model.dao.implementation.GenericDAOImpl
 * @author bafonline
 *
 */
public interface GenericDAO {

    public static final int REQUEST_SCOPE = 1;

    public static final int LOCAL_SCOPE = 1;

    public static final int PERC_LIKE_PERC = 1;

    public static final int LIKE_PERC = 2;

    public static final int PERC_LIKE = 3;

    public static final int LIKE = 4;

    public static final String AND = "and";

    public static final String OR = "or";

    public static final String EQUAL = "=";

    public static final String LIKE_DESC = "like";

    public static final String LIKE_SYMBOL = "%";

    public static final String WHERE = "where";

    public static final int DEFAULT_MATCH_MODE = PERC_LIKE_PERC;

    public static final boolean DEFAULT_IGNORE_CASE = true;

    public Session getSession() throws HibernateSessionFactoryException;

    public Session currentSession() throws HibernateSessionFactoryException;

    public void closeSession() throws HibernateSessionFactoryException;

    public void beginTransaction() throws HibernateSessionFactoryException;

    public void rollback() throws HibernateSessionFactoryException;

    public void commit() throws HibernateSessionFactoryException;

    public void setScope(int scope);

    public int getScope();

    public boolean isLocalScope();

    public BaseDTO save(BaseDTO dto) throws DAOException;

    public BaseDTO update(BaseDTO dto) throws DAOException;

    public boolean delete(BaseDTO dto) throws DAOException;

    public boolean deleteById(Class<?> obj, Long id) throws DAOException;

    public boolean deleteById(Class<?> obj, String id) throws DAOException;

    public BaseDTO findById(Class<?> obj, Integer id) throws DAOException;

    public BaseDTO findById(Class<?> obj, Long id) throws DAOException;

    public BaseDTO findById(Class<?> obj, String id) throws DAOException;

    public List<?> findAll(Class<?> obj) throws DAOException;

    public List<?> findAll(Class<?> obj, String orderField) throws DAOException;

    public List<?> findByExample(BaseDTO dto, int matchMode, boolean ignoreCase, String orderField) throws DAOException;

    public List<?> findByExample(BaseDTO dto) throws DAOException;

    public List<?> findByExample(BaseDTO dto, String orderField) throws DAOException;

    public List<?> find(Class<?> obj, Criterion[] expressionList, Order[] orderList) throws DAOException;

    public List<?> find(Class<?> obj, Criterion[] expressionList) throws DAOException;

    public List<?> findByHQL(String hql) throws DAOException;

    public List<?> findByHQL(String hql, Object[] values, Type[] types) throws DAOException;

    public List<?> findByProperties(BaseDTO dto) throws DAOException;

    public List<?> findByProperties(BaseDTO dto, String condicional) throws DAOException;

    public List<?> findByProperties(BaseDTO dto, String condicional, String comparador) throws DAOException;

    public List<?> findByProperties(BaseDTO dto, String condicional, String comparador, String orderField) throws DAOException;

    public List<?> findByProperty(BaseDTO dto, String propertyName, Object value) throws DAOException;
}
