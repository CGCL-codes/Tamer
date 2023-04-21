package com.laoer.bbscs.dao.hibernate;

import org.springframework.orm.hibernate3.support.*;
import com.laoer.bbscs.dao.SysNumStatDAO;
import com.laoer.bbscs.bean.SysNumStat;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import java.sql.SQLException;

/**
 * <p>Title: Tianyi BBS</p>
 *
 * <p>Description: BBSCS</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Laoer.com</p>
 *
 * @author Gong Tianyi
 * @version 7.0
 */
public class SysNumStatHibernateDAO extends HibernateDaoSupport implements SysNumStatDAO {

    private static final String LOAD_BY_RECDATE = "from SysNumStat where recDate = ?";

    private static final String LOADS_ALL = "from SysNumStat order by createTime desc";

    private static final String LOADS_ALL_COUNT = "select count(*) from SysNumStat";

    public SysNumStatHibernateDAO() {
        super();
    }

    /**
   *
   * @param sns SysNumStat
   * @return SysNumStat
   * @todo Implement this com.laoer.bbscs.dao.SysNumStatDAO method
   */
    public SysNumStat saveSysNumStat(SysNumStat sns) {
        this.getHibernateTemplate().saveOrUpdate(sns);
        return sns;
    }

    /**
   *
   * @param recDate String
   * @return SysNumStat
   * @todo Implement this com.laoer.bbscs.dao.SysNumStatDAO method
   */
    public SysNumStat findSysNumStatByRecDate(String recDate) {
        List l = this.getHibernateTemplate().find(LOAD_BY_RECDATE, recDate);
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return (SysNumStat) l.get(0);
        }
    }

    /**
   *
   * @return int
   * @todo Implement this com.laoer.bbscs.dao.SysNumStatDAO method
   */
    public long getSysNumStatNum() {
        List l = this.getHibernateTemplate().find(LOADS_ALL_COUNT);
        if (l == null || l.isEmpty()) {
            return 0;
        } else {
            return ((Long) l.get(0)).longValue();
        }
    }

    /**
   *
   * @param firstResult int
   * @param maxResults int
   * @return List
   * @todo Implement this com.laoer.bbscs.dao.SysNumStatDAO method
   */
    public List findSysNumStats(final int firstResult, final int maxResults) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session s) throws HibernateException, SQLException {
                Query query = s.createQuery(LOADS_ALL);
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResults);
                List list = query.list();
                return list;
            }
        });
    }
}
