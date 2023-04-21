package net.sourceforge.eclipsetrader.charts.indicators;

import net.sourceforge.eclipsetrader.charts.IndicatorPluginPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class MACDPreferences extends IndicatorPluginPreferencePage {

    public MACDPreferences() {
    }

    public void createControl(Composite parent) {
        Composite content = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = gridLayout.marginHeight = 0;
        content.setLayout(gridLayout);
        content.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        setControl(content);
        addColorSelector(content, "color", Messages.MACDPreferences_Color, MACD.DEFAULT_COLOR);
        addIntegerValueSelector(content, "fastPeriod", Messages.MACDPreferences_FastPeriod, 1, 9999, MACD.DEFAULT_FAST_PERIOD);
        addIntegerValueSelector(content, "slowPeriod", Messages.MACDPreferences_SlowPeriod, 1, 9999, MACD.DEFAULT_SLOW_PERIOD);
        addLabelField(content, "label", Messages.MACDPreferences_Label, MACD.DEFAULT_LABEL);
        addLineTypeSelector(content, "lineType", Messages.MACDPreferences_LineType, MACD.DEFAULT_LINETYPE);
        addMovingAverageSelector(content, "maType", Messages.MACDPreferences_MAType, MACD.DEFAULT_MA_TYPE);
        addInputSelector(content, "input", Messages.MACDPreferences_Input, MACD.DEFAULT_INPUT, false);
        addColorSelector(content, "triggerColor", Messages.MACDPreferences_TriggerColor, MACD.DEFAULT_TRIGGER_COLOR);
        addIntegerValueSelector(content, "triggerPeriod", Messages.MACDPreferences_TriggerPeriod, 1, 9999, MACD.DEFAULT_TRIGGER_PERIOD);
        addLabelField(content, "triggerLabel", Messages.MACDPreferences_TriggerLabel, MACD.DEFAULT_TRIGGER_LABEL);
        addLineTypeSelector(content, "triggerLineType", Messages.MACDPreferences_TriggerLineType, MACD.DEFAULT_TRIGGER_LINETYPE);
        addColorSelector(content, "oscColor", Messages.MACDPreferences_OscColor, MACD.DEFAULT_OSC_COLOR);
        addLabelField(content, "oscLabel", Messages.MACDPreferences_OscLabel, MACD.DEFAULT_OSC_LABEL);
        addLineTypeSelector(content, "oscLineType", Messages.MACDPreferences_OscLineType, MACD.DEFAULT_OSC_LINETYPE);
    }
}
