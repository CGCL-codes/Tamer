package com.matrixbi.adansi.client.report;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Widget;
import com.matrixbi.adansi.client.report.mvc.ReportEvents;
import com.matrixbi.adansi.client.report.mvc.ReportView;
import com.matrixbi.adansi.client.report.widget.FormControls;
import com.matrixbi.adansi.ocore.client.report.ReportType;

public class ReportPanel extends ReportView {

    protected ContentPanel mainPanel;

    protected LayoutContainer infoPanel;

    protected LayoutContainer filterPanel;

    protected LayoutContainer dataPanel;

    protected LayoutContainer chartPanel;

    public ReportPanel() {
        super();
    }

    public ContentPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new ContentPanel();
            mainPanel.setFrame(true);
            mainPanel.setLayout(new RowLayout());
            mainPanel.setAnimCollapse(false);
            mainPanel.setHeaderVisible(false);
            mainPanel.setBorders(false);
            mainPanel.setHeight(1);
            mainPanel.setScrollMode(Scroll.AUTOY);
            mainPanel.setAutoWidth(true);
        }
        return mainPanel;
    }

    public LayoutContainer getInfoPanel() {
        if (infoPanel == null) {
            infoPanel = new LayoutContainer(new FitLayout());
            infoPanel.add(getController().getHeader().getView().asWidget());
        }
        return infoPanel;
    }

    public LayoutContainer getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new LayoutContainer();
            filterPanel.add(getController().getFilterBox().getView().asWidget());
        }
        return filterPanel;
    }

    public LayoutContainer getDataPanel() {
        if (dataPanel == null) {
            dataPanel = new LayoutContainer();
            dataPanel.add(getController().getData().getView().asWidget());
        }
        return dataPanel;
    }

    public LayoutContainer getChartPanel() {
        if (chartPanel == null) {
            chartPanel = new LayoutContainer();
            chartPanel.add(getController().getChart().getView().asWidget());
        }
        return chartPanel;
    }

    public ReportPanelController getController() {
        return (ReportPanelController) this.controller;
    }

    @Override
    protected void handleEvent(AppEvent event) {
        EventType type = event.getType();
        if (type == ReportEvents.Init) {
            getMainPanel().add(getInfoPanel(), FormControls.getBasicRowData());
            getMainPanel().add(getFilterPanel(), FormControls.getBasicRowData());
            getMainPanel().add(getDataPanel(), FormControls.getBasicRowData());
            getMainPanel().add(getChartPanel(), FormControls.getBasicRowData());
        }
        if (type == ReportEvents.LoadReport) {
        }
        if (event.getType() == ReportEvents.ReportLoaded) {
            getMainPanel().unmask();
        }
        if (event.getType() == ReportEvents.ShowMeasureSelector) {
        }
    }

    protected ReportType getViewType() {
        return null;
    }

    @Override
    public Widget asWidget() {
        return getMainPanel();
    }
}
