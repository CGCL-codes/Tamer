package com.kxproweb.kxprowebgwt.client.view.chart;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.kxproweb.kxprowebgwt.client.resources.Resources;

public class ChartViewImpl extends Composite implements ChartView {

    private static ChartViewImplUiBinder uiBinder = GWT.create(ChartViewImplUiBinder.class);

    interface ChartViewImplUiBinder extends UiBinder<Widget, ChartViewImpl> {
    }

    @UiField
    LayoutContainer containerOfCharts;

    @UiField
    LayoutContainer containerOfCharts2;

    @UiField
    Resources res;

    Presenter listerner;

    SimplePanel panel = new SimplePanel();

    public ChartViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        containerOfCharts.setId("containerOfCharts");
        res.style().ensureInjected();
        float[] f = new float[] { 1, (float) 1.5, (float) 2.8, (float) 3.6, (float) 3.9, (float) 4.2, (float) 5.2, (float) 5.2, (float) 6.2 };
        JsArrayNumber datas = initArray(f);
        chart2();
        Widget chart = RootPanel.get("container").asWidget();
        containerOfCharts.add(chart);
        chart("idOfChart", "container2", "400px", "400px", "Title Scatter plot with regression line", "line", "Regression Line", "scatter", "Observations", datas);
        Widget chart2 = RootPanel.get("container2").asWidget();
        containerOfCharts2.add(chart2);
    }

    public static native void chart(String id, String parentNode, String width, String height, String title, String typeSeries, String nameSeries, String type, String observation, JsArrayNumber datas);

    public static native void chart2();

    @Override
    public void setPresenter(Presenter listerner) {
        this.listerner = listerner;
    }

    public static native JsArrayNumber createNew();

    public static JsArrayNumber initArray(float[] f) {
        JsArrayNumber a = createNew();
        for (float fv : f) a.push(fv);
        return a;
    }
}
