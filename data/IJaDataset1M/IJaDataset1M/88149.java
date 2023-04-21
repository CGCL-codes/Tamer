package net.sf.myway.hibernate.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import net.sf.myway.hibernate.HibernateActivator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = HibernateActivator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        store.setDefault(PreferenceConstants.P_DRIVER, "org.postgresql.Driver");
        store.setDefault(PreferenceConstants.P_URL, "jdbc:postgresql:myway");
    }
}
