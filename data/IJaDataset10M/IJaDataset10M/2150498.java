package org.vardb.admin;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.vardb.IVardbService;
import org.vardb.admin.dao.CActivityLogItem;
import org.vardb.admin.dao.CBlastLogItem;
import org.vardb.admin.dao.CErrorLogItem;
import org.vardb.admin.dao.CLogItem;
import org.vardb.admin.dao.CLoginLogItem;
import org.vardb.admin.dao.ILoggingDao;
import org.vardb.search.CSearchResults;
import org.vardb.search.CSequenceSearchResults;
import org.vardb.util.CHibernateHelper;

public interface ILoggingService {

    public CLogItem getLogItem();

    public void contact(String name, String affiliation, String email, String purpose, String comments);

    public CLogReport getLogReport(CLogReport.CParams params);

    public void logUrl(CLogItem logitem);

    public void logLogin(String username, CLoginLogItem.Status status);

    public void logActivity(CActivityLogItem.Activity activity);

    public void logActivity(CActivityLogItem.Activity activity, String details);

    public void logSearch(String query);

    public void logSearch(String query, int numresults);

    public void logSearch(String query, String filter, int numresults);

    public void logBlast(CBlastLogItem item);

    public void sendEmail(String body);

    public void sendEmail(String subject, String body);

    public void sendEmail(String to, String subject, String body);

    public void sendEmail(String from, String to, String subject, String body);

    public CErrorLogItem handle(Exception e);

    public CErrorLogItem handle(Exception e, boolean email);

    public List<CErrorLogItem> getErrors(Date startdate, Date enddate);

    public CStatus getStatus();

    public IVardbService getVardbService();

    public void setVardbService(IVardbService vardbService);

    public ILoggingDao getDao();
}
