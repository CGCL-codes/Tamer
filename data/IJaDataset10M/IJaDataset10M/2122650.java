package org.opennms.netmgt.dao;

import java.util.List;
import org.opennms.netmgt.config.databaseReports.Report;

/**
 * <p>DatabaseReportConfigDao interface.</p>
 */
public interface DatabaseReportConfigDao {

    /**
     * <p>getReports</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<Report> getReports();

    /**
     * <p>getOnlineReports</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<Report> getOnlineReports();

    /**
     * <p>getReportService</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getReportService(String id);

    /**
     * <p>getDisplayName</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getDisplayName(String id);
}
