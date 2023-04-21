package net.sourceforge.eclipsetrader.core.ui.export;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.sourceforge.eclipsetrader.core.ui.export.messages";

    private Messages() {
    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String CSVExport_Title;

    public static String CSVExport_Description;

    public static String CSVExport_JobName;

    public static String CSVExport_ErrorMessage;

    public static String CSVExport_HistoricalTask;

    public static String CSVExport_IntradayTask;

    public static String CSVExport_LastTask;

    public static String ExportSelectionPage_All;

    public static String ExportSelectionPage_Selected;

    public static String ExportSelectionPage_SelectLabel;

    public static String ExportSelectionPage_Historical;

    public static String ExportSelectionPage_intraday;

    public static String ExportSelectionPage_Last;

    public static String ExportSelectionPage_DestinationLabel;

    public static String ExportSelectionPage_FileLabel;

    public static String ExportSelectionPage_BrowseButton;

    public static String ExportSelectionPage_FileDialogTitle;
}
