package cn.myapps.core.user.ejb;

import java.util.Collection;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.user.dao.UserDefinedDAO;

public class UserDefinedProcessBean extends AbstractDesignTimeProcessBean<UserDefined> implements UserDefinedProcess {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1210735331986765651L;

    @Override
    protected IDesignTimeDAO<UserDefined> getDAO() throws Exception {
        return (UserDefinedDAO) DAOFactory.getDefaultDAO(UserDefined.class.getName());
    }

    public void doUserDefinedUpdate(ValueObject vo) throws Exception {
    }

    public Collection<UserDefined> doViewByApplication(String applicationId) throws Exception {
        return ((UserDefinedDAO) getDAO()).findByApplication(applicationId);
    }

    public int doViewCountByName(String name, String applicationid) throws Exception {
        return ((UserDefinedDAO) getDAO()).queryCountByName(name, applicationid);
    }

    public DataPackage<UserDefined> getDatapackage(String hql, ParamsTable params) throws Exception {
        return ((UserDefinedDAO) getDAO()).getDatapackage(hql, params);
    }
}
