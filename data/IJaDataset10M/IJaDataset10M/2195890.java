package cn.tm05.dao;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import cn.tm05.pojo.UserInfo;

/**
 * 操作用户对象类
 * @author TM05
 *
 */
public class DaoUserInfo {

    /**
	 * 验证登陆用户是否存在
	 * @param ui：登陆用户对象
	 * @return：用户对象：null：不存在
	 */
    public static UserInfo userValidate(UserInfo ui) {
        org.hibernate.Session session = HBUtils.getSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        tx.begin();
        List userList = session.createCriteria(UserInfo.class).add(Restrictions.eq("user_name", ui.getUser_name())).add(Restrictions.eq("user_pwd", ui.getUser_pwd())).add(Restrictions.eq("popedom_level", ui.getPopedom_level())).list();
        tx.commit();
        session.close();
        System.out.println(userList.size());
        if (userList.size() != 0) {
            UserInfo user = (UserInfo) userList.get(0);
            return user;
        } else {
            return null;
        }
    }
}
