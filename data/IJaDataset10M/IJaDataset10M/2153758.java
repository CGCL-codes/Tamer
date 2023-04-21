package phex.prefs.core;

import phex.common.Environment;
import phex.common.EnvironmentConstants;
import phex.common.PhexVersion;
import phex.prefs.api.Preferences;
import phex.utils.StringUtils;

public class PhexCorePrefs extends Preferences {

    protected static final PhexCorePrefs instance;

    static {
        instance = new PhexCorePrefs();
    }

    protected PhexCorePrefs() {
        super(Environment.getInstance().getPhexConfigFile(EnvironmentConstants.CORE_PREFERENCES_FILE_NAME));
    }

    public static void init() {
        instance.load();
        instance.updatePreferences();
        StatisticPrefs.TotalStartupCounter.set(Integer.valueOf(StatisticPrefs.TotalStartupCounter.get().intValue() + 1));
    }

    public static void save(boolean force) {
        if (force) {
            instance.saveRequiredNotify();
        }
        instance.save();
    }

    public void updatePreferences() {
        if (StringUtils.isEmpty(UpdatePrefs.RunningBuildNumber.get())) {
        }
        UpdatePrefs.RunningBuildNumber.set(PhexVersion.getBuild());
        UpdatePrefs.RunningPhexVersion.set(PhexVersion.getVersion());
    }
}
