package org.seqtagutils.logging.dao;

import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.seqtagutils.logging.CLogReport;

@Transactional(readOnly = false)
public interface ILoggingDao {

    void addLoginLogItem(CLoginLogItem item);

    void addActivityLogItem(CActivityLogItem item);

    void addChangeLogItem(CChangeLogItem item);

    List<CLogItem> getLog(CLogReport.Params params);

    void addLogItem(CLogItem logitem);

    List<CErrorLogItem> getErrors(Date startdate, Date enddate);

    CErrorLogItem getErrorLogItem(int id);

    void addErrorLogItem(CErrorLogItem item);
}

;
