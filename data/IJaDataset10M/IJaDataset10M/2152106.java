package com.google.code.sagetvaddons.customevents;

import gkusnick.sagetv.api.API;
import java.util.Map;
import java.util.Timer;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sage.SageTVPlugin;
import sage.SageTVPluginRegistry;

/**
 * Implementation class for the customevents plugin
 * @author dbattams
 *
 */
public final class Plugin implements SageTVPlugin {

    private static Logger LOG = null;

    private static Plugin INSTANCE = null;

    static synchronized Plugin get() {
        return INSTANCE;
    }

    private static final String OPT_PREFIX = "customevents/";

    static final String OPT_REC_SPACE_MIN_GBS = OPT_PREFIX + "minGB";

    private static final String OPT_REC_SPACE_MIN_GBS_DEFAULT = "25";

    private Timer scheduler;

    /**
	 * Constructor
	 * @param reg The SageTV plugin registry instance
	 */
    public Plugin(SageTVPluginRegistry reg) {
        synchronized (Plugin.class) {
            if (LOG == null) {
                PropertyConfigurator.configure("plugins/customevents/customevents.log4j.properties");
                LOG = Logger.getLogger(Plugin.class);
                LOG.debug("Logger created successfully!");
            }
            INSTANCE = this;
        }
        scheduler = new Timer(true);
    }

    public void destroy() {
    }

    public String getConfigHelpText(String arg0) {
        if (arg0.equals(OPT_REC_SPACE_MIN_GBS)) return "Fire a 'recording space low' event if the free space on your recording drives falls below this number (in GBs)."; else return "No help available.";
    }

    public String getConfigLabel(String arg0) {
        if (arg0.equals(OPT_REC_SPACE_MIN_GBS)) return "Minimum Recording Space (GBs)"; else return "No Label Available";
    }

    public String[] getConfigOptions(String arg0) {
        return null;
    }

    public String[] getConfigSettings() {
        return new String[] { OPT_REC_SPACE_MIN_GBS };
    }

    public int getConfigType(String arg0) {
        if (arg0.equals(OPT_REC_SPACE_MIN_GBS)) return SageTVPlugin.CONFIG_INTEGER;
        throw new IllegalArgumentException("Invalid argument! [" + arg0 + "]");
    }

    public String getConfigValue(String arg0) {
        String defaultVal;
        if (arg0.equals(OPT_REC_SPACE_MIN_GBS)) defaultVal = OPT_REC_SPACE_MIN_GBS_DEFAULT; else defaultVal = "";
        return API.apiNullUI.configuration.GetServerProperty(arg0, defaultVal);
    }

    public String[] getConfigValues(String arg0) {
        return null;
    }

    public void resetConfig() {
        setConfigValue(OPT_REC_SPACE_MIN_GBS, OPT_REC_SPACE_MIN_GBS_DEFAULT);
    }

    public void setConfigValue(String arg0, String arg1) {
        API.apiNullUI.configuration.SetServerProperty(arg0, arg1);
    }

    public void setConfigValues(String arg0, String[] arg1) {
    }

    public void start() {
        scheduler.schedule(new LowRecordingSpaceTimerTask(), 15000, 120000);
    }

    public void stop() {
        scheduler.cancel();
        scheduler = new Timer(true);
    }

    public void sageEvent(String arg0, @SuppressWarnings("rawtypes") Map arg1) {
        LOG.debug("Event received: " + arg0);
    }
}
