package com.ibm.tuningfork.sampleview;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;
import com.ibm.tuningfork.core.action.ConfigureFigureAction;
import com.ibm.tuningfork.core.figure.Figure;
import com.ibm.tuningfork.core.figure.TimeSlider;
import com.ibm.tuningfork.core.graphics.Area;
import com.ibm.tuningfork.core.mouse.HoverMouser;
import com.ibm.tuningfork.core.mouse.MouseMaster;
import com.ibm.tuningfork.core.mouse.MoveLegendMouser;
import com.ibm.tuningfork.core.view.FigureView;

/**
 * View of the figure.
 */
public class SampleView extends FigureView {

    public static final String ID = "com.ibm.tuningfork.sampleview.SampleView";

    public static final int TIMESLICE_VIEW_LOGICAL_REFRESH_PERIOD = 5;

    protected Sample figure;

    protected Slider verticalSlider;

    protected TimeSlider timeSlider;

    public Figure getFigure() {
        return figure;
    }

    protected void setFigure(Figure f) {
        this.figure = (Sample) f;
    }

    protected ConfigureFigureAction createConfigureFigureAction() {
        ConfigureFigureAction configureAction = new ConfigureFigureAction("Sample View", new Action() {

            public void run() {
                new ConfigureSampleViewDialog(getShell(), figure).open();
            }
        });
        return configureAction;
    }

    public void createPartControl(final Composite parent) {
        super.createPartControl(null);
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = layout.marginWidth = 0;
        parent.setLayout(layout);
        int numCols = 2;
        Composite top = new Composite(parent, SWT.NONE);
        Composite bottom = new Composite(parent, SWT.NONE);
        GridLayout rowLayout = new GridLayout(numCols, false);
        rowLayout.marginHeight = rowLayout.marginWidth = 0;
        top.setLayout(rowLayout);
        top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        bottom.setLayout(rowLayout);
        bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        super.createPartControl(top, TIMESLICE_VIEW_LOGICAL_REFRESH_PERIOD);
        verticalSlider = new Slider(top, SWT.VERTICAL);
        verticalSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        verticalSlider.setMinimum(0);
        timeSlider = new TimeSlider(figure, bottom);
        verticalSlider.setIncrement(1);
        verticalSlider.addSelectionListener(new SelectionListener() {

            public final void widgetDefaultSelected(final SelectionEvent ev) {
                figure.sendRedrawToSharedFigure();
            }

            public final void widgetSelected(final SelectionEvent ev) {
                widgetDefaultSelected(ev);
            }
        });
        verticalSlider.setMaximum(100);
        verticalSlider.setSelection(1);
        setupMouseEventHandling();
        contributeToActionBars();
    }

    @Override
    protected void addMousers(final MouseMaster mouseMaster) {
        super.addMousers(mouseMaster);
        Canvas canvas = getCanvas();
        mouseMaster.add(new MoveLegendMouser(canvas, figure));
        mouseMaster.add(new HoverMouser(canvas, figure));
    }

    public void paint(GC gc, Rectangle clientArea) {
        Area a = prePaint(gc, clientArea);
        Figure figure = getFigure();
        figure.paintFigure(getGraphics(), a);
        postPaint(gc);
    }

    protected void updateToolbarActions() {
        super.updateToolbarActions();
        timeSlider.updateTimeSlider();
        updateVerticalSlider();
    }

    private void updateVerticalSlider() {
    }
}
