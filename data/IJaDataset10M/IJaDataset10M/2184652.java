package cn.myapps.core.report.tablecolumn.dao;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;

public interface TableColumnDAO extends IDesignTimeDAO {

    public DataPackage getFieldsByReportConfigAndType(String reportconfigid, String type, String application) throws Exception;
}
