package org.vikamine.app.rcp.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vikamine.app.Resources;
import org.vikamine.app.rcp.Activator;

/**
 * The Class SubgroupTuningTablePage.
 * 
 * @author Alex Plischke
 */
public class SubgroupTuningTablePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * Instantiates a new SubgroupTuningTablePage.
     */
    public SubgroupTuningTablePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    /**
     * Instantiates a new SubgroupTuningTablePage.
     * 
     * @param style
     *            the style
     */
    public SubgroupTuningTablePage(int style) {
        super(style);
    }

    /**
     * Instantiates a new SubgroupTuningTablePage.
     * 
     * @param title
     *            the title
     * @param style
     *            the style
     */
    public SubgroupTuningTablePage(String title, int style) {
        super(title, style);
    }

    /**
     * Instantiates a new SubgroupTuningTablePage.
     * 
     * @param title
     *            the title
     * @param image
     *            the image
     * @param style
     *            the style
     */
    public SubgroupTuningTablePage(String title, ImageDescriptor image, int style) {
        super(title, image, style);
    }

    @Override
    public boolean performOk() {
        PreferencesSetter.getInstance().propagateSettings();
        return super.performOk();
    }

    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_POPSIZE, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.populationSize"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_TARGETPOP, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.p0"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_TARGETSG, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.p"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_RG, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.relativeGain"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_SENSITIVITY, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.sensitivity"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_SPECIFICITY, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.specificity"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_SIGNIFICANCE, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.significance"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_CHI2, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.chi2"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_MEANSG, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.sgMeanValue"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_MEANPOP, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.populationMeanValue"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_NORMMEANSG, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.sgNormalizedMeanValue"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_NORMMEANPOP, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.populationNormalizedMeanValue"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_VARSG, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.populationVariance"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_VARPOP, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.sgVariance"), getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.SGTT_LIFT, Resources.I18N.getString("vikamine.verbalization.subgroupStatInfoParameters.lift"), getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
