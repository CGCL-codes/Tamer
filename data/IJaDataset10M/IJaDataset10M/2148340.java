package cn.myapps.core.report.reportconfig.dao;

import java.util.Collection;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.HibernateBaseDAO;

public class HibernateReportConfigDAO extends HibernateBaseDAO implements ReportConfigDAO {

    public HibernateReportConfigDAO(String voClassName) {
        super(voClassName);
    }

    public Collection getReportByModule(String moduleid, String application) throws Exception {
        String hql = "FROM " + _voClazzName + " vo WHERE vo.module.id='" + moduleid + "'";
        ParamsTable params = new ParamsTable();
        params.setParameter("application", application);
        return getDatas(hql, params);
    }
}
