package tuxiazi.dao.impl;

import halo.dao.query.BaseDao;
import halo.util.NumberUtil;
import org.springframework.stereotype.Component;
import tuxiazi.bean.Api_user;
import tuxiazi.dao.Api_userDao;

@Component("api_userDao")
public class Api_userDaoImpl extends BaseDao<Api_user> implements Api_userDao {

    @Override
    public Class<Api_user> getClazz() {
        return Api_user.class;
    }

    @Override
    public Object save(Api_user t) {
        long id = NumberUtil.getLong(super.save(t));
        t.setOid(id);
        return id;
    }

    public Api_user getByUseridAndApi_type(long userid, int apiType) {
        return this.getObject("userid=? and api_type=?", new Object[] { userid, apiType });
    }
}
