package cn.myapps.core.task.dao;

import java.util.Collection;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.HibernateBaseDAO;

public class HibernateTaskDAO extends HibernateBaseDAO implements TaskDAO {

    public HibernateTaskDAO(String voClassName) {
        super(voClassName);
    }

    public Collection getTaskByModule(String application, String module) throws Exception {
        String hql = "FROM " + _voClazzName + " vo WHERE vo.module.id='" + module + "'";
        ParamsTable params = new ParamsTable();
        params.setParameter("application", application);
        return getDatas(hql, params);
    }

    public Collection query(String application) throws Exception {
        String hql = "FROM " + _voClazzName + " vo ORDER BY vo.firstTime";
        ParamsTable params = new ParamsTable();
        params.setParameter("application", application);
        return getDatas(hql, params);
    }
}
