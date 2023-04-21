package org.vikamine.app.rcp.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vikamine.app.rcp.Activator;
import org.vikamine.app.rcp.control.StatisticComponentPreferences;
import org.vikamine.kernel.statistics.StatisticComponent;

/**
 * The Class CurrentSubgroupPage.
 * 
 * @author Alex Plischke
 */
public class CurrentSubgroupPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /** The Constant ID. */
    public static final String ID = "org.vikamine.app.rcp.preferences.CurrentSubgroupPage";

    /**
     * Instantiates a new current subgroup page.
     */
    public CurrentSubgroupPage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    /**
     * Instantiates a new CurrentSubgroupPage.
     * 
     * @param style
     *            the style
     */
    public CurrentSubgroupPage(int style) {
        super(style);
    }

    /**
     * Instantiates a new CurrentSubgroupPage.
     * 
     * @param title
     *            the title
     * @param style
     *            the style
     */
    public CurrentSubgroupPage(String title, int style) {
        super(title, style);
    }

    /**
     * Instantiates a new CurrentSubgroupPage.
     * 
     * @param title
     *            the title
     * @param image
     *            the image
     * @param style
     *            the style
     */
    public CurrentSubgroupPage(String title, ImageDescriptor image, int style) {
        super(title, image, style);
    }

    @Override
    public boolean performOk() {
        PreferencesSetter.getInstance().propagateSettings();
        return super.performOk();
    }

    @Override
    protected void createFieldEditors() {
        StatisticComponentPreferences scp = StatisticComponentPreferences.CURRENT_SG;
        for (StatisticComponent sc : StatisticComponent.createAllStatisticComponents()) {
            addField(new BooleanFieldEditor(scp.getId() + "." + sc.getDescription(), sc.getDescription(), getFieldEditorParent()));
        }
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
