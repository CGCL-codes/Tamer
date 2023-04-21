package openbiomind.gui.wizards;

import openbiomind.gui.data.AbstractTaskData;
import openbiomind.gui.data.GraphFeaturesTaskData;
import openbiomind.gui.main.GraphvizHelper;

/**
 * The class UtilityComputerToGraphFeaturesWizard.
 * 
 * @author bsanghvi
 * @since Aug 10, 2008
 * @version Aug 18, 2008
 */
public class UtilityComputerToGraphFeaturesWizard extends AbstractTaskWizard {

    /** The utility computer wizard page. */
    private final UtilityComputerWizardPage UTILITY_COMPUTER_WIZ_PAGE = new UtilityComputerWizardPage();

    /** The graph features wizard page. */
    private final GraphFeaturesWizardPage GRAPH_FEATURES_WIZ_PAGE = new GraphFeaturesWizardPage();

    /** The graph features task data. */
    private GraphFeaturesTaskData graphFeaturesTaskData = null;

    /** The Graphviz helper. */
    private GraphvizHelper graphvizHelper = null;

    /**
    * Instantiates a new graph features from dataset transformer wizard.
    */
    public UtilityComputerToGraphFeaturesWizard() {
        super(Messages.UtilComp_GraFeature_Wiz_Title);
    }

    @Override
    public void addPages() {
        addPage(this.UTILITY_COMPUTER_WIZ_PAGE);
        addPage(this.GRAPH_FEATURES_WIZ_PAGE);
    }

    @Override
    protected AbstractTaskData[] getTaskData() {
        if (this.GRAPH_FEATURES_WIZ_PAGE.isCurrentPage()) {
            return new AbstractTaskData[] { this.UTILITY_COMPUTER_WIZ_PAGE.prepareTaskData(), getGraphFeaturesTaskData() };
        } else {
            return new AbstractTaskData[] { this.UTILITY_COMPUTER_WIZ_PAGE.prepareTaskData() };
        }
    }

    @Override
    protected AbstractTaskWizardPage getFirstWizardPage() {
        return this.UTILITY_COMPUTER_WIZ_PAGE;
    }

    /**
    * Gets the graph features task data.
    * 
    * @return the graph features task data
    */
    private GraphFeaturesTaskData getGraphFeaturesTaskData() {
        if (this.graphFeaturesTaskData == null) {
            this.graphFeaturesTaskData = this.GRAPH_FEATURES_WIZ_PAGE.prepareTaskData();
        }
        return this.graphFeaturesTaskData;
    }

    /**
    * Gets the graphviz helper.
    * 
    * @return the graphviz helper
    */
    private GraphvizHelper getGraphvizHelper() {
        if (this.graphvizHelper == null) {
            this.graphvizHelper = new GraphvizHelper(this.GRAPH_FEATURES_WIZ_PAGE.getOutputFile());
        }
        return this.graphvizHelper;
    }

    @Override
    protected Process getPostSuccessfulExecutionProcess() {
        final Process dotProcess = getGraphvizHelper().createDotProcess();
        if (dotProcess != null) {
            getGraphFeaturesTaskData().setGraphImagePath(getGraphvizHelper().getOutputFilePath());
            return dotProcess;
        } else {
            return super.getPostSuccessfulExecutionProcess();
        }
    }
}
