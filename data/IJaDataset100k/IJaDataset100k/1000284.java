package org.das2.components;

import org.das2.graph.DasColumn;
import org.das2.graph.DasCanvas;
import org.das2.graph.GraphUtil;
import org.das2.graph.DasRow;
import org.das2.graph.DasPlot;
import org.das2.graph.DasAxis;
import org.das2.dataset.TableDataSetConsumer;
import org.das2.datum.DatumRange;
import org.das2.datum.Units;
import org.das2.datum.Datum;
import org.das2.event.BoxSelectionEvent;
import org.das2.event.BoxSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.das2.graph.SeriesRenderer;
import org.virbo.dataset.DataSetUtil;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dsutil.DataSetBuilder;

public class AngleSpectrogramSlicer extends DasPlot implements BoxSelectionListener {

    private JDialog popupWindow;

    private SeriesRenderer renderer;

    private DasPlot parentPlot;

    private TableDataSetConsumer consumer;

    private Datum xstart;

    private Datum ystart;

    private DatumRange xrange;

    private DatumRange yrange;

    private int sliceDir;

    private final int SLICEDIR_HORIZ = 0;

    private final int SLICEDIR_VERTICAL = 1;

    private AngleSpectrogramSlicer(DasPlot plot, DasAxis xAxis, DasAxis yAxis, TableDataSetConsumer consumer) {
        super(xAxis, yAxis);
        parentPlot = plot;
        renderer = new SeriesRenderer();
        this.consumer = consumer;
        addRenderer(renderer);
    }

    public static AngleSpectrogramSlicer createSlicer(DasPlot plot, TableDataSetConsumer dataSetConsumer) {
        DasAxis sourceXAxis = plot.getXAxis();
        DasAxis xAxis = sourceXAxis.createAttachedAxis(DasAxis.HORIZONTAL);
        DasAxis yAxis = dataSetConsumer.getZAxis().createAttachedAxis(DasAxis.VERTICAL);
        return new AngleSpectrogramSlicer(plot, xAxis, yAxis, dataSetConsumer);
    }

    public void showPopup() {
        if (SwingUtilities.isEventDispatchThread()) {
            showPopupImpl();
        } else {
            Runnable r = new Runnable() {

                public void run() {
                    showPopupImpl();
                }
            };
        }
    }

    /** This method should ONLY be called by the AWT event thread */
    private void showPopupImpl() {
        if (popupWindow == null) {
            createPopup();
        }
        popupWindow.setVisible(true);
    }

    /** This method should ONLY be called by the AWT event thread */
    private void createPopup() {
        int width = parentPlot.getCanvas().getWidth() / 2;
        int height = parentPlot.getCanvas().getHeight() / 2;
        final DasCanvas canvas = new DasCanvas(width, height);
        DasRow row = new DasRow(canvas, 0.1, 0.9);
        DasColumn column = new DasColumn(canvas, 0.1, 0.9);
        canvas.add(this, row, column);
        JPanel content = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.add(Box.createHorizontalGlue());
        JButton printButton = new JButton(new AbstractAction("Print...") {

            public void actionPerformed(ActionEvent e) {
                canvas.makeCurrent();
                canvas.PRINT_ACTION.actionPerformed(e);
            }
        });
        buttonPanel.add(printButton);
        JButton close = new JButton("Hide Window");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                popupWindow.setVisible(false);
            }
        });
        buttonPanel.add(close);
        content.add(canvas, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        Window parentWindow = SwingUtilities.getWindowAncestor(parentPlot);
        if (parentWindow instanceof Frame) {
            popupWindow = new JDialog((Frame) parentWindow);
        } else if (parentWindow instanceof Dialog) {
            popupWindow = new JDialog((Dialog) parentWindow);
        } else {
            popupWindow = new JDialog();
        }
        popupWindow.setTitle("Angle Slicer");
        popupWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popupWindow.setContentPane(content);
        popupWindow.pack();
        Point parentLocation = new Point();
        SwingUtilities.convertPointToScreen(parentLocation, parentPlot.getCanvas());
        popupWindow.setLocation(parentLocation.x + parentPlot.getCanvas().getWidth(), parentLocation.y + height);
    }

    private QDataSet angleSliceHoriz(QDataSet tds, DatumRange xlimit, Datum xbase, Datum ybase, Datum slope) {
        DataSetBuilder builder = new DataSetBuilder(1, 100);
        DataSetBuilder xbuilder = new DataSetBuilder(1, 100);
        QDataSet xds = SemanticOps.xtagsDataSet(tds);
        QDataSet yds = SemanticOps.ytagsDataSet(tds);
        int i0 = DataSetUtil.closestIndex(xds, xlimit.min());
        int i1 = DataSetUtil.closestIndex(xds, xlimit.max());
        int irow0 = 0;
        int irow1 = 0;
        Units zunits = SemanticOps.getUnits(tds);
        Units yunits = SemanticOps.getUnits(yds);
        Units xunits = SemanticOps.getUnits(xds);
        for (int i = i0; i < i1; i++) {
            double x = xds.value(i);
            double y = ybase.add(slope.multiply(x - xbase.doubleValue(xunits))).doubleValue(yunits);
            while (irow0 > 0 && yds.value(irow0) > y) irow0--;
            irow1 = irow0 + 1;
            while ((irow1 + 1) < yds.length() && yds.value(irow1) < y) irow1++;
            irow0 = irow1 - 1;
            if (irow0 > 0) {
                double z0 = tds.value(i, irow0);
                double z1 = tds.value(i, irow1);
                double y0 = yds.value(irow0);
                double y1 = yds.value(irow1);
                double yy = y;
                double alpha = (yy - y0) / (y1 - y0);
                double zinterp = z0 + (z1 - z0) * alpha;
                builder.putValue(-1, zinterp);
                xbuilder.putValue(-1, x);
                builder.nextRecord();
                xbuilder.nextRecord();
            }
        }
        xbuilder.putProperty(QDataSet.UNITS, xunits);
        builder.putProperty(QDataSet.UNITS, zunits);
        builder.putProperty(QDataSet.DEPEND_0, xbuilder.getDataSet());
        return builder.getDataSet();
    }

    @Override
    public void drawContent(Graphics2D g) {
        double[] ixs;
        double ix;
        if (sliceDir == SLICEDIR_HORIZ) {
            ixs = GraphUtil.transformRange(this.getXAxis(), xrange);
            ix = this.getXAxis().transform(xstart);
        } else {
            ixs = GraphUtil.transformRange(this.getYAxis(), yrange);
            ix = this.getYAxis().transform(ystart);
        }
        DasRow row = getRow();
        g.setColor(new Color(230, 230, 230));
        g.fillRect((int) ixs[0], row.getDMinimum(), (int) (ixs[1] - ixs[0]), row.getHeight());
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine((int) ix, row.getDMinimum(), (int) ix, row.getDMaximum());
        super.drawContent(g);
    }

    protected void processDasUpdateEvent(org.das2.event.DasUpdateEvent e) {
        if (isDisplayable()) {
            updateImmediately();
            resize();
        }
    }

    public void BoxSelected(BoxSelectionEvent e) {
        Datum xbase = e.getStartX();
        Datum ybase = e.getStartY();
        xstart = e.getStartX();
        ystart = e.getStartY();
        xrange = e.getXRange();
        yrange = e.getYRange();
        Datum slope = e.getFinishY().subtract(ybase).divide(e.getFinishX().subtract(xbase));
        QDataSet ds = consumer.getConsumedDataSet();
        if (ds == null || !(SemanticOps.isSimpleTableDataSet(ds))) {
            return;
        }
        QDataSet tds = (QDataSet) ds;
        QDataSet sliceDataSet;
        sliceDataSet = angleSliceHoriz(tds, getXAxis().getDatumRange(), xbase, ybase, slope);
        sliceDir = SLICEDIR_HORIZ;
        renderer.setDataSet(sliceDataSet);
        if (!(popupWindow == null || popupWindow.isVisible()) || getCanvas() == null) {
            showPopup();
        }
    }
}
