package com.fddtool.ui.view.report;

import java.io.Serializable;
import java.util.Date;
import com.fddtool.pd.common.AppEntry;
import com.fddtool.pd.report.ReportTypeDescription;
import com.fddtool.resource.IMessageProvider;
import com.fddtool.resource.MessageProviderImpl;
import com.fddtool.util.UserLocalizer;

/**
 * This class represents the report in the user interface.
 * 
 * @author Serguei Khramtchenko
 */
public class UIReport extends UIReportParams implements Serializable {

    /**
     * Initial serialization ID
     */
    private static final long serialVersionUID = -9013409820594930262L;

    /**
     * Provides translated messages from resource.
     */
    private static final IMessageProvider MESSAGE_PROVIDER = MessageProviderImpl.getProvider();

    /**
     * Creates a new instance of this class and populates it with parameters.
     * 
     * @param reportType
     *            ReportTypeDescription specifies the report type to be
     *            generated.
     * @param entry
     *            AppEntry that the report should be generated for.
     * @param cutOffDate
     *            Date of data collection for the report. If present, the report
     *            should be generated as of that date. If absent - the latest
     *            report should be generated.
     */
    public UIReport(ReportTypeDescription reportType, AppEntry entry, Date cutOffDate) {
        super(reportType, entry, cutOffDate);
    }

    /**
     * Returns the name of the report how it should be displayed in user
     * interface.
     * 
     * @return String containing report name.
     */
    public String getName() {
        return MESSAGE_PROVIDER.getMessage(reportType.getNameKey());
    }

    /**
     * Returns name of the entry (project or project group) that the report is
     * to be generated for.
     * 
     * @return String with entry name.
     */
    public String getEntryName() {
        return entry.getName();
    }

    /**
     * Returns name of the view that renders the report in HTML format. The name
     * of the view is valid only if the report supports HTML rendering.
     * 
     * @return String name of the view that may be used in URL.
     * 
     * @see #isHtmlSupported()
     */
    public String getView() {
        return getReportId() + ".jsfo";
    }

    /**
     * Indicates if the report may be rendered as HTML.
     * 
     * @return boolean true if the report can be rendered as HTML.
     */
    public boolean isSupportsHTML() {
        return reportType.isSupportsHTML();
    }

    /**
     * Indicates if the report may be rendered as PDF.
     * 
     * @return boolean true if the report can be rendered as PDF.
     */
    public boolean isSupportsPDF() {
        return reportType.isSupportsPDF();
    }

    /**
     * Returns the date as of which the report data was collected. This date is
     * intended to be displayed in UI.
     * 
     * @return String representing the date.
     */
    public String getFormattedCutOffDate() {
        return UserLocalizer.formatLongDate(cutOffDate);
    }
}
