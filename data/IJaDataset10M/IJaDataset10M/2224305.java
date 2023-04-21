package net.sf.logdistiller.reports;

import java.util.*;
import net.sf.logdistiller.ReportFormat;
import net.sf.logdistiller.ReportFormats;

/**
 * Declaration of base report formats that are shipped with <b>LogDistiller</b>.
 *
 * @since 0.8
 */
public class BaseReportFormats extends ReportFormats {

    public static final ReportFormat TEXT = new TextReport();

    public static final ReportFormat XML = new XmlReport();

    private static final ReportFormat[] REPORT_FORMATS = { TEXT, XML };

    private static final List<ReportFormat> REPORT_FORMATS_LIST = Collections.unmodifiableList(Arrays.asList(REPORT_FORMATS));

    public List<ReportFormat> defineReportFormats() {
        return REPORT_FORMATS_LIST;
    }
}
