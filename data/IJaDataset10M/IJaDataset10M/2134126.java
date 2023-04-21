package com.openthinks.woms.report.service;

import java.util.Collection;
import com.openthinks.woms.report.SizeReport;
import com.openthinks.woms.report.SizeReportItem;
import com.openthinks.woms.report.dao.SizeReportDao;

public class SizeReportServiceImpl implements SizeReportService {

    private SizeReportDao sizeReportDao;

    public void setSizeReportDao(SizeReportDao sizeReportDao) {
        this.sizeReportDao = sizeReportDao;
    }

    @Override
    public SizeReport analyzeSizeByCandidate(String accountId, String candidate) throws Exception {
        SizeReport report = new SizeReport();
        Collection<SizeReportItem> items = sizeReportDao.countSizeByCandidate(accountId, candidate);
        report.setSizeReportItems(items);
        return report;
    }
}
