package org.aiotrade.charting.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.util.LinkedHashMap;
import javax.swing.JLayeredPane;
import java.awt.Graphics;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import org.aiotrade.charting.view.pane.AxisXPane;
import org.aiotrade.charting.view.pane.AxisYPane;
import org.aiotrade.charting.view.pane.ChartPane;
import org.aiotrade.charting.view.pane.DivisionPane;
import org.aiotrade.charting.view.pane.GlassPane;
import org.aiotrade.math.timeseries.SerChangeEvent;
import org.aiotrade.math.timeseries.SerChangeListener;
import org.aiotrade.math.timeseries.MasterSer;
import org.aiotrade.math.timeseries.Ser;
import org.aiotrade.math.timeseries.Var;
import org.aiotrade.charting.chart.Chart;
import org.aiotrade.charting.chart.ChartFactory;
import org.aiotrade.charting.chart.GradientChart;
import org.aiotrade.charting.chart.ProfileChart;
import org.aiotrade.charting.chart.StickChart;
import org.aiotrade.charting.view.pane.DatumPlane;
import org.aiotrade.charting.view.pane.DrawingPane;
import org.aiotrade.charting.view.pane.Pane;
import org.aiotrade.charting.view.pane.XControlPane;
import org.aiotrade.charting.view.pane.YControlPane;
import org.aiotrade.charting.laf.LookFeel;
import org.aiotrade.charting.view.scalar.Scalar;
import org.aiotrade.util.ChangeObservable;
import org.aiotrade.util.ChangeObserver;
import org.aiotrade.util.ChangeObservableHelper;

/**
 * A ChartView's container can be any Component even without a ChartViewContainer,
 * but should reference back to a controller. All ChartViews shares the same
 * controller will have the same cursor behaves.
 *
 * Example: you can add a ChartView directly to a JFrame.
 *
 * masterSer: the ser instaceof MasterSer, with the calendar time feature,
 *            it's put in the masterView to control the cursor;
 * mainSer: vs overlappingSer, this view's main ser.
 *
 *       1..n           1..n
 * ser --------> chart ------> var
 *
 * @author Caoyuan Deng
 */
public abstract class ChartView extends JComponent implements ChangeObservable {

    protected ChartingController controller;

    protected MasterSer masterSer;

    protected Ser mainSer;

    protected Map<Chart<?>, Set<Var<?>>> mainSerChartMapVars = new LinkedHashMap<Chart<?>, Set<Var<?>>>();

    protected Map<Ser, Map<Chart<?>, Set<Var<?>>>> overlappingSerChartMapVars = new LinkedHashMap<Ser, Map<Chart<?>, Set<Var<?>>>>();

    protected int lastDepthOfOverlappingChart = Pane.DEPTH_CHART_BEGIN;

    protected ChartPane mainChartPane;

    protected GlassPane glassPane;

    protected AxisXPane axisXPane;

    protected AxisYPane axisYPane;

    protected DivisionPane divisionPane;

    protected XControlPane xControlPane;

    protected YControlPane yControlPane;

    protected JLayeredPane mainLayeredPane;

    public static final int AXISX_HEIGHT = 12;

    public static final int AXISY_WIDTH = 50;

    public static final int CONTROL_HEIGHT = 12;

    public static final int TITLE_HEIGHT_PER_LINE = 12;

    /** geometry */
    private int nBars;

    private float maxValue = 1;

    private float minValue = 0;

    protected MySerChangeListener serChangeListener = new MySerChangeListener();

    private ComponentAdapter componentAdapter;

    private boolean interactive = true;

    private boolean pinned = false;

    private ChangeObservableHelper observableHelper = new ChangeObservableHelper();

    public ChartView() {
    }

    public ChartView(ChartingController controller, Ser mainSer) {
        init(controller, mainSer);
    }

    public void init(ChartingController controller, Ser mainSer) {
        this.controller = controller;
        this.masterSer = controller.getMasterSer();
        this.mainSer = mainSer;
        createBasisComponents();
        initComponents();
        putChartsOfMainSer();
        this.mainSer.addSerChangeListener(serChangeListener);
    }

    public void addObserver(Object owner, ChangeObserver observer) {
        observableHelper.addObserver(owner, observer);
    }

    public void removeObserver(ChangeObserver observer) {
        observableHelper.removeObserver(observer);
    }

    public void removeObserversOf(Object owner) {
        observableHelper.removeObserversOf(owner);
    }

    /**
     * Changed cases:
     *   rightSideRow
     *   referCursorRow
     *   wBar
     *   onCalendarMode
     */
    public void notifyObserversChanged(Class<? extends ChangeObserver> oberverType) {
        observableHelper.notifyObserversChanged(this, oberverType);
    }

    protected abstract void initComponents();

    private void createBasisComponents() {
        setDoubleBuffered(true);
        setOpaque(true);
        mainChartPane = new ChartPane(this);
        glassPane = new GlassPane(this, mainChartPane);
        axisXPane = new AxisXPane(this, mainChartPane);
        axisYPane = new AxisYPane(this, mainChartPane);
        divisionPane = new DivisionPane(this, mainChartPane);
        mainLayeredPane = new JLayeredPane() {

            /** this will let the pane components getting the proper size when init */
            protected void paintComponent(Graphics g) {
                int width = getWidth();
                int height = getHeight();
                for (Component c : getComponents()) {
                    if (c instanceof Pane) {
                        c.setBounds(0, 0, width, height);
                    }
                }
            }
        };
        mainLayeredPane.setPreferredSize(new Dimension(10, (int) (10 - 10 / 6.18)));
        mainLayeredPane.add(mainChartPane, JLayeredPane.DEFAULT_LAYER);
        glassPane.setPreferredSize(new Dimension(10, (int) (10 - 10 / 6.18)));
        axisXPane.setPreferredSize(new Dimension(10, AXISX_HEIGHT));
        axisYPane.setPreferredSize(new Dimension(AXISY_WIDTH, 10));
        divisionPane.setPreferredSize(new Dimension(10, 1));
    }

    /**
     * The paintComponent() method will always be called automatically whenever
     * the component need to be reconstructed as it is a JComponent.
     */
    protected void paintComponent(Graphics g) {
        prePaintComponent();
        if (isOpaque()) {
            setBackground(LookFeel.getCurrent().backgroundColor);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        postPaintComponent();
    }

    protected void prePaintComponent() {
        computeGeometry();
        getMainChartPane().computeGeometry();
    }

    /**
     * what may affect the geometry:
     * 1. the size of this component changed;
     * 2. the rightCursorRow changed;
     * 3. the ser's value changed or its items added, which need computeMaxMin();
     *
     * The controller only define wBar (the width of each bar), this component
     * will compute number of bars according to its size. So, if you want to more
     * bars displayed, such as an appointed newNBars, you should compute the size of
     * this's container, and call container.setBounds() to proper size, then, the
     * layout manager will layout the size of its ChartView instances automatically,
     * and if success, the newNBars computed here will equals the newNBars you want.
     */
    protected void computeGeometry() {
        final int newNBars = (int) ((getWidth() - AXISY_WIDTH) / controller.getWBar());
        setNBars(Math.max(newNBars, 1));
        computeMaxMin();
    }

    protected void postPaintComponent() {
        if (getAxisXPane() != null) {
            getAxisXPane().syncWithView();
        }
        if (getAxisYPane() != null) {
            getAxisYPane().syncWithView();
        }
        if (getXControlPane() != null) {
            getXControlPane().syncWithView();
        }
        if (getYControlPane() != null) {
            getYControlPane().syncWithView();
        }
    }

    private void setNBars(int nBars) {
        int oldValue = this.nBars;
        this.nBars = nBars;
        if (this.nBars != oldValue) {
            notifyObserversChanged(ChartValidityObserver.class);
        }
    }

    protected void setMaxValue(float value) {
        float oldValue = this.maxValue;
        this.maxValue = value;
        if (this.maxValue != oldValue) {
            notifyObserversChanged(ChartValidityObserver.class);
        }
    }

    protected void setMinValue(float value) {
        float oldValue = this.minValue;
        this.minValue = value;
        if (this.minValue != oldValue) {
            notifyObserversChanged(ChartValidityObserver.class);
        }
    }

    public void setSelected(boolean b) {
        getGlassPane().setSelected(b);
    }

    public void setInteractive(boolean b) {
        getGlassPane().setInteractive(b);
        this.interactive = b;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void pin() {
        getGlassPane().setPinned(true);
        this.pinned = true;
    }

    public void unPin() {
        getGlassPane().setPinned(false);
        this.pinned = false;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setYChartScale(float yChartScale) {
        DatumPlane mainChartPane = getMainChartPane();
        if (mainChartPane != null) {
            mainChartPane.setYChartScale(yChartScale);
        }
        repaint();
    }

    public void setValueScalar(Scalar valueScalar) {
        DatumPlane mainChartPane = getMainChartPane();
        if (mainChartPane != null) {
            mainChartPane.setValueScalar(valueScalar);
        }
        repaint();
    }

    public void adjustYChartScale(float increment) {
        DatumPlane mainChartPane = getMainChartPane();
        if (mainChartPane != null) {
            mainChartPane.growYChartScale(increment);
        }
        repaint();
    }

    public void setYChartScaleByCanvasValueRange(double canvasValueRange) {
        DatumPlane mainChartPane = getMainChartPane();
        if (mainChartPane != null) {
            mainChartPane.setYChartScaleByCanvasValueRange(canvasValueRange);
        }
        repaint();
    }

    public void scrollChartsVerticallyByPixel(int increment) {
        ChartPane mainChartPane = getMainChartPane();
        if (mainChartPane != null) {
            mainChartPane.scrollChartsVerticallyByPixel(increment);
        }
        repaint();
    }

    /**
     * barIndex -> time
     *
     * @param barIndex, index of bars, start from 1 and to nBars
     * @return time
     */
    public final long tb(int barIndex) {
        return masterSer.timeOfRow(rb(barIndex));
    }

    public final int rb(int barIndex) {
        return getController().getRightSideRow() - nBars + barIndex;
    }

    /**
     * time -> barIndex
     *
     * @param time
     * @return index of bars, start from 1 and to nBars
     */
    public final int bt(long time) {
        return br(masterSer.rowOfTime(time));
    }

    public final int br(int row) {
        return row - getController().getRightSideRow() + nBars;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public final int getNBars() {
        return nBars;
    }

    public GlassPane getGlassPane() {
        return glassPane;
    }

    public ChartPane getMainChartPane() {
        return mainChartPane;
    }

    public AxisXPane getAxisXPane() {
        return axisXPane;
    }

    public AxisYPane getAxisYPane() {
        return axisYPane;
    }

    public final ChartingController getController() {
        return controller;
    }

    public final Ser getMainSer() {
        return mainSer;
    }

    public JLayeredPane getMainLayeredPane() {
        return mainLayeredPane;
    }

    public Map<Chart<?>, Set<Var<?>>> getMainSerChartMapVars() {
        return mainSerChartMapVars;
    }

    public Map<Chart<?>, Set<Var<?>>> getChartMapVars(final Ser ser) {
        assert ser != null : "Do not pass me a null ser!";
        return ser == getMainSer() ? mainSerChartMapVars : overlappingSerChartMapVars.get(ser);
    }

    public Collection<Ser> getOverlappingSers() {
        return overlappingSerChartMapVars.keySet();
    }

    public Collection<Ser> getAllSers() {
        Collection<Ser> allSers = new HashSet<Ser>();
        allSers.add(getMainSer());
        allSers.addAll(getOverlappingSers());
        return allSers;
    }

    public void popupToDesktop() {
    }

    public void addOverlappingCharts(Ser ser) {
        ser.addSerChangeListener(serChangeListener);
        Map<Chart<?>, Set<Var<?>>> chartVarsMap = new LinkedHashMap<Chart<?>, Set<Var<?>>>();
        overlappingSerChartMapVars.put(ser, chartVarsMap);
        int depthGradient = Pane.DEPTH_GRADIENT_BEGIN;
        for (Var<?> var : ser.varSet()) {
            Set<Var<?>> chartVars = new HashSet<Var<?>>();
            Chart<?> chart = ChartFactory.createVarChart(chartVars, var);
            if (chart != null) {
                chartVarsMap.put(chart, chartVars);
                chart.set(mainChartPane, ser);
                if (chart instanceof GradientChart || chart instanceof ProfileChart) {
                    chart.setDepth(depthGradient--);
                } else if (chart instanceof StickChart) {
                    chart.setDepth(-8);
                } else {
                    chart.setDepth(lastDepthOfOverlappingChart++);
                }
                mainChartPane.putChart(chart);
            }
        }
        notifyObserversChanged(ChartValidityObserver.class);
        repaint();
    }

    public void removeOverlappingCharts(Ser ser) {
        ser.removeSerChangeListener(serChangeListener);
        Map<Chart<?>, Set<Var<?>>> chartVarsMap = overlappingSerChartMapVars.get(ser);
        if (chartVarsMap != null) {
            for (Chart<?> chart : chartVarsMap.keySet()) {
                mainChartPane.removeChart(chart);
                if (chart instanceof GradientChart || chart instanceof ProfileChart) {
                } else if (chart instanceof StickChart) {
                } else {
                    lastDepthOfOverlappingChart--;
                }
            }
            chartVarsMap.clear();
            overlappingSerChartMapVars.remove(ser);
        }
        notifyObserversChanged(ChartValidityObserver.class);
        repaint();
    }

    public void computeMaxMin() {
        maxValue = 1;
        minValue = 0;
    }

    protected abstract void putChartsOfMainSer();

    /** this method only process FinishedComputing event, if you want more, do it in subclass */
    protected void updateView(SerChangeEvent evt) {
        if (evt.getType() == SerChangeEvent.Type.FinishedComputing) {
            if (this instanceof WithDrawingPane) {
                DrawingPane drawing = ((WithDrawingPane) ChartView.this).getSelectedDrawing();
                if (drawing != null && drawing.isInDrawing()) {
                    return;
                }
            }
            notifyObserversChanged(ChartValidityObserver.class);
            repaint();
        }
    }

    /**
     * @return x-control pane, may be <code>null</code>
     */
    public XControlPane getXControlPane() {
        return xControlPane;
    }

    /**
     * @return y-control pane, may be <code>null</code>
     */
    public YControlPane getYControlPane() {
        return yControlPane;
    }

    @Override
    protected void finalize() throws Throwable {
        if (serChangeListener != null) {
            mainSer.removeSerChangeListener(serChangeListener);
        }
        super.finalize();
    }

    private class MySerChangeListener implements SerChangeListener {

        public void serChanged(SerChangeEvent evt) {
            switch(evt.getType()) {
                case FinishedComputing:
                case Updated:
                    updateView(evt);
                    break;
                default:
            }
            evt.callBack();
        }
    }
}
