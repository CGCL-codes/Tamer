package com.kongur.network.erp.dao.uc.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kongur.network.erp.common.Paginable;
import com.kongur.network.erp.dao.BaseDaoiBatis;
import com.kongur.network.erp.dao.uc.UserDao;
import com.kongur.network.erp.domain.uc.Report;
import com.kongur.network.erp.domain.uc.UserDO;
import com.kongur.network.erp.domain.uc.query.ReportQuery;

/**
 * @author gaojf
 * @version $Id: UserDaoImpl.java,v 0.1 2011-12-19 ����03:57:36 gaojf Exp $
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoiBatis<UserDO> implements UserDao {

    /**
     * ���id��û�Ա��Ϣ
     * @param id
     * @return
     */
    public UserDO getUserById(Long id) {
        return (UserDO) this.getSqlMapClientTemplate().queryForObject("User.uc-user-queryById", id);
    }

    /**
     * ���������û�Ա��Ϣ
     * @param user
     * @return
     */
    public UserDO getObjByCondition(UserDO user) {
        return (UserDO) this.getSqlMapClientTemplate().queryForObject("User.uc-user-queryByCondition", user);
    }

    /**
     * ������Ա��Ϣ
     * @param user
     * @return
     */
    public Long insertUser(UserDO user) {
        return (Long) this.getSqlMapClientTemplate().insert("User.uc-user-insert", user);
    }

    /**
     * �޸Ļ�Ա��Ϣ
     * @param user
     * @return
     */
    public int updateUser(UserDO user) {
        return (Integer) this.getSqlMapClientTemplate().update("User.uc-user-update", user);
    }

    /**
     * ���������û�Ա��Ϣ�б�
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserDO> getListByCondition(UserDO user) {
        return this.getSqlMapClientTemplate().queryForList("User.uc-user-queryByCondition", user);
    }

    /**
     * ��ѯ��Ա��Ϣ - ��ҳ
     * @param page
     * @return
     */
    public Paginable<UserDO> getPaginatedUser(Paginable<UserDO> page) {
        return super.getPagination(page, "User.uc-user-page-count", "User.uc-user-page-query");
    }

    /**
     * ��Ա��ݱ��� - �ȼ�
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> userLevelReport(ReportQuery query) {
        return this.getSqlMapClientTemplate().queryForList("User.uc-user-level-report", query);
    }

    /**
     * ��Ա��ݱ��� - �Ա�
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> userSexReport(ReportQuery query) {
        return this.getSqlMapClientTemplate().queryForList("User.uc-user-sex-report", query);
    }

    /**
     * ��Ա��ݱ��� - ����
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> userAreaReport(ReportQuery query) {
        if (query.getProvince() != null) {
            if (query.getCity() != null) {
                return this.getSqlMapClientTemplate().queryForList("User.uc-user-area-district-report", query);
            }
            return this.getSqlMapClientTemplate().queryForList("User.uc-user-area-city-report", query);
        }
        return this.getSqlMapClientTemplate().queryForList("User.uc-user-area-province-report", query);
    }

    /**
     * ��Ա��ݱ��� - ����
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> userAgeReport(ReportQuery query) {
        return this.getSqlMapClientTemplate().queryForList("User.uc-user-age-report", query);
    }
}
