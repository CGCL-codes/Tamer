package de.fzi.herakles.protege.configuration;

import org.protege.editor.core.ui.workspace.Workspace;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import de.fzi.herakles.commons.herakles.HeraklesManager;
import de.fzi.herakles.commons.configuration.FunctionConfiguration;

/**
 * The view component  of execution strategy configuration(using reasoner property)
 * @author Xu
 *
 */
public class ExecutionConfigurationView extends AbstractOWLViewComponent {

    @SuppressWarnings("unused")
    private Workspace workspace;

    @SuppressWarnings("unused")
    private FunctionConfiguration config;

    private TaskSelectionConfiguration taskSelectionConfiguration;

    private ABoxTboxRatioConfiguration ratioSelectionConfiguration;

    private AnytimeConfiguration anytimeConfiguration;

    /**
	 * 
	 */
    private static final long serialVersionUID = 4998693079187075050L;

    private static ExecutionConfigurationView instance = null;

    public ExecutionConfigurationView() {
        workspace = getWorkspace();
        instance = this;
        config = HeraklesManager.getFunctionConfiguration();
    }

    @Override
    protected void disposeOWLView() {
    }

    @Override
    protected void initialiseOWLView() throws Exception {
        taskSelectionConfiguration = new TaskSelectionConfiguration(this);
        taskSelectionConfiguration.setActive(false);
        ratioSelectionConfiguration = new ABoxTboxRatioConfiguration(this);
        ratioSelectionConfiguration.setActive(false);
        anytimeConfiguration = new AnytimeConfiguration(this);
        anytimeConfiguration.setActive(false);
    }

    /**
	 * show the configuration panel of selection execution strategy
	 */
    public void showSelectionStrategyConfiguration() {
        taskSelectionConfiguration.showPanel();
        taskSelectionConfiguration.setActive(true);
        ratioSelectionConfiguration.setActive(false);
        anytimeConfiguration.setActive(false);
    }

    public void showABoxtBoxRatioSelectionStrategyConfiguration() {
        ratioSelectionConfiguration.showPanel();
        taskSelectionConfiguration.setActive(false);
        ratioSelectionConfiguration.setActive(true);
        anytimeConfiguration.setActive(false);
    }

    public void showAnytimeStrategyConfiguration() {
        anytimeConfiguration.showPanel();
        taskSelectionConfiguration.setActive(false);
        ratioSelectionConfiguration.setActive(false);
        anytimeConfiguration.setActive(true);
    }

    /**
	 * remove all the components. 
	 */
    public void clear() {
        setVisible(false);
        removeAll();
        updateUI();
        invalidate();
        validate();
    }

    /**
	 * get the instance of the View Object
	 * @return the view instance
	 */
    public static synchronized ExecutionConfigurationView getInstance() {
        if (instance == null) {
            instance = new ExecutionConfigurationView();
            try {
                instance.initialise();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
	 * update the reasoner information
	 */
    public void updateReasoner() {
        taskSelectionConfiguration.updateReasoner();
        if (taskSelectionConfiguration.isActive()) taskSelectionConfiguration.showPanel(); else taskSelectionConfiguration.clear();
        ratioSelectionConfiguration.updateReasoner();
        if (ratioSelectionConfiguration.isActive()) ratioSelectionConfiguration.showPanel(); else ratioSelectionConfiguration.clear();
        anytimeConfiguration.updateReasoner();
        anytimeConfiguration.updateReasoner();
        if (anytimeConfiguration.isActive()) anytimeConfiguration.showPanel(); else anytimeConfiguration.clear();
    }
}
