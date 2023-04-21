package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.constants.Constants;

public class DummyGuiPlugin extends DummyPlugin implements SynchronizerGuiPlugin {

    private static DummyGuiPlugin INSTANCE;

    public static DummyGuiPlugin getInstance() {
        if (INSTANCE == null) INSTANCE = new DummyGuiPlugin();
        return INSTANCE;
    }

    private static String VERSION = "1.0";

    private DummyGuiPlugin() {
    }

    @Override
    public String getAccountLabel() {
        return null;
    }

    @Override
    public void loadPlugin() {
    }

    @Override
    public void installPlugin() {
    }

    @Override
    public void deletePlugin() {
    }

    @Override
    public int getPluginApiVersion() {
        return Constants.PLUGIN_API_VERSION;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public ConfigurationPanel getConfigurationPanel(ConfigurationGroup configuration, boolean welcome) {
        return new DummyConfigurationPanel(configuration, welcome);
    }

    @Override
    public String getLicenseUrl() {
        return null;
    }

    @Override
    public boolean needsLicense() {
        return false;
    }

    @Override
    public boolean checkLicense() throws SynchronizerLicenseException {
        return true;
    }
}
