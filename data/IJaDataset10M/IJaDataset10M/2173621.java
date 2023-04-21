package cn.myapps.core.report.crossreport.definition.dao;

import cn.myapps.base.dao.HibernateBaseDAO;

public class HibernateCrossReportDAO extends HibernateBaseDAO implements CrossReportDAO {

    public HibernateCrossReportDAO(String voClassName) {
        super(voClassName);
    }
}
