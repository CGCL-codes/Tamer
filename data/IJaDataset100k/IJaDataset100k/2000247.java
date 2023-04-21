package net.sourceforge.eclipsetrader.yahoo.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.sourceforge.eclipsetrader.yahoo.preferences.messages";

    public static String FrenchNewsPreferencesPage_GetSubscribersOnly;

    public static String FrenchNewsPreferencesPage_ProviderColumnName;

    public static String ItalianNewsPreferencesPage_ProviderColumnName;

    public static String NewsPreferencesPage_GetSubscribersOnly;

    public static String NewsPreferencesPage_ProviderColumnName;

    public static String PluginPreferencesPage_SnapshotUpdate;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
