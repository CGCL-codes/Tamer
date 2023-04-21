package com.tensegrity.palobrowser.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import com.tensegrity.palobrowser.editors.DataComposite;
import com.tensegrity.palobrowser.editors.charteditor.PaloChart;

public class LegendContent extends Canvas {

    private final PaloChart paloChart;

    public LegendContent(PaloChart parent, int style) {
        super(parent, style | SWT.V_SCROLL | SWT.DOUBLE_BUFFERED);
        this.paloChart = parent;
        addListeners();
    }

    public void refresh() {
        getDisplay().asyncExec(new Runnable() {

            public void run() {
                if (!isDisposed()) {
                    redraw();
                    update();
                }
            }
        });
    }

    public void addListeners() {
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent pe) {
                LegendContent.this.paintControl(pe);
            }
        });
        addMouseListener(new MouseListener() {

            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                Rectangle rectClient = getClientArea();
                if (paloChart.getChartContent().getChart().legend.mouseDown(e, rectClient)) {
                    refresh();
                }
            }

            public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
            }

            public void mouseDoubleClick(MouseEvent e) {
            }
        });
    }

    public void paintControl(PaintEvent pe) {
        GC gc = pe.gc;
        Rectangle clip = getClientArea();
        Rectangle innerClip = new Rectangle(clip.x, clip.y, clip.width + 1, clip.height + 1);
        gc.setClipping(innerClip);
        innerClip.width -= 3;
        innerClip.height -= 2;
        paloChart.getChartContent().getChart().legend.paint(gc, innerClip);
    }

    public DataComposite getDataComposite() {
        return paloChart;
    }
}
