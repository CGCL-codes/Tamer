package com.kwoksys.biz.reports;

import com.kwoksys.biz.reports.dto.SoftwareUsage;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.exception.DatabaseException;
import java.util.List;

/**
 * ReportService class
 */
public interface ReportService {

    public List<SoftwareUsage> getSoftwareUsage(QueryBits query) throws DatabaseException;

    public int getSoftwareUsageCount(QueryBits query) throws DatabaseException;
}
