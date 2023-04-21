package cn.myapps.core.report.wfdashboard.dao;

import java.util.Collection;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.report.basereport.dao.ReportDAO;

public interface WFDashBoardDAO extends ReportDAO {

    public Collection getSumWf(String application, String domainid) throws Exception;

    public Collection getSumStableLabel(String application, String domainid, String flowid) throws Exception;

    public DataPackage getSumRole(String application, String domainid, String flowid, int curPage) throws Exception;

    public Collection getSumTime(String application, String domainid, String flowid) throws Exception;
}
