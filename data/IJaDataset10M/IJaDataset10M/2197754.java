package org.lcelb.accounts.manager.ui.extensions.preferences.chart;

import org.lcelb.accounts.manager.ui.extensions.message.Messages;
import org.lcelb.accounts.manager.ui.extensions.preferences.AbstractDefaultPreferencePage;

/**
 * Appearance preference page related to CategoryMonthTotalsChartView.
 * @author La Carotte
 */
public class ChartViewsAppearancePreferencePage extends AbstractDefaultPreferencePage {

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.preferences.AbstractDefaultPreferencePage#getPageDescription()
   */
    @Override
    protected String getPageDescription() {
        return Messages.ChartViewsAppearancePreferencePage_Description;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.extensions.preferences.AbstractDefaultPreferencePage#getPageTitle()
   */
    @Override
    protected String getPageTitle() {
        return Messages.ChartViewsAppearancePreferencePage_Title;
    }

    /**
   * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
   */
    @Override
    protected void createFieldEditors() {
    }
}
