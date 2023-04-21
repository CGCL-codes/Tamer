package bgpanalyzer.functions.configuration;

import bgpanalyzer.base.ControllerBase;
import bgpanalyzer.main.MainController;

public class ConfigurationController implements ControllerBase {

    private ConfigurationView configurationView = null;

    private MainController mainController = null;

    /** Creates a new instance of ConfigurationController */
    public ConfigurationController(MainController mainController) {
        this.mainController = mainController;
    }

    public void clearViews() {
        configurationView = null;
    }

    public void showConfigurationView() {
        configurationView = new ConfigurationView(this);
        configurationView.setVisible(true);
    }

    public void close() {
        configurationView.setVisible(false);
    }
}
