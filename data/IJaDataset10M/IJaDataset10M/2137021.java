package com.esri.gpt.control.webharvest.engine;

/**
 * Execution unit helper.
 */
class ExecutionUnitHelper {

    private ExecutionUnit unit;

    public ExecutionUnitHelper(ExecutionUnit unit) {
        this.unit = unit;
    }

    public ExecutionUnit getUnit() {
        return unit;
    }

    public void setReportBuilder(ReportBuilder rp) {
        if (unit != null) {
            unit.setAttribute(ReportBuilder.class.getCanonicalName(), rp);
        }
    }

    public ReportBuilder getReportBuilder() {
        return unit != null ? (ReportBuilder) unit.getAttribute(ReportBuilder.class.getCanonicalName()) : null;
    }

    public void setSourceUris(SourceUriArray sourceUris) {
        if (unit != null) {
            unit.setAttribute(SourceUriArray.class.getCanonicalName(), sourceUris);
        }
    }

    public SourceUriArray getSourceUris() {
        return unit != null ? (SourceUriArray) unit.getAttribute(SourceUriArray.class.getCanonicalName()) : null;
    }
}
