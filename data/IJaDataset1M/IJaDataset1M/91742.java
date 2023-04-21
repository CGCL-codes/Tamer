package com.yubuild.coreman.business.impl;

import java.util.List;
import com.yubuild.coreman.business.ActivityLogManager;
import com.yubuild.coreman.common.SearchData;
import com.yubuild.coreman.dao.ActivityLogDAO;
import com.yubuild.coreman.data.ActivityLog;
import com.yubuild.coreman.data.searchable.ActivityLogSearchable;

public class ActivityLogManagerImpl extends BaseManager implements ActivityLogManager {

    private ActivityLogDAO dao;

    public void setActivityLogDAO(ActivityLogDAO dao) {
        this.dao = dao;
    }

    public ActivityLog getActivityLog(Long activityLogId) {
        return dao.getActivityLog(activityLogId);
    }

    public void saveActivityLog(ActivityLog activityLog) {
        dao.saveActivityLog(activityLog);
    }

    public void removeActivityLog(Long activityLogId) {
        dao.removeActivityLog(activityLogId);
    }

    public SearchData getActivityLogs(ActivityLogSearchable activityLog, SearchData searchData) {
        searchData.setResults((List) dao.getActivityLogs(activityLog, searchData));
        searchData.setResultsCount((Integer) dao.getActivityLogCount(activityLog));
        return searchData;
    }

    public SearchData getActivityLogs(ActivityLogSearchable activityLog) {
        SearchData searchData = new SearchData();
        searchData.setResults((List) dao.getActivityLogs(activityLog));
        searchData.setResultsCount((Integer) dao.getActivityLogCount(activityLog));
        return searchData;
    }
}
