package com.google.gwt.visualization.client.visualizations;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.events.Handler;
import com.google.gwt.visualization.client.events.ReadyHandler;
import com.google.gwt.visualization.client.events.StateChangeHandler;

/**
 * Motion Chart visualization. Note that this chart does not work when loading
 * the HTML from a local file. It works only when loading the HTML from a web
 * server.
 * 
 * @see <a
 *      href="http://code.google.com/apis/visualization/documentation/gallery/motionchart.html"
 *      > Motion Chart Visualization Reference</a>
 */
public class MotionChart extends Visualization<MotionChart.Options> {

    /**
   * Options for drawing the chart.
   */
    public static class Options extends AbstractDrawOptions {

        public static Options create() {
            return JavaScriptObject.createObject().cast();
        }

        protected Options() {
        }

        public final native void setHeight(int height);

        public final native void setShowAdvancedPanel(boolean showAdvancedPanel);

        public final native void setShowChartButtons(boolean showChartButtons);

        public final native void setShowHeader(boolean showHeader);

        public final native void setShowSelectListComponent(boolean showSelectListComponent);

        public final native void setShowSidePanel(boolean showSidePanel);

        public final native void setShowXMetricPicker(boolean showXMetricPicker);

        public final native void setShowXScalePicker(boolean showXScalePicker);

        public final native void setShowYMetricPicker(boolean showYMetricPicker);

        public final native void setShowYScalePicker(boolean showYScalePicker);

        public final void setSize(int width, int height) {
            setWidth(width);
            setHeight(height);
        }

        public final native void setState(String state);

        public final native void setWidth(int width);
    }

    public static final String PACKAGE = "motionchart";

    public MotionChart() {
        super();
    }

    public MotionChart(AbstractDataTable data, Options options) {
        super(data, options);
    }

    public final void addReadyHandler(ReadyHandler handler) {
        Handler.addHandler(this, "ready", handler);
    }

    public final void addStateChangeHandler(StateChangeHandler handler) {
        Handler.addHandler(this, "statechange", handler);
    }

    /**
   * Returns the current state of the {@link MotionChart}, serialized to a JSON
   * string. To assign this state to the chart, assign this string to the state
   * option in the draw() method. This is often used to specify a custom chart
   * state on startup, instead of using the default state.
   * 
   * @return a JSON encoded string indicating the state of the UI. This method
   *         may return <code>null</code> if the state was not supplied by
   *         {@link MotionChart.Options#setState(String)} or a statechange event
   *         has not yet fired.
   */
    public final native String getState();

    @Override
    protected native JavaScriptObject createJso(Element parent);
}
