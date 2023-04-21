package de.jaret.examples.timebars.millis.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import de.jaret.examples.timebars.millis.model.BreakIntervalFilter;
import de.jaret.examples.timebars.millis.model.ModelCreator;
import de.jaret.examples.timebars.millis.swt.renderer.GlobalBreakRenderer;
import de.jaret.examples.timebars.millis.swt.renderer.MilliGrid;
import de.jaret.examples.timebars.millis.swt.renderer.MilliScale;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;
import de.jaret.util.date.iterator.DateIterator;
import de.jaret.util.date.iterator.DayIterator;
import de.jaret.util.date.iterator.HourIterator;
import de.jaret.util.date.iterator.IIteratorFormatter;
import de.jaret.util.date.iterator.MillisecondIterator;
import de.jaret.util.date.iterator.MinuteIterator;
import de.jaret.util.date.iterator.MonthIterator;
import de.jaret.util.date.iterator.SecondIterator;
import de.jaret.util.date.iterator.WeekIterator;
import de.jaret.util.date.iterator.YearIterator;
import de.jaret.util.date.iterator.DateIterator.Format;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.TimeBarMarkerListener;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.mod.DefaultIntervalModificator;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;
import de.jaret.util.ui.timebars.model.DefaultTimeBarNode;
import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.PPSInterval;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.swt.TimeBarViewer;
import de.jaret.util.ui.timebars.swt.renderer.BoxTimeScaleRenderer;
import de.jaret.util.ui.timebars.swt.renderer.DefaultTimeScaleRenderer;
import de.jaret.util.ui.timebars.swt.renderer.DefaultTitleRenderer;

/**
 * Example showing millisecond accuracy usage.
 * 
 * @author Peter Kliem
 * @version $Id: MilliExample.java 894 2009-11-02 22:29:11Z kliem $
 */
public class MilliExample extends ApplicationWindow {

    private TimeBarViewer _tbv;

    private TimeBarViewer _tbv2;

    public MilliExample() {
        super(null);
    }

    protected Control createContents(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);
        ModelCreator creator = new ModelCreator();
        TimeBarModel model = creator.createModel();
        _tbv = new TimeBarViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd = new GridData(GridData.FILL_BOTH);
        _tbv.setLayoutData(gd);
        _tbv.addIntervalModificator(new DefaultIntervalModificator());
        _tbv.setMilliAccuracy(true);
        _tbv.setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
        _tbv.setModel(model);
        _tbv.setPixelPerSecond(6000);
        _tbv.setDrawRowGrid(true);
        _tbv.setTimeScaleRenderer(new MilliScale());
        _tbv.setGridRenderer(new MilliGrid());
        DefaultTitleRenderer titleRenderer = new DefaultTitleRenderer();
        titleRenderer.setBackgroundRscName("/de/jaret/examples/timebars/hierarchy/swt/titlebg.png");
        _tbv.setTitleRenderer(titleRenderer);
        _tbv.setTitle("Millis");
        MilliControlPanel ctrl = new MilliControlPanel(parent, SWT.NULL, _tbv, null);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        ctrl.setLayoutData(gd);
        _tbv.setVariableXScale(true);
        DefaultTimeBarNode scaleRow = (DefaultTimeBarNode) _tbv.getPpsRow();
        creator.createPPSIntervals(_tbv.getPixelPerSecond(), scaleRow);
        _tbv.setIntervalFilter(new BreakIntervalFilter(_tbv.getPpsRow()));
        _tbv.setGlobalAssistantRenderer(new GlobalBreakRenderer());
        DefaultTimeBarModel model2 = new DefaultTimeBarModel();
        model2.addRow(scaleRow);
        _tbv2 = new TimeBarViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        _tbv2.setLayoutData(gd);
        TimeBarViewerDelegate delegate2 = (TimeBarViewerDelegate) _tbv2.getData("delegate");
        delegate2.setMilliAccuracy(true);
        _tbv2.setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
        _tbv2.setModel(model2);
        _tbv2.setPixelPerSecond(1000.0 / 30.0);
        _tbv2.setDrawRowGrid(true);
        _tbv2.setGridRenderer(null);
        _tbv2.setTimeScaleRenderer(new MilliScale(1000, 10000));
        _tbv2.addIntervalModificator(new DefaultIntervalModificator());
        _tbv2.setTitleRenderer(titleRenderer);
        _tbv2.setTitle("Scale");
        final TimeBarMarker marker = new TimeBarMarkerImpl(true, _tbv.getStartDate().copy());
        _tbv2.addMarker(marker);
        _tbv.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("StartDate")) {
                    marker.setDate(((JaretDate) evt.getNewValue()).copy());
                }
            }
        });
        marker.addTimeBarMarkerListener(new TimeBarMarkerListener() {

            public void markerDescriptionChanged(TimeBarMarker marker, String oldValue, String newValue) {
            }

            public void markerMoved(TimeBarMarker marker, JaretDate oldDate, JaretDate currentDate) {
                _tbv.setStartDate(currentDate.copy());
            }
        });
        return _tbv;
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(getClass().getName());
        shell.setSize(1200, 800);
    }

    public static void main(String[] args) {
        MilliExample test = new MilliExample();
        test.setBlockOnOpen(true);
        test.open();
    }
}
