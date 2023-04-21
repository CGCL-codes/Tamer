package net.sf.fallfair.view;

import net.sf.fallfair.menu.FairMenu;
import net.sf.fallfair.reportviewer.ReportException;
import net.sf.fallfair.reportviewer.ReportViewer;
import java.util.HashMap;
import java.util.Map;
import net.sf.fallfair.CRUD.SQLMap;
import net.sf.fallfair.CRUD.SQLMapFactory;
import net.sf.fallfair.context.FairContext;
import net.sf.fallfair.dictionary.Dictionary;
import net.sf.fallfair.dictionary.DictionarySingleton;
import net.sf.fallfair.reportviewer.SQLReportCompiler;

public class FairReportCommand<F extends FairView> implements FairCommand<F> {

    private final SQLMap sqlMap = new SQLMapFactory().getSQLMap();

    private final Dictionary dictionary = DictionarySingleton.getInstance();

    private final FairMenu menu;

    private final Object source;

    private final String reportTitle;

    private final String reportSource;

    private final String[] subReportSource;

    private final Map<String, Object> reportParams;

    private final AddFairViewInFocusCommand addViewCommand;

    public FairReportCommand(FairMenu menu, Object source, String reportTitle, String reportSource, String[] subReportSource) {
        this(menu, source, reportTitle, reportSource, subReportSource, new HashMap<String, Object>());
    }

    public FairReportCommand(FairMenu menu, Object source, String reportTitle, String reportSource, String[] subReportSource, Map<String, Object> reportParams) {
        super();
        this.menu = menu;
        this.source = source;
        this.reportTitle = reportTitle;
        this.reportSource = reportSource;
        this.subReportSource = null != subReportSource ? subReportSource.clone() : null;
        this.reportParams = reportParams;
        this.addViewCommand = new AddFairViewInFocusCommand(menu);
    }

    @Override
    public boolean execute(FairView fairView, FairContext context) {
        this.addViewCommand.execute(ReportViewer.class.getName(), this.dictionary.getMessage("reportViewer"));
        ReportViewer reportViewer = (ReportViewer) fairView.getFairMenu().getPanel(ReportViewer.class.getName());
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        reportParameters.put("REPORT_TITLE", reportTitle);
        reportParameters.put("YEAR", context.getYear());
        reportParameters.putAll(this.reportParams);
        return printReportInternal(reportViewer, reportParameters, fairView, context);
    }

    protected boolean printReportInternal(ReportViewer reportViewer, Map<String, Object> reportParams, FairView fairView, FairContext context) {
        try {
            reportViewer.setReport(new SQLReportCompiler(this.reportSource, this.subReportSource, reportParams, this.sqlMap).compile());
        } catch (ReportException ex) {
            fairView.updateStatus(new ExceptionStatusLabel(ex));
        }
        return true;
    }

    protected Object getSource() {
        return this.source;
    }

    protected String getReportTitle() {
        return this.reportTitle;
    }

    protected String getReportSource() {
        return this.reportSource;
    }

    protected String[] getSubReportSource() {
        return this.subReportSource;
    }

    protected Map<String, Object> getReportParams() {
        return this.reportParams;
    }
}
