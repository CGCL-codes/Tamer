package phex.gui.prefs;

import phex.prefs.api.PreferencesFactory;
import phex.prefs.api.Setting;

public class PrivacyPrefs extends PhexGuiPrefs {

    public static final Setting<Boolean> ClearKeywordSearchHistoryDialog;

    public static final Setting<Boolean> ClearBrowseHostHistoryDialog;

    public static final Setting<Boolean> ClearConnectToHistoryDialog;

    public static final Setting<Boolean> ClearLibrarySearchCountDialog;

    public static final Setting<Boolean> ClearLibraryUploadCountDialog;

    public static final Setting<Boolean> ClearSecurityRuleTriggerCountDialog;

    static {
        ClearKeywordSearchHistoryDialog = PreferencesFactory.createBoolSetting("Interface.ShowKeywordSearchHistoryDialog", false, instance);
        ClearBrowseHostHistoryDialog = PreferencesFactory.createBoolSetting("Interface.ShowBrowseHostHistoryDialog", false, instance);
        ClearConnectToHistoryDialog = PreferencesFactory.createBoolSetting("Interface.ShowConnectToHistoryDialog", false, instance);
        ClearLibrarySearchCountDialog = PreferencesFactory.createBoolSetting("Interface.ShowLibrarySearchCountDialog", false, instance);
        ClearLibraryUploadCountDialog = PreferencesFactory.createBoolSetting("Interface.ShowLibraryUploadCountDialog", false, instance);
        ClearSecurityRuleTriggerCountDialog = PreferencesFactory.createBoolSetting("Interface.ShowSecurityRuleTriggerCoutDialog", false, instance);
    }
}
