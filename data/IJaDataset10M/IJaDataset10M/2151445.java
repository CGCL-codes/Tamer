package org.seqtagutils.logging;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.seqtagutils.logging.dao.CActivityLogItem;
import org.seqtagutils.logging.dao.CErrorLogItem;
import org.seqtagutils.logging.dao.CLogItem;
import org.seqtagutils.logging.dao.ILoggingDao;
import org.seqtagutils.util.acegi.ILoginListener;
import org.seqtagutils.util.logging.IExceptionListener;

public interface ILoggingService extends ILoginListener, IExceptionListener {

    CLogItem getLogItem();

    CLogReport getLogReport(String user_id);

    CLogReport getLogReport(CLogReport.Params params);

    CLogItem logUrl(HttpServletRequest request);

    void logUrl(CLogItem logitem);

    void logActivity(CActivityLogItem.IActivity activity);

    List<CErrorLogItem> getErrors(Date startdate, Date enddate);

    ILoggingDao getDao();
}
