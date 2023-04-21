package org.appspy.admin.client.reports.form;

import java.io.Serializable;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class DashboardParamBindingForm implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected Long mId;

    protected DashboardReportForm mDashboardReport;

    protected ReportParamForm mDashboardParam;

    protected ReportParamForm mReportParam;

    /**
	 * @return the dashboardParamBindingId
	 */
    public Long getId() {
        return mId;
    }

    /**
	 * @param dashboardParamBindingId the dashboardParamBindingId to set
	 */
    public void setId(Long dashboardParamBindingId) {
        this.mId = dashboardParamBindingId;
    }

    /**
	 * @return the dashboardReport
	 */
    public DashboardReportForm getDashboardReport() {
        return mDashboardReport;
    }

    /**
	 * @param dashboardReport the dashboardReport to set
	 */
    public void setDashboardReport(DashboardReportForm dashboardReport) {
        this.mDashboardReport = dashboardReport;
    }

    /**
	 * @return the dashboardParam
	 */
    public ReportParamForm getDashboardParam() {
        return mDashboardParam;
    }

    /**
	 * @param dashboardParam the dashboardParam to set
	 */
    public void setDashboardParam(ReportParamForm dashboardParam) {
        this.mDashboardParam = dashboardParam;
    }

    /**
	 * @return the reportParam
	 */
    public ReportParamForm getReportParam() {
        return mReportParam;
    }

    /**
	 * @param reportParam the reportParam to set
	 */
    public void setReportParam(ReportParamForm reportParam) {
        this.mReportParam = reportParam;
    }
}
