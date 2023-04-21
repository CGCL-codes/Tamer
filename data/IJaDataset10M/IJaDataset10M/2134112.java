package net.sourceforge.jabm.report;

import java.util.Map;
import net.sourceforge.jabm.event.SimEvent;

public abstract class AbstractReport implements Report {

    protected ReportVariables reportVariables;

    public AbstractReport(ReportVariables reportVariables) {
        super();
        this.reportVariables = reportVariables;
    }

    public AbstractReport() {
        super();
    }

    @Override
    public Map<Object, Number> getVariableBindings() {
        return reportVariables.getVariableBindings();
    }

    public ReportVariables getReportVariables() {
        return reportVariables;
    }

    /**
	 * Configure the variables that will be updated by this report.
	 * 
	 * @see ReportVariables
	 * @param reportVariables
	 */
    public void setReportVariables(ReportVariables reportVariables) {
        this.reportVariables = reportVariables;
    }

    @Override
    public void eventOccurred(SimEvent event) {
        reportVariables.eventOccurred(event);
    }
}
