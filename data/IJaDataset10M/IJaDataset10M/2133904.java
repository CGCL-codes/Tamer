package idv.takeshi.persistence.dao.mybatis;

import java.util.List;
import idv.takeshi.model.Role;
import idv.takeshi.persistence.dao.RoleDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * 
 * @author takeshi.miao
 *
 */
public class RoleDaoImpl extends SqlSessionDaoSupport implements RoleDao {

    private static final String MAPPER_NAMESPACE = "idv.takeshi.persistence.dao.RoleDao.";

    @SuppressWarnings("unchecked")
    @Override
    public List<Role> getAll() {
        return this.getSqlSession().selectList(MAPPER_NAMESPACE + "getAll");
    }
}
