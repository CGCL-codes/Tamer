package org.plazmaforge.studio.reportdesigner.dialogs.chart.plot;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.model.chart.plot.ThermometerPlot;
import org.plazmaforge.studio.reportdesigner.model.data.Entry;
import org.plazmaforge.studio.reportdesigner.util.ReportUtils;

/**
 * 
 * @author Oleh Hapon
 * $Id: ChartThermometerPlotItemProvider.java,v 1.1 2010/08/23 09:49:48 ohapon Exp $
 */
public class ChartThermometerPlotItemProvider extends ChartPlotItemProvider {

    private Combo valueLocationField;

    private Button showValueLinesField;

    private ValueDisplayWidgetProvider valueDisplayWidgetProvider;

    private RangeWidgetProvider dataRangeWidgetProvider;

    private RangeWidgetProvider lowRangeWidgetProvider;

    private RangeWidgetProvider mediumRangeWidgetProvider;

    private RangeWidgetProvider highRangeWidgetProvider;

    private List<Entry> valueLocations;

    public ChartThermometerPlotItemProvider() {
        super();
        setTitle("Thermometer Plot");
    }

    public Composite createPanel(Composite parent) {
        Composite c = super.createPanel(parent);
        c.setLayout(new FillLayout());
        ScrolledComposite scrolledComposite = createScrolledComposite(c);
        Composite composite = new Composite(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(composite);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);
        leftMargin = 10;
        columnWidth = COLUMN_WIDTH;
        Label valueLocationLabel = new Label(composite, SWT.NONE);
        valueLocationLabel.setText(ReportDesignerResources.Value_location);
        initLayoutDataFix(valueLocationLabel);
        valueLocationField = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        GridData d = new GridData(SWT.FILL, SWT.CENTER, false, false);
        d.horizontalIndent = leftMargin;
        d.widthHint = columnWidth;
        valueLocationField.setLayoutData(d);
        Label showValueLinesLabel = new Label(composite, SWT.NONE);
        showValueLinesLabel.setText(ReportDesignerResources.Show_value_lines);
        initLayoutDataFix(showValueLinesLabel);
        showValueLinesField = new Button(composite, SWT.CHECK);
        d = new GridData(SWT.FILL, SWT.CENTER, false, false);
        d.horizontalIndent = leftMargin;
        d.widthHint = columnWidth;
        showValueLinesField.setLayoutData(d);
        valueDisplayWidgetProvider = new ValueDisplayWidgetProvider();
        valueDisplayWidgetProvider.setTitle(ReportDesignerResources.Values);
        valueDisplayWidgetProvider.setValueDisplay(getThermometerPlot().getValueDisplay());
        valueDisplayWidgetProvider.setReportEditor(getReportEditor());
        valueDisplayWidgetProvider.setForceChange(isForceChange());
        valueDisplayWidgetProvider.createWidget(composite);
        dataRangeWidgetProvider = new RangeWidgetProvider();
        dataRangeWidgetProvider.setTitle(ReportDesignerResources.Data_Range);
        dataRangeWidgetProvider.setDataRange(getThermometerPlot().getDataRange());
        dataRangeWidgetProvider.setReportEditor(getReportEditor());
        dataRangeWidgetProvider.setForceChange(isForceChange());
        dataRangeWidgetProvider.createWidget(composite);
        lowRangeWidgetProvider = new RangeWidgetProvider();
        lowRangeWidgetProvider.setTitle(ReportDesignerResources.Low_Range);
        lowRangeWidgetProvider.setDataRange(getThermometerPlot().getLowRange());
        lowRangeWidgetProvider.setReportEditor(getReportEditor());
        lowRangeWidgetProvider.setForceChange(isForceChange());
        lowRangeWidgetProvider.createWidget(composite);
        mediumRangeWidgetProvider = new RangeWidgetProvider();
        mediumRangeWidgetProvider.setTitle(ReportDesignerResources.Medium_Range);
        mediumRangeWidgetProvider.setDataRange(getThermometerPlot().getMediumRange());
        mediumRangeWidgetProvider.setReportEditor(getReportEditor());
        mediumRangeWidgetProvider.setForceChange(isForceChange());
        mediumRangeWidgetProvider.createWidget(composite);
        highRangeWidgetProvider = new RangeWidgetProvider();
        highRangeWidgetProvider.setTitle(ReportDesignerResources.High_Range);
        highRangeWidgetProvider.setDataRange(getThermometerPlot().getHighRange());
        highRangeWidgetProvider.setReportEditor(getReportEditor());
        highRangeWidgetProvider.setForceChange(isForceChange());
        highRangeWidgetProvider.createWidget(composite);
        initControls();
        initScrolledComposite(scrolledComposite);
        initListeners();
        return composite;
    }

    private void initControls() {
        valueLocations = ReportUtils.getThermometerValueLocations();
        populateEntries(valueLocationField, valueLocations);
    }

    public void loadData() {
        ThermometerPlot meterPlot = getThermometerPlot();
        if (meterPlot == null) {
            return;
        }
        setEntryKey(valueLocationField, valueLocations, meterPlot.getValueLocation());
        showValueLinesField.setSelection(meterPlot.isShowValueLines());
        valueDisplayWidgetProvider.loadData();
        dataRangeWidgetProvider.loadData();
        lowRangeWidgetProvider.loadData();
        mediumRangeWidgetProvider.loadData();
        highRangeWidgetProvider.loadData();
    }

    public void storeData() {
        ThermometerPlot meterPlot = getThermometerPlot();
        if (meterPlot == null) {
            return;
        }
        meterPlot.setValueLocation(getByteEntryKey(valueLocationField, valueLocations));
        meterPlot.setShowValueLines(showValueLinesField.getSelection());
        valueDisplayWidgetProvider.storeData();
        dataRangeWidgetProvider.storeData();
        lowRangeWidgetProvider.storeData();
        mediumRangeWidgetProvider.storeData();
        highRangeWidgetProvider.storeData();
    }

    private ThermometerPlot getThermometerPlot() {
        return (ThermometerPlot) getChartPlot();
    }

    public boolean validate() {
        StringBuffer buf = new StringBuffer();
        dataRangeWidgetProvider.validateRange(buf);
        lowRangeWidgetProvider.validateRange(buf);
        mediumRangeWidgetProvider.validateRange(buf);
        highRangeWidgetProvider.validateRange(buf);
        return isError(buf);
    }

    protected void initChangeListeners() {
        addChangeListeners(valueLocationField, ThermometerPlot.PROPERTY_VALUE_LOCATION);
        addChangeListeners(showValueLinesField, ThermometerPlot.PROPERTY_SHOW_VALUE_LINES);
    }

    protected Object getControlValue(Object control) {
        if (control == valueLocationField) {
            return getByteEntryKey(valueLocationField, valueLocations);
        } else if (control == showValueLinesField) {
            return showValueLinesField.getSelection();
        }
        return null;
    }
}
