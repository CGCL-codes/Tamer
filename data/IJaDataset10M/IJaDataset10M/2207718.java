package edu.mobbuzz.daf.dao.ibatis;

import java.util.HashMap;
import java.util.Map;
import edu.mobbuzz.daf.bean.Administrator;
import edu.mobbuzz.daf.bean.User;
import edu.mobbuzz.daf.dao.IAdministratorDao;

public class AdministratorDaoImpl extends BaseDaoImpl implements IAdministratorDao {

    @Override
    protected String getNamespace() {
        return "Administrator";
    }

    public Administrator findUserId(String username, String password) {
        try {
            Map map = new HashMap();
            map.put("username", username);
            map.put("password", password);
            return (Administrator) getSqlMapClientTemplate().queryForObject(getNamespace() + ".findUserId", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
