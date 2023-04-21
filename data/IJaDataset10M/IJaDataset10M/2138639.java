package org.timepedia.chronoscope.client.canvas;

import org.timepedia.chronoscope.client.Chart;
import org.timepedia.chronoscope.client.ChronoscopeMenu;
import org.timepedia.chronoscope.client.ChronoscopeMenuFactory;
import org.timepedia.chronoscope.client.Cursor;
import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.Datasets;
import org.timepedia.chronoscope.client.InfoWindow;
import org.timepedia.chronoscope.client.gss.GssContext;
import org.timepedia.chronoscope.client.gss.GssElement;
import org.timepedia.chronoscope.client.gss.GssProperties;
import org.timepedia.chronoscope.client.util.PortableTimer;
import org.timepedia.chronoscope.client.util.PortableTimerTask;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * View encapsulates platform specific behaviors, such as graphics rendering,
 * timing, and CSS property retrieval. Views support asynchronous creation,
 * therefore, the proper use of a view is to postpone operations until
 * ViewReadyCallback is invoked.
 *
 */
@ExportPackage("chronoscope")
public abstract class View implements Exportable {

    protected int viewHeight, viewWidth;

    protected Canvas frontCanvas, backingCanvas;

    protected ChronoscopeMenuFactory menuFactory = null;

    protected GssContext gssContext;

    protected ViewReadyCallback callback;

    protected Chart chart;

    private boolean doubleBuffered = false;

    private ChronoscopeMenu contextMenu = null;

    protected String viewId = "view" + (int) (999.9 * Math.random());

    public void canvasSetupDone() {
        getCanvas().canvasSetupDone();
    }

    /**
   * Create a ChronoscopeMenu implementation.
   */
    public abstract ChronoscopeMenu createChronoscopeMenu(int x, int y);

    /**
   */
    @Export("createMenu")
    public ChronoscopeMenu createChronoscopeMenu() {
        return createChronoscopeMenu(0, 0);
    }

    /**
   * Create a timer capable of scheduling delayed execution of the given
   * PortableTimerTask. PortableTimerTask is an abstract to ensure that
   * View/Plot related code is not tightly bound to the browser's environment of
   * GWT and can run in an Applet or Servlet environment as well.
   */
    public abstract PortableTimer createTimer(PortableTimerTask run);

    /**
   * Make sure the canvas is currently visible in the UI.
   */
    public void ensureViewVisible() {
    }

    /**
   * If double buffered, the frontCanvas is made invisible, the offscreen canvas
   * is flipped to front, and the references to the front and back canvases are
   * swapped.
   */
    public void flipCanvas() {
        if (doubleBuffered) {
            Canvas tmp = frontCanvas;
            frontCanvas = backingCanvas;
            backingCanvas = tmp;
        }
    }

    /**
   * Returns the backingCanvas (offscreen canvas being drawn to)
   */
    public Canvas getCanvas() {
        return backingCanvas;
    }

    /**
   */
    @Export
    public Chart getChart() {
        return chart;
    }

    public GssContext getGssContext() {
        return gssContext;
    }

    /**
   * Given a GssElement and pseudo class, we utilize the GssContext to retrieve
   * a GssProperties object for this GssElement
   */
    public GssProperties getGssProperties(GssElement gssElem, String pseudoElt) {
        return gssContext.getProperties(gssElem, pseudoElt);
    }

    /**
   * shim for UI binder
   */
    private GssProperties shimGssProperties(GssProperties gssProperties) {
        Datasets datasets = getChart().getPlot().getDatasets();
        Dataset shim = datasets.getById("shim__");
        return gssProperties;
    }

    public int getHeight() {
        return viewHeight;
    }

    public int getWidth() {
        return viewWidth;
    }

    public String getViewId() {
        return viewId;
    }

    /**
   * Create a view with the given dimensions, GssContext, calling the
   * ViewReadyCallback when all Canvases are created and the view layer is
   * ready.
   */
    public void initialize(final int width, final int height, boolean doubleBuffered, final GssContext gssContext, final ViewReadyCallback callback) {
        this.viewWidth = width == 0 ? 400 : width;
        this.viewHeight = height == 0 ? 300 : height;
        this.gssContext = gssContext;
        this.doubleBuffered = doubleBuffered;
        gssContext.setView(this);
        this.callback = callback;
        backingCanvas = createCanvas(viewWidth, viewHeight);
        if (doubleBuffered) {
            frontCanvas = createCanvas(viewWidth, viewHeight);
        }
    }

    public String numberFormat(String labelFormat, double value) {
        return String.valueOf(value);
    }

    /**
   * Invoked when the parent element containing this view is added to the
   * visible UI hierarchy (e.g. DOM)
   */
    public void onAttach() {
        backingCanvas.attach(this, new CanvasReadyCallback() {

            public void onCanvasReady(Canvas canvas) {
                if (doubleBuffered) {
                    frontCanvas.attach(View.this, new CanvasReadyCallback() {

                        public void onCanvasReady(Canvas canvas) {
                            allCanvasReady();
                        }
                    });
                } else {
                    allCanvasReady();
                }
            }
        });
    }

    /**
   * Popup a window containing the given HTML at the coordinates specified
   * (relative to plot insets)
   */
    public abstract InfoWindow createInfoWindow(String html, double x, double y);

    /**
   * Hack, to add IEEERemainer function for GWT until fixed in JRE emul
   */
    public double remainder(double numerator, double modulus) {
        return 0;
    }

    /**
   * Resizing the chart once displayed currently unsupported
   */
    @Export
    public void resize(int width, int height) {
        chart.reloadStyles();
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    /**
   * Attach a context menu to this View
   *
   */
    @Export
    public void setContextMenu(ChronoscopeMenu cm) {
        if (contextMenu == null) {
            contextMenu = cm;
        } else {
            contextMenu = cm;
        }
    }

    /**
   * Hint to set the mouse cursor to a particular mode.
   */
    public void setCursor(Cursor cursor) {
    }

    /**
   * A menu factory is used to delegate the creation of Menu UI widgets
   */
    public void setMenuFactory(ChronoscopeMenuFactory menuFactory) {
        this.menuFactory = menuFactory;
    }

    /**
   * Invoked after all canvases (front, back, etc) are created
   */
    protected void allCanvasReady() {
        init();
        if (callback != null) {
            callback.onViewReady(this);
        }
    }

    /**
   * Implement this method to create a Canvas with the given dimensions.
   */
    protected abstract Canvas createCanvas(int width, int height);

    /**
   * Override to provide View-specific initialization.
   */
    protected void init() {
    }

    private ChronoscopeMenu getContextMenu() {
        return contextMenu;
    }

    public GssProperties getGssPropertiesBySelector(String gssSelector) {
        return gssContext.getPropertiesBySelector(gssSelector);
    }

    public void dispose() {
        if (null != backingCanvas) {
            backingCanvas.dispose();
        }
        backingCanvas = null;
        if (null != frontCanvas) {
            frontCanvas.dispose();
        }
        frontCanvas = null;
        callback = null;
        chart = null;
        contextMenu = null;
        gssContext = null;
        menuFactory = null;
    }
}
