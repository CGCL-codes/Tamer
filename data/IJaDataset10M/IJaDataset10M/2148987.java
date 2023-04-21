package com.hs.framework.pool.hibernate;

import com.hs.framework.db.hibernate.HibernateUtil;
import com.hs.framework.pool.PoolException;

/**
 * HibernateUtil�ش�����
 * 
 * @author Ansun
 * 
 */
public class HibernatePoolProxy {

    private static HibernatePool hibernatePool = null;

    /**
	 * ��HibernatePool�л��һ��HibernateUtil
	 * 
	 * @return
	 * @throws PoolException??
	 * 
	 */
    public static HibernateUtil getHibernateDAO() throws PoolException {
        HibernateUtil result = null;
        if (hibernatePool == null) {
            getHibernatePool();
        }
        result = (HibernateUtil) hibernatePool.get();
        return result;
    }

    /**
	 * ��һ��HibernateUtil�Żص�HibernateUtil��
	 * 
	 * @param hibernateDAO
	 * @throws PoolException??
	 * 
	 */
    public static void putHibernateDAO(HibernateUtil hibernateDAO) throws PoolException {
        if (hibernatePool == null) {
            getHibernatePool();
        }
        hibernatePool.put(hibernateDAO);
    }

    /**
	 * ���HibernatePoolʵ��
	 * 
	 * @throws PoolException??
	 * 
	 */
    private static void getHibernatePool() throws PoolException {
        hibernatePool = HibernatePool.getInstance(2, 5);
    }

    public static int getFreeHibernateDAO() {
        return hibernatePool.getFreeObjectSize();
    }

    public static int getHibernateDAOSize() {
        return hibernatePool.getHibernateDAOSize();
    }
}
