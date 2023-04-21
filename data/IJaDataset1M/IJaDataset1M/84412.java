package com.hs.framework.db.jdbc.impl;

import java.util.Collection;
import com.hs.framework.BaseBean;
import com.hs.framework.db.DAOException;
import com.hs.framework.db.jdbc.BaseDAOJDBC;

/**
 * JDBC DAO��MySQLʵ��
 * @author Ansun
 *
 */
public abstract class BaseDAOJDBCMysql implements BaseDAOJDBC {

    public Collection loadList(BaseBean bean) throws DAOException {
        return null;
    }

    public BaseBean loadByPk(Object pk) throws DAOException {
        return null;
    }

    public void insert(BaseBean bean) throws DAOException {
    }

    public void updateByPk(BaseBean bean, Object pk) throws DAOException {
    }

    public void updateListByPk(BaseBean[] beans, Object[] pks) throws DAOException {
    }

    public void delByPk(Object pk) throws DAOException {
    }

    /**
	 * ��ò�ѯһ����ݵ�SQL
	 * @param bean
	 * @return
	 */
    public abstract String getLoadListSQL(BaseBean bean);

    /**
	 * ���ͨ�������ѯһ����¼��SQL
	 * @param pk
	 * @return
	 */
    public abstract String getLoadSQL(Object pk);

    /**
	 * ��ò���һ����ݵ�SQL
	 * @param pk
	 * @return
	 */
    public abstract String getInsertSQL(BaseBean bean);

    /**
	 * ���һ��ͨ��������¼�¼��SQL
	 * @param bean
	 * @param pk
	 * @return
	 */
    public abstract String getUpdateSQL(BaseBean bean, Object pk);

    /**
	 * ��ø���һ���¼��SQL
	 * @param beans
	 * @param pks
	 * @return
	 */
    public abstract String getUpdateSQL(BaseBean[] beans, Object[] pks);

    /**
	 * ���һ��ͨ������ɾ���¼��SQL
	 * @param pk
	 * @return
	 */
    public abstract String getDeleteSQL(Object pk);
}
