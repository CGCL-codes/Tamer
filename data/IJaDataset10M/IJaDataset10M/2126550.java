package whf.framework.security.dao;

import java.util.List;
import whf.framework.dao.hibernate.HibernateHelper;
import whf.framework.dao.param.IParam;
import whf.framework.dao.param.LongParam;
import whf.framework.exception.FindException;
import whf.framework.security.entity.Role;
import whf.framework.security.entity.User;
import whf.framework.util.Utils;

/**
 * @author wanghaifeng
 * @email king@126.com
 * @modify 2006-05-31
 */
public class RoleDAOImp extends whf.framework.dao.DAOImp<Role> implements RoleDAO {

    public List<Role> findByUser(User user) throws FindException {
        List<IParam> params = Utils.newArrayList();
        params.add(new LongParam("user_id", user == null ? 0 : user.getId()));
        return HibernateHelper.getNamedQueryResult("find-role-by-user", params);
    }
}
