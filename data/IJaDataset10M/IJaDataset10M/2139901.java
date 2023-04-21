package it.jwallpaper.config;

import it.jwallpaper.JawcContext;
import java.util.List;
import java.util.Set;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JawcConfiguration extends AbstractConfiguration implements ConfigurationInterface {

    public static final String CHANGED_CHANGEEVERYMINUTES = "CHANGED_CHANGEEVERYMINUTES";

    public enum PluginExecuteMode {

        SEQUENTIAL, RANDOM
    }

    ;

    public static final String CONFIG_FILE_NAME = "jawc";

    private boolean changeOnStartup = true;

    private boolean changeOnInterval = true;

    private int changeEveryMinutes = 15;

    private int checkForNewVersionEvery = 7;

    private boolean displayBalloon = true;

    private PluginExecuteMode pluginExecuteMode = PluginExecuteMode.RANDOM;

    public PluginExecuteMode getPluginExecuteMode() {
        return pluginExecuteMode;
    }

    public void setPluginExecuteMode(PluginExecuteMode pluginExecuteMode) {
        this.pluginExecuteMode = pluginExecuteMode;
    }

    public boolean isChangeOnStartup() {
        return changeOnStartup;
    }

    public void setChangeOnStartup(boolean changeOnStartup) {
        this.changeOnStartup = changeOnStartup;
    }

    public boolean isChangeOnInterval() {
        return changeOnInterval;
    }

    public void setChangeOnInterval(boolean changeOnInterval) {
        this.changeOnInterval = changeOnInterval;
    }

    public int getChangeEveryMinutes() {
        return changeEveryMinutes;
    }

    public void setChangeEveryMinutes(int changeEveryMinutes) {
        boolean valueChanged = (this.changeEveryMinutes != changeEveryMinutes);
        this.changeEveryMinutes = changeEveryMinutes;
        if (valueChanged) {
            throwConfigurationChangedEvent(CHANGED_CHANGEEVERYMINUTES);
        }
    }

    public boolean isDisplayBalloon() {
        return displayBalloon;
    }

    public void setDisplayBalloon(boolean displayBalloon) {
        this.displayBalloon = displayBalloon;
    }

    public int getCheckForNewVersionEvery() {
        return checkForNewVersionEvery;
    }

    public void setCheckForNewVersionEvery(int checkForNewVersionEvery) {
        this.checkForNewVersionEvery = checkForNewVersionEvery;
    }

    @SuppressWarnings("unchecked")
    public void setConfiguration(XMLConfiguration configuration) {
        changeOnStartup = configuration.getBoolean("jawc.changeOnStartup", true);
        changeOnInterval = configuration.getBoolean("jawc.changeOnInterval", true);
        displayBalloon = configuration.getBoolean("jawc.displayBalloon", true);
        changeEveryMinutes = configuration.getInt("jawc.changeEveryMinutes", 15);
        checkForNewVersionEvery = configuration.getInt("jawc.checkForNewVersionEvery", 7);
        List<String> enabledPlugins = configuration.getList("jawc.enabledPlugins");
        JawcContext.getInstance().getPluginEngine().setEnabledPlugins(enabledPlugins);
        pluginExecuteMode = Enum.valueOf(PluginExecuteMode.class, configuration.getString("jawc.pluginExecutionMode", "RANDOM"));
    }

    public XMLConfiguration getConfiguration() {
        XMLConfiguration configuration = new XMLConfiguration();
        configuration.setProperty("jawc.changeOnStartup", changeOnStartup);
        configuration.setProperty("jawc.changeOnInterval", changeOnInterval);
        configuration.setProperty("jawc.displayBaloon", displayBalloon);
        configuration.setProperty("jawc.changeEveryMinutes", changeEveryMinutes);
        configuration.setProperty("jawc.checkForNewVersionEvery", checkForNewVersionEvery);
        Set<String> enabledPlugins = JawcContext.getInstance().getPluginEngine().getEnabledPlugins();
        for (String pluginName : enabledPlugins) {
            configuration.addProperty("jawc.enabledPlugins(-1)", pluginName);
        }
        configuration.setProperty("jawc.pluginExecutionMode", pluginExecuteMode.toString());
        return configuration;
    }

    public String toString() {
        return new ToStringBuilder(this).append("changeOnStartup", changeOnStartup).append("changeOnInterval", changeOnInterval).append("changeEveryMinutes", changeEveryMinutes).append("checkForNewVersionEvery", checkForNewVersionEvery).append("displayBalloon", displayBalloon).append("pluginExecuteMode", pluginExecuteMode).toString();
    }
}
