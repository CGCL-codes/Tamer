package com.cci.bmc.service.impl;

import com.cci.bmc.action.Page;
import com.cci.bmc.dao.LogEntryDao;
import com.cci.bmc.dao.ReportDao;
import com.cci.bmc.domain.LogEntry;
import com.cci.bmc.report.ScopeUsageReport;
import com.cci.bmc.service.ReportingService;

public class ReportingServiceImpl implements ReportingService {

    private LogEntryDao logEntryDao;

    private ReportDao reportDao;

    public void setReportDao(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public void setLogEntryDao(LogEntryDao logEntryDao) {
        this.logEntryDao = logEntryDao;
    }

    public ScopeUsageReport generateScopeUsageReport() {
        return reportDao.generateScopeUsageReport();
    }

    public Long getCountOfLogEntries(String username, String accountNumber) {
        return logEntryDao.count(username, accountNumber);
    }

    public Page<LogEntry> getLogEntries(String username, String accountNumber, Integer pageSize, Integer page) {
        return logEntryDao.get(username, accountNumber, pageSize, page);
    }
}
