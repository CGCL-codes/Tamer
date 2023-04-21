package org.monet.reportgenerator.client.event;

import org.monet.reportgenerator.client.base.utils.L;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;

public class ReportOpenEvent extends GwtEvent<ReportOpenEventHandler> {

    public static GwtEvent.Type<ReportOpenEventHandler> TYPE = new GwtEvent.Type<ReportOpenEventHandler>();

    private String reportId;

    public ReportOpenEvent(String reportId) {
        Log.trace(L.m(this, "ReportOpenEvent", reportId));
        this.reportId = reportId;
    }

    public String getReportId() {
        Log.trace(L.m(this, "getReportId"));
        return this.reportId;
    }

    @Override
    public GwtEvent.Type<ReportOpenEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ReportOpenEventHandler handler) {
        handler.onReportOpen(this);
    }
}
