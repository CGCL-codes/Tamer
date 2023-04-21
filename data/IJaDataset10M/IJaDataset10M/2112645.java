package tuxiazi.dao.impl;

import halo.dao.query.BaseDao;
import halo.util.NumberUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tuxiazi.bean.User;
import tuxiazi.bean.Userid;
import tuxiazi.dao.UserDao;

@Component("userDao")
public class UserDaoImpl extends BaseDao<User> implements UserDao {

    @Autowired
    private UseridDaoImpl useridDao;

    @Override
    public Class<User> getClazz() {
        return User.class;
    }

    public int addPi_num(long userid, int add) {
        return this.updateBySQL("pic_num=pic_num+?", "userid=?", new Object[] { userid, add });
    }

    @Override
    public Object save(User t) {
        long userid = NumberUtil.getLong(this.useridDao.save(new Userid()));
        t.setUserid(userid);
        super.save(t);
        return userid;
    }

    public Map<Long, User> getMapInId(List<Long> idList) {
        List<User> list = this.getListInId(idList);
        Map<Long, User> map = new HashMap<Long, User>();
        for (User o : list) {
            map.put(o.getUserid(), o);
        }
        return map;
    }

    public List<User> getListInId(List<Long> idList) {
        return this.getListInField("userid", idList);
    }
}
