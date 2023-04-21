package net.sf.webwarp.reports;

import java.util.Collection;
import java.util.List;

public interface ReportProvider {

    public Report getReport(Integer reportID);

    public List<Report> getReports();

    public List<Report> getReports(Integer reportGroupID);

    public ReportGroup getReportGroup(Integer reportGroupID);

    public List<ReportGroup> getReportGroups();

    public List<ReportGroup> getReportGroups(Collection<String> roles);

    public void installReports(Collection<ReportGroup> reportGroups);
}
