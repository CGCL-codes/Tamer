package ces.arch.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate.HibernateTemplate;
import ces.arch.dao.hibernate.Hibernate2DAOImpl;
import ces.arch.query.ListQuery;
import ces.arch.query.ListQury;

/**
 * Ĭ����ݷ��ʶ��󣬲���Hibernateʵ����ݿ����
 *
 * @author ������
 *
 */
public class DefaultDao extends Hibernate2DAOImpl implements IDao {

    Log log = LogFactory.getLog(this.getClass());

    public List find(ListQuery filter) throws DAOException {
        List result = null;
        String hql = filter.getQuery();
        Map valuesMap = filter.getParameterMap();
        HibernateTemplate ht = getHibernateTemplate();
        Session session = null;
        try {
            log.debug("query hql : " + hql.toString());
            int from_index = hql.indexOf("from");
            String from_hql = hql.substring(from_index);
            session = ht.getSessionFactory().openSession();
            if (hql.indexOf("group") < 0) {
                Query countQuery = session.createQuery("select count(*) " + from_hql);
                setParameter(countQuery, valuesMap);
                Iterator it = valuesMap.values().iterator();
                while (it.hasNext()) {
                    System.out.print("** " + it.next());
                }
                filter.setTotalNum(((Integer) countQuery.uniqueResult()).intValue());
                int total = filter.getTotalNum();
                int pageN = filter.getCursor();
                if (pageN >= total) {
                    filter.setCursor(0);
                }
            }
            Query query = session.createQuery(hql);
            query.setFirstResult(filter.getCursor());
            int pSize = filter.getPageSize();
            if (pSize > 0) {
                query.setMaxResults(pSize);
            }
            setParameter(query, valuesMap);
            result = query.list();
            log.debug("result.size : " + result.size());
        } catch (HibernateException e) {
            log.error("��ҳ��ѯ����", e);
            throw new DAOException("��ҳ��ѯ����", e);
        } finally {
            try {
                session.close();
            } catch (HibernateException e) {
                log.error("sesion.close() : ", e);
            }
        }
        return result;
    }

    public List find(ces.arch.query.Query query) {
        String hql = query.getQuery();
        Map valuesMap = query.getParameterMap();
        Object[] parameter = new Object[valuesMap.size()];
        valuesMap.entrySet().toArray(parameter);
        List result = getHibernateTemplate().find(hql, parameter);
        return result;
    }

    private void setParameter(Query query, Map valuesMap) {
        Iterator keyIt = valuesMap.keySet().iterator();
        while (keyIt.hasNext()) {
            String key = (String) keyIt.next();
            Object value = valuesMap.get(key);
            Type type = getType(value);
            log.debug("key:" + key + " value:" + value.toString() + " type:" + type.toString());
            query.setParameter(key, value, type);
        }
    }

    /**
	 * Ŀǰ֧�ֵ��Զ�ת�����Ͱ�����String,Integer,Date,Time,TimeStamp,Boolean,Byte,Character,Double
	 *
	 * @param value
	 * @return
	 */
    private Type getType(Object value) {
        Type type = Hibernate.STRING;
        if (value == null || value instanceof String) type = Hibernate.STRING; else if (value instanceof Integer) type = Hibernate.INTEGER; else if (value instanceof Date) type = Hibernate.DATE; else if (value instanceof Time) type = Hibernate.TIME; else if (value instanceof Timestamp || value instanceof java.util.Date) type = Hibernate.TIMESTAMP; else if (value instanceof Long) type = Hibernate.LONG; else if (value instanceof Boolean) type = Hibernate.BOOLEAN; else if (value instanceof Byte) type = Hibernate.BYTE; else if (value instanceof Character) type = Hibernate.CHARACTER; else if (value instanceof Double) type = Hibernate.DOUBLE;
        return type;
    }

    public void insertOrUpdate(Object pojo) throws DAOException {
        getHibernateTemplate().saveOrUpdate(pojo);
    }

    /**
	 * <pre>
	 * ��ɾ�������ƶ��ı��������ɾ��һ����¼
	 * �˷�����д��Ŀ����Ϊ�˽�����ӱ?ɾ���ּ�¼����ΪHibernate�ṩ�ķ����Ƚ��鷳��
	 * ���Բ�����һ�����Ǻܺõķ�������ɣ��ر��ǶԸ��ӹ�ϵ�ж���ӱ�������
	 * ����и�õ�ʵ�ֻ�ӭɾ��˷���
	 * �˷��������ų���ɾ��ʹ��
	 * </pre>
	 * @param tableName
	 * @param id
	 * @throws Exception
	 */
    public void delete(String tableName, long id) throws Exception {
        String sql = "delete from " + tableName + " where id = " + id;
        Connection con = null;
        con = getHibernateTemplate().getSessionFactory().openSession().connection();
        con.createStatement().execute(sql);
    }

    public void cleanSession(Object o) {
    }

    public void cleanSession() {
    }

    public List find(ListQury filter) throws DAOException {
        return null;
    }

    public List findBySql(ListQury filter) throws DAOException {
        return null;
    }

    public void insertOrUpdateObjects(List objects) throws DAOException {
    }
}
