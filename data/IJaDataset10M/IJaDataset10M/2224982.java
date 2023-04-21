package cn.myapps.core.standardreport.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import cn.myapps.base.dao.DAOException;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;

public interface StandardReportDAO extends IRuntimeDAO {

    public Collection getSummaryReport(String sql) throws Exception;
}
