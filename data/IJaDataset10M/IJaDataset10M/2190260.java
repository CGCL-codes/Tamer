package com.matrixbi.adans.client.report;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.matrixbi.adans.client.mvc.CustomDispatcher;
import com.matrixbi.adans.client.report.mvc.ReportController;
import com.matrixbi.adans.client.report.mvc.ReportEvents;
import com.matrixbi.adans.client.report.mvc.ReportView;
import com.matrixbi.adans.client.report.widget.DimensionController;
import com.matrixbi.adans.client.report.widget.DimensionSelector;
import com.matrixbi.adans.client.report.widget.FilterBox;
import com.matrixbi.adans.client.report.widget.FilterBoxController;
import com.matrixbi.adans.client.report.widget.FilterController;
import com.matrixbi.adans.client.report.widget.FilterSelector;
import com.matrixbi.adans.client.report.widget.ReportChartController;
import com.matrixbi.adans.client.report.widget.ReportDataController;
import com.matrixbi.adans.client.report.widget.HeaderController;
import com.matrixbi.adans.client.report.widget.MeasureController;
import com.matrixbi.adans.client.report.widget.MeasureSelector;
import com.matrixbi.adans.client.report.widget.TimeController;
import com.matrixbi.adans.client.report.widget.TimeSelector;
import com.matrixbi.adans.ocore.client.report.Report;

public class ReportPanelController extends ReportController {

    private String id;

    private HeaderController header;

    private LoaderController loader;

    private MeasureController measure;

    private DimensionController dimension;

    private FilterController filter;

    private FilterBoxController filterBox;

    protected ReportDataController data;

    protected ReportChartController chart;

    protected TimeController time;

    @Override
    public ReportPanel getView() {
        return (ReportPanel) super.getView();
    }

    public HeaderController getHeader() {
        if (header == null) {
            ReportView headerView = ReportFactoryView.getHeaderView(getView().getViewType());
            header = new HeaderController(getDispatcher(), headerView);
        }
        return header;
    }

    public LoaderController getLoader() {
        if (loader == null) {
            loader = new LoaderController(getDispatcher());
        }
        return loader;
    }

    public MeasureController getMeasure() {
        if (measure == null) {
            MeasureSelector measureSelector = new MeasureSelector();
            measure = new MeasureController(getDispatcher(), measureSelector);
        }
        return measure;
    }

    public DimensionController getDimension() {
        if (dimension == null) {
            DimensionSelector dimensionSelector = new DimensionSelector();
            dimension = new DimensionController(getDispatcher(), dimensionSelector);
        }
        return dimension;
    }

    public FilterController getFilter() {
        if (filter == null) {
            FilterSelector filterSelector = new FilterSelector();
            filter = new FilterController(getDispatcher(), filterSelector);
        }
        return filter;
    }

    public FilterBoxController getFilterBox() {
        if (filterBox == null) {
            FilterBox filterBoxView = new FilterBox();
            filterBox = new FilterBoxController(getDispatcher(), filterBoxView);
        }
        return filterBox;
    }

    protected ReportDataController getData() {
        if (data == null) {
            ReportView dataView = ReportFactoryView.getGridView(getView().getViewType());
            data = new ReportDataController(getDispatcher(), dataView);
        }
        return data;
    }

    public ReportChartController getChart() {
        if (chart == null) {
            ReportView chartView = ReportFactoryView.getChartView(getView().getViewType());
            chart = new ReportChartController(getDispatcher(), chartView);
        }
        return chart;
    }

    public TimeController getTime() {
        if (time == null) {
            TimeSelector timeView = new TimeSelector();
            time = new TimeController(getDispatcher(), timeView);
        }
        return time;
    }

    public ReportPanelController(CustomDispatcher dispatcher, ReportView view, String id) {
        super(dispatcher, view);
        this.id = id;
        initControllers();
        launchInitEvent();
    }

    private void initControllers() {
        getLoader();
        getHeader();
        getMeasure();
        getDimension();
        getFilter();
        getFilterBox();
        getData();
        getChart();
        getTime();
    }

    @Override
    public void handleEvent(AppEvent event) {
        if (event.getType() == ReportEvents.Init) {
            forwardToView(getView(), event);
        }
        if (event.getType() == ReportEvents.LoadReport) {
            forwardToView(getView(), event);
        }
        if (event.getType() == ReportEvents.ReportLoaded) {
            setReport((Report) event.getData());
            forwardToView(getView(), event);
        }
        if (event.getType() == ReportEvents.ShowMeasureSelector) {
            forwardToView(getView(), event);
        }
        if (event.getType() == ReportEvents.ShowDimensionSelector) {
            forwardToView(getView(), event);
        }
        if (event.getType() == ReportEvents.OnVisualizationClick) {
            forwardToView(getView(), event);
        }
    }

    private void launchInitEvent() {
        AppEvent init = new AppEvent(ReportEvents.Init, this.id);
        getDispatcher().forwardEvent(init);
    }
}
