package com.ivis.xprocess.ui.preferences.pages;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.ivis.xprocess.ui.UIConstants;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.preferences.Timescale;
import com.ivis.xprocess.ui.preferences.managers.PreferenceChangeManager;
import com.ivis.xprocess.ui.preferences.managers.PreferenceChangeManager.ChangedPreference;
import com.ivis.xprocess.ui.properties.PreferenceMessages;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.util.UIToolkit;

public class TimeUnitPreferences extends PreferencePage implements IWorkbenchPreferencePage {

    private boolean effortPreferenceChanged = false;

    private boolean decimalPlacePreferenceChanged = false;

    private boolean decimalDayPlacePreferenceChanged = false;

    private boolean dailyRecordspreferenceChanged = false;

    private Combo effortTimeScaleCombo;

    private Combo dailyRecordsTimeScaleCombo;

    private Composite composite;

    private Combo decimalPlacesCombo;

    private Combo decimalDayPlacesCombo;

    @Override
    protected Control createContents(Composite parent) {
        String description = PreferenceMessages.timeunit_preference_page_title;
        composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        composite.setLayout(gridLayout);
        Label descriptionLabel = new Label(composite, SWT.WRAP);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        gridData.verticalSpan = 2;
        descriptionLabel.setLayoutData(gridData);
        descriptionLabel.setText(description);
        createBlankLine(composite, 2);
        createEffortGroup(composite);
        createBlankLine(composite, 2);
        createDailyRecordsGroup(composite);
        Composite decimalPlacesComposite = new Composite(composite, SWT.NONE);
        decimalPlacesComposite.setLayout(new GridLayout(2, false));
        gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        decimalPlacesComposite.setLayoutData(gridData);
        Label decimalPlaceLabel = new Label(decimalPlacesComposite, SWT.NONE);
        decimalPlaceLabel.setText(PreferenceMessages.timeunit_dailyrecords_decimalplaces);
        decimalPlacesCombo = new Combo(decimalPlacesComposite, SWT.READ_ONLY);
        for (int i = 0; i < 2; i++) {
            decimalPlacesCombo.add("" + (i + 1));
        }
        decimalPlacesCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                decimalPlacePreferenceChanged = true;
            }
        });
        gridData = new GridData();
        decimalPlacesCombo.setLayoutData(gridData);
        Label decimalDayPlaceLabel = new Label(decimalPlacesComposite, SWT.NONE);
        decimalDayPlaceLabel.setText(PreferenceMessages.timeunit_dailyrecords_decimaldayplaces);
        decimalDayPlacesCombo = new Combo(decimalPlacesComposite, SWT.READ_ONLY);
        for (int i = 0; i < 2; i++) {
            decimalDayPlacesCombo.add("" + (i + 1));
        }
        decimalDayPlacesCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                decimalDayPlacePreferenceChanged = true;
            }
        });
        gridData = new GridData();
        decimalDayPlacesCombo.setLayoutData(gridData);
        displayData();
        setupTestHarness();
        return composite;
    }

    private void createBlankLine(Composite parent, int horizontalSpan) {
        Label blankerLabel = new Label(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = horizontalSpan;
        blankerLabel.setLayoutData(gridData);
    }

    private void createEffortGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(PreferenceMessages.timeunit_taskestimates_group_title);
        String groupDescription = PreferenceMessages.timeunit_taskestimates_group_description;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gridData.verticalAlignment = SWT.TOP;
        group.setLayoutData(gridData);
        createGroupDescription(group, groupDescription);
        Label label = new Label(group, SWT.NONE);
        label.setText(PreferenceMessages.timeunit_taskestimates_group_label);
        effortTimeScaleCombo = UIToolkit.createComboFromEnum(group, Timescale.class, UIConstants.units_estimate);
        effortTimeScaleCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (UIPlugin.getDefault().getPreferenceStore().getInt(UIConstants.units_estimate) != effortTimeScaleCombo.getSelectionIndex()) {
                    effortPreferenceChanged = true;
                }
            }
        });
        gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        effortTimeScaleCombo.setLayoutData(gridData);
    }

    private void createDailyRecordsGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(PreferenceMessages.timeunit_dailyrecords_group_title);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group.setLayout(gridLayout);
        String groupDescription = PreferenceMessages.timeunit_dailyrecords_group_description;
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gridData.verticalAlignment = SWT.TOP;
        group.setLayoutData(gridData);
        createGroupDescription(group, groupDescription);
        Label label = new Label(group, SWT.NONE);
        label.setText(PreferenceMessages.timeunit_dailyrecords_group_label);
        dailyRecordsTimeScaleCombo = UIToolkit.createComboFromEnum(group, Timescale.class, UIConstants.units_dailyrecord);
        dailyRecordsTimeScaleCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (UIPlugin.getDefault().getPreferenceStore().getInt(UIConstants.units_dailyrecord) != dailyRecordsTimeScaleCombo.getSelectionIndex()) {
                    dailyRecordspreferenceChanged = true;
                }
            }
        });
        gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        dailyRecordsTimeScaleCombo.setLayoutData(gridData);
    }

    private void createGroupDescription(Group group, String description) {
        Label groupDescriptionLabel = new Label(group, SWT.WRAP);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        gridData.verticalSpan = 2;
        gridData.horizontalSpan = 2;
        groupDescriptionLabel.setLayoutData(gridData);
        groupDescriptionLabel.setText(description);
    }

    private void displayData() {
        effortTimeScaleCombo.select(UIPlugin.getDefault().getPreferenceStore().getInt(UIConstants.units_estimate));
        decimalPlacesCombo.select(UIPlugin.getDefault().getPreferenceStore().getInt(UIConstants.units_estimate_decimalplaces) - 1);
        decimalDayPlacesCombo.select(UIPlugin.getDefault().getPreferenceStore().getInt(UIConstants.units_estimate_decimaldayplaces) - 1);
        dailyRecordsTimeScaleCombo.select(UIPlugin.getDefault().getPreferenceStore().getInt(UIConstants.units_dailyrecord));
        composite.layout(true);
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        if (effortPreferenceChanged) {
            UIPlugin.getDefault().getPreferenceStore().setValue(UIConstants.units_estimate, effortTimeScaleCombo.getSelectionIndex());
            PreferenceChangeManager.notifyListeners(ChangedPreference.UNITS_ESTIMATE_CHANGED);
        }
        if (decimalPlacePreferenceChanged) {
            UIPlugin.getDefault().getPreferenceStore().setValue(UIConstants.units_estimate_decimalplaces, decimalPlacesCombo.getText());
            PreferenceChangeManager.notifyListeners(ChangedPreference.UNITS_ESTIMATE_CHANGED);
        }
        if (decimalDayPlacePreferenceChanged) {
            UIPlugin.getDefault().getPreferenceStore().setValue(UIConstants.units_estimate_decimaldayplaces, decimalDayPlacesCombo.getText());
            PreferenceChangeManager.notifyListeners(ChangedPreference.UNITS_ESTIMATE_CHANGED);
        }
        if (dailyRecordspreferenceChanged) {
            UIPlugin.getDefault().getPreferenceStore().setValue(UIConstants.units_dailyrecord, dailyRecordsTimeScaleCombo.getSelectionIndex());
            PreferenceChangeManager.notifyListeners(ChangedPreference.UNITS_DAILY_RECORD_CHANGED);
        }
        return true;
    }

    @Override
    protected void performApply() {
        super.performApply();
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        effortTimeScaleCombo.select(UIPlugin.getDefault().getPreferenceStore().getDefaultInt(UIConstants.units_estimate));
        dailyRecordsTimeScaleCombo.select(UIPlugin.getDefault().getPreferenceStore().getDefaultInt(UIConstants.units_dailyrecord));
    }

    private void setupTestHarness() {
        TestHarness.name(effortTimeScaleCombo, TestHarness.TIMEUNIT_PREFERENCE_EFFORTCOMBO);
        TestHarness.name(dailyRecordsTimeScaleCombo, TestHarness.TIMEUNIT_PREFERENCE_DAILYRECORDCOMBO);
    }
}
