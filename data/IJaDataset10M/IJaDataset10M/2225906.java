package com.ivis.xprocess.ui.workflowdesigner.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.licensing.UnsupportedFeatureDialog;
import com.ivis.xprocess.ui.properties.WorkflowDesignerMessages;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.workflowdesigner.preferences.fontsAndColors.FontAndColorsSection;
import com.ivis.xprocess.util.FeatureUtil;
import com.ivis.xprocess.util.LicensingEnums.Feature;

public class WorkflowDesignerPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage, ISectionListener {

    private Section[] mySections;

    public WorkflowDesignerPreferencesPage() {
        initSections();
    }

    public WorkflowDesignerPreferencesPage(String title) {
        super(title);
        initSections();
    }

    public WorkflowDesignerPreferencesPage(String title, ImageDescriptor image) {
        super(title, image);
        initSections();
    }

    private void initSections() {
        mySections = new Section[] { new LayoutSection(), new FontAndColorsSection() };
        for (int i = 0; i < mySections.length; i++) {
            mySections[i].addSectionListener(this);
        }
    }

    protected Control createContents(Composite parent) {
        Composite pane = new Composite(parent, SWT.NONE);
        pane.setLayout(new GridLayout());
        if (checkLicense()) {
            for (int i = 0; i < mySections.length; i++) {
                Control sectionControl = mySections[i].createContents(pane);
                sectionControl.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
            }
        }
        return pane;
    }

    public void init(IWorkbench workbench) {
        IPreferenceStore store = getPreferenceStore();
        for (int i = 0; i < mySections.length; i++) {
            mySections[i].init(store);
        }
    }

    public boolean performOk() {
        if (!FeatureUtil.can(Feature.WORKFLOW_DESIGNER)) {
            return true;
        }
        IPreferenceStore store = getPreferenceStore();
        for (int i = 0; i < mySections.length; i++) {
            if (!mySections[i].performOk(store)) {
                return false;
            }
        }
        UIPlugin.getDefault().savePluginPreferences();
        return true;
    }

    protected void performDefaults() {
        if (!FeatureUtil.can(Feature.WORKFLOW_DESIGNER)) {
            return;
        }
        IPreferenceStore store = getPreferenceStore();
        for (int i = 0; i < mySections.length; i++) {
            mySections[i].performDefaults(store);
        }
        super.performDefaults();
    }

    protected IPreferenceStore doGetPreferenceStore() {
        return UIPlugin.getDefault().getPreferenceStore();
    }

    public void sectionChanged(boolean isSectionValid) {
        for (int i = 0; i < mySections.length; i++) {
            boolean sectionIsValid = mySections[i].isValid();
            if (!sectionIsValid) {
                setValid(false);
                setErrorMessage(mySections[i].getError());
                return;
            }
        }
        setValid(true);
        setErrorMessage(null);
    }

    private boolean checkLicense() {
        if (!FeatureUtil.can(Feature.WORKFLOW_DESIGNER)) {
            UnsupportedFeatureDialog ufd = new UnsupportedFeatureDialog(ViewUtil.getCurrentShell(), WorkflowDesignerMessages.WorkflowDesignerFeatureName);
            ufd.open();
            return FeatureUtil.can(Feature.WORKFLOW_DESIGNER);
        }
        return true;
    }
}
