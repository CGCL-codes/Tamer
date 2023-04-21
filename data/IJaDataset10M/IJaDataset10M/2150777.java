package com.herestudio.dao.impl;

import java.util.Date;
import java.util.List;
import org.springframework.dao.DataAccessException;
import com.herestudio.dao.IUserDao;
import com.herestudio.pojo.HahereUser;
import com.herestudio.pojo.UserBasicInfo;

public class UserDao extends DaoBasicImpl implements IUserDao {

    public UserBasicInfo selectUserByEmail(String email) {
        String[] param = new String[] { "email" };
        String[] value = new String[] { email };
        return (UserBasicInfo) uniqueReturnList(getHibernateTemplate().findByNamedQueryAndNamedParam("userInfo_GetByEmail", param, value));
    }

    /**
	 * example user HibernateTemplate
	 * 
	 * @param begin_time
	 * @param end_time
	 * @return
	 */
    public List selectUserByCreateTime(Date begin_time, Date end_time) {
        String[] param = new String[] { "begin_time", "end_time" };
        Object[] value = new Object[] { begin_time, end_time };
        String sql = "from RegisterUser _user where _user.createTime between  :begin_time and  :end_time ";
        return (getHibernateTemplate().findByNamedParam(sql, param, value));
    }

    /**
	 * 
	 */
    public HahereUser validateLoginById(long user_id, String md5_pass) throws DataAccessException {
        HahereUser user = (HahereUser) getHibernateTemplate().get(HahereUser.class, user_id);
        if (user != null) {
            if (user.getUserInfo() != null) {
                if (md5_pass.equals(user.getUserInfo().getMd5Pass())) {
                    return user;
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("pass not equal");
                    }
                    return null;
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("userinfo null");
                }
                return null;
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("user null");
            }
            return null;
        }
    }

    public HahereUser validateLoginByEmail(String email, String md5_pass) throws DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("validateLoginByEmail:email=" + email + ",md5_pass=" + md5_pass);
        }
        UserBasicInfo user_basic_info = selectUserByEmail(email);
        HahereUser user = null;
        if (user_basic_info == null) {
            return null;
        } else {
            if (md5_pass.equals(user_basic_info.getMd5Pass())) {
                user = user_basic_info.getUser();
                return user;
            } else {
                return null;
            }
        }
    }
}
