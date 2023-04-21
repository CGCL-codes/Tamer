package org.eclipse.mylyn.internal.java.ui.wizards;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.mylyn.context.ui.ContextUiPlugin;
import org.eclipse.mylyn.internal.context.ui.ContextUiImages;
import org.eclipse.mylyn.internal.context.ui.ContextUiPrefContstants;
import org.eclipse.mylyn.internal.java.ui.JavaUiBridgePlugin;
import org.eclipse.mylyn.internal.java.ui.JavaUiUtil;
import org.eclipse.mylyn.internal.tasks.ui.views.TaskListView;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Mik Kersten
 */
public class RecommendedPreferencesWizard extends Wizard implements INewWizard {

    private RecommendedPreferencesWizardPage preferencePage;

    public static final String MYLYN_FIRST_RUN = "org.eclipse.mylyn.ui.first.run.0_4_9";

    private static final String DEFAULT_FOLDING_PROVIDER = "org.eclipse.jdt.ui.text.defaultFoldingProvider";

    private IPreferenceStore javaPrefs = JavaPlugin.getDefault().getPreferenceStore();

    public void init() {
        setDefaultPageImageDescriptor(ContextUiImages.MYLYN);
        setWindowTitle("Recommended Preferences");
        super.setDefaultPageImageDescriptor(JavaUiBridgePlugin.imageDescriptorFromPlugin(JavaUiBridgePlugin.PLUGIN_ID, "icons/wizban/banner-prefs.gif"));
        preferencePage = new RecommendedPreferencesWizardPage("Mylyn Java Preference Settings");
    }

    public RecommendedPreferencesWizard() {
        super();
        init();
    }

    public RecommendedPreferencesWizard(String htmlDocs) {
        super();
        init();
    }

    @Override
    public boolean performFinish() {
        setPreferences();
        if (preferencePage.isOpenTaskList()) {
            TaskListView.openInActivePerspective();
        }
        return true;
    }

    private void setPreferences() {
        boolean mylynContentAssist = preferencePage.isMylynContentAssistDefault();
        JavaUiUtil.installContentAssist(javaPrefs, mylynContentAssist);
        if (preferencePage.isAutoFolding()) {
            ContextUiPlugin.getDefault().getPreferenceStore().setValue(ContextUiPrefContstants.ACTIVE_FOLDING_ENABLED, true);
            javaPrefs.setValue(PreferenceConstants.EDITOR_FOLDING_ENABLED, true);
            javaPrefs.setValue(PreferenceConstants.EDITOR_FOLDING_PROVIDER, DEFAULT_FOLDING_PROVIDER);
        } else {
            ContextUiPlugin.getDefault().getPreferenceStore().setValue(ContextUiPrefContstants.ACTIVE_FOLDING_ENABLED, false);
        }
    }

    @Override
    public void addPages() {
        addPage(preferencePage);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
