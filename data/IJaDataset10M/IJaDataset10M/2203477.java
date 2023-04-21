package net.sourceforge.eclipsetrader.ta_lib.internal;

import net.sourceforge.eclipsetrader.charts.IndicatorPluginPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class TALibIndicatorPreferencePage extends IndicatorPluginPreferencePage {

    public TALibIndicatorPreferencePage() {
    }

    public Combo addMovingAverageSelector(Composite parent, String id, String text, int defaultValue) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        Combo combo = new Combo(parent, SWT.READ_ONLY);
        combo.add(Messages.TALibIndicatorPreferencePage_Simple);
        combo.add(Messages.TALibIndicatorPreferencePage_Exponential);
        combo.add(Messages.TALibIndicatorPreferencePage_Weighted);
        combo.add(Messages.TALibIndicatorPreferencePage_DoubleExponential);
        combo.add(Messages.TALibIndicatorPreferencePage_TripleExponential);
        combo.add(Messages.TALibIndicatorPreferencePage_Triangular);
        combo.add(Messages.TALibIndicatorPreferencePage_KaufmanAdaptive);
        combo.add(Messages.TALibIndicatorPreferencePage_MESAAdaptive);
        combo.add(Messages.TALibIndicatorPreferencePage_TripleExponentialT3);
        combo.select(getSettings().getInteger(id, defaultValue).intValue());
        addControl(id, combo);
        return combo;
    }
}
