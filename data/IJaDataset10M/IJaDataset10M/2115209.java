package org.timepedia.chronoscope.client.browser;

import java.util.ArrayList;
import java.util.Arrays;
import org.timepedia.chronoscope.client.Chart;
import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.Datasets;
import org.timepedia.chronoscope.client.XYPlot;
import org.timepedia.chronoscope.client.browser.json.GwtJsonDataset;
import org.timepedia.chronoscope.client.browser.json.JsonDatasetJSO;
import org.timepedia.chronoscope.client.canvas.View;
import org.timepedia.chronoscope.client.canvas.ViewReadyCallback;
import org.timepedia.chronoscope.client.data.tuple.Tuple2D;
import org.timepedia.chronoscope.client.gss.GssContext;
import org.timepedia.chronoscope.client.plot.DefaultXYPlot;
import org.timepedia.chronoscope.client.render.XYPlotRenderer;
import org.timepedia.chronoscope.client.util.ArgChecker;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

@ExportPackage("chronoscope")
public class ChartPanel extends Composite implements Exportable, ResizeHandler {

    public static native JsonDatasetJSO getJsonDataset(String varName);

    public static native JsArray<JsonDatasetJSO> getJsonDatasets(String varName);

    private boolean autoResize = false;

    private Dataset[] datasets;

    private String targetElementId;

    private boolean initialized = false;

    private DefaultXYPlot plot;

    private PlotPanel plotPanel;

    private int width = 400, height = 250, wheight, wwidth;

    public ChartPanel() {
        plotPanel = new PlotPanel();
        initWidget(plotPanel);
    }

    protected XYPlot createPlot(Dataset[] datasetArray) {
        plot = new DefaultXYPlot();
        plot.setDatasets(new Datasets<Tuple2D>(datasetArray));
        plot.setPlotRenderer(new XYPlotRenderer());
        return plot;
    }

    public void onDetach() {
        super.onDetach();
        dispose();
        plotPanel = null;
    }

    @Export
    public void detach() {
        dispose();
        this.removeFromParent();
        plotPanel = null;
    }

    private void dispose() {
        for (Dataset<?> d : datasets) {
            d.clear();
        }
        datasets = null;
        plot.clear();
        plot = null;
    }

    @Export
    public Chart getChart() {
        return plotPanel.getChart();
    }

    public int getChartHeight() {
        return plotPanel.getChartHeight();
    }

    public int getChartWidth() {
        return plotPanel.getChartWidth();
    }

    @Export
    public XYPlot getPlot() {
        return plotPanel.getChart().getPlot();
    }

    public Widget getPlotPanel() {
        return plotPanel;
    }

    @Export
    public View getView() {
        return plotPanel.getView();
    }

    public final void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        ArgChecker.isNotNull(this.datasets, "this.datasets");
        plot = (DefaultXYPlot) createPlot(datasets);
        plotPanel.init(plot, width, height);
        Window.addResizeHandler(this);
        if (!isAttached()) {
            if ((targetElementId != null) && (RootPanel.get(targetElementId) != null)) {
                RootPanel rootPanel = RootPanel.get(targetElementId);
                rootPanel.add(this);
            }
        }
    }

    protected void onAttach() {
        initialize();
        super.onAttach();
        wheight = Window.getClientHeight();
        wwidth = Window.getClientWidth();
    }

    /**
   * Called when the window is resized.
   * The algorithm uses is increase/decrease the chart size according with 
   * the new window size.
   */
    public void onResize(ResizeEvent resize) {
        if (!autoResize) {
            return;
        }
        int nwheight = Window.getClientHeight();
        int nwwidth = Window.getClientWidth();
        double wfact = 1;
        double hfact = 1;
        if (nwheight != wheight) {
            hfact = (double) nwheight / (double) wheight;
            wheight = nwheight;
        }
        if (nwwidth != wwidth) {
            wfact = (double) nwwidth / (double) wwidth;
            wwidth = nwwidth;
        }
        if (wfact != 1 || hfact != 1) {
            resize((int) (width * wfact), (int) (height * hfact));
        }
    }

    /**
   * Replace the datasets and redraw all the elements in the chart. It is
   * similar to re-create the graph but the performance is better especially
   * when using flash canvas.
   * 
   * @param dsets json array of the new datasets
   */
    public void replaceDatasets(Dataset[] dsets) {
        setDatasets(dsets);
        Datasets<Tuple2D> d = new Datasets<Tuple2D>(datasets);
        plot.setDatasets(d);
        plot.init();
        plot.redraw(true);
    }

    /**
   * Replace the datasets and redraw all the elements in the chart. It is
   * similar to re-create the graph but the performance is better especially
   * when using flash canvas.
   * 
   * @param jsonDatasets json array of the new datasets
   */
    @Export
    public void replaceDatasets(JsArray<JsonDatasetJSO> jsonDatasets) {
        replaceDatasets(Chronoscope.getInstance().createDatasets(jsonDatasets));
    }

    @Export
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        if (initialized) {
            getView().resize(width, height);
            getChart().redraw();
        }
    }

    /**
   * Enabling autoresize, the chart will increase/decrease its size 
   * according with the window size. 
   */
    @Export
    public void setAutoResize(boolean b) {
        autoResize = b;
    }

    public void setDatasets(Dataset[] dsets) {
        for (int i = 0; datasets != null && i < datasets.length; i++) {
            datasets[i].clear();
        }
        this.datasets = dsets;
    }

    /**
   * Set the name of a javascript window.var which contains an array of dataset,
   * eg: var datasets = [{id:..., domain: ...}, {id:..., domain: ...}];
   * 
   */
    public void setDatasetsVarName(String name) {
        JsArray<JsonDatasetJSO> jsArray = getJsonDatasets(name);
        if (jsArray != null) {
            setDatasets(Chronoscope.getInstance().createDatasets(jsArray));
        }
    }

    /**
   * Set the name of a javascript window.var which contains just one dataset,
   * eg: var datasets = {id:..., domain: ...};
   * 
   * This method could be called many times before initializing the chart so as
   * you can add multiple series to the chart.
   * 
   */
    public void setDatasetVarName(String... names) {
        if (names.length == 0) {
            return;
        }
        if (names.length > 1) {
            for (String name : names) {
                setDatasetVarName(name);
            }
            return;
        }
        JsonDatasetJSO jaDataset = getJsonDataset(names[0]);
        Dataset dataset = Chronoscope.getInstance().getDatasetReader().createDatasetFromJson(new GwtJsonDataset(jaDataset));
        if (datasets == null) {
            datasets = new Dataset[] { dataset };
        } else {
            ArrayList<Dataset> a = new ArrayList<Dataset>(Arrays.asList(datasets));
            a.add(dataset);
            datasets = a.toArray(new Dataset[a.size()]);
        }
    }

    public void setDimensions(int width, int height) {
        resize(width, height);
    }

    public void setTargetElement(Element element) {
        if (element == null) {
            return;
        }
        targetElementId = DOM.getElementAttribute(element, "id");
        if (null == targetElementId || targetElementId.isEmpty()) {
            targetElementId = "cp" + (int) (Math.random() * 999.9);
            DOM.setElementAttribute(element, "id", targetElementId);
        }
    }

    public void setGssContext(GssContext gssContext) {
        plotPanel.setGssContext(gssContext);
    }

    public void setHeight(String height) {
        if (height.contains("px") || height.matches("^\\d+$")) {
            resize(width, Integer.parseInt(height.replaceAll("[^\\d]+", "")));
        }
    }

    public void setViewReadyCallback(ViewReadyCallback callback) {
        plotPanel.setReadyListener(callback);
    }

    public void setWidth(String width) {
        if (width.contains("px") || width.matches("^\\d+$")) {
            resize(Integer.parseInt(width.replaceAll("[^\\d]+", "")), height);
        }
    }

    /**
   * use setViewReadyCallback
   */
    @Deprecated
    public void setReadyListener(ViewReadyCallback viewReadyCallback) {
        setViewReadyCallback(viewReadyCallback);
    }
}
