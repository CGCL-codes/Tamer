package com.brekeke.report.history.timer;

import com.brekeke.report.history.dao.SystemOptionsDao;
import com.brekeke.report.history.entity.HistoryOptions;

public class DailyGenerateReportTrigger extends TimerTrigger {

    private SystemOptionsDao systemOptionsDao;

    private static final long serialVersionUID = 7519276435154490899L;

    public String returnReportTime() throws Exception {
        ;
        HistoryOptions historyOptions = systemOptionsDao.searchHistoryOptions();
        return getYYYYMMDDString(Integer.parseInt(historyOptions.getStartTime())) + " * * ?";
    }

    public void setSystemOptionsDao(SystemOptionsDao systemOptionsDao) {
        this.systemOptionsDao = systemOptionsDao;
    }
}
