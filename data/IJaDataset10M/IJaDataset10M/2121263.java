package org.pubcurator.analyzers.metamap.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.metamap.uima.annotators.BasicMetaMapAnnotator;
import org.pubcurator.analyzers.metamap.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public PreferenceInitializer() {
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(BasicMetaMapAnnotator.MODE_PARAMETER, BasicMetaMapAnnotator.API_MODE);
        store.setDefault(BasicMetaMapAnnotator.METAMAP_BASEDIR_PARAMETER, "/opt/public_mm");
        store.setDefault(BasicMetaMapAnnotator.METAMAP_OPTIONS_PARAMETER, "");
        store.setDefault(BasicMetaMapAnnotator.TEMP_DIR_PARAMETER, "");
        store.setDefault(BasicMetaMapAnnotator.SKR_USERNAME_PARAMETER, "");
        store.setDefault(BasicMetaMapAnnotator.SKR_PASSWORD_PARAMETER, "");
        store.setDefault(BasicMetaMapAnnotator.SKR_EMAIL_PARAMETER, "");
        store.setDefault(BasicMetaMapAnnotator.API_HOSTNAME_PARAMETER, "");
        store.setDefault(BasicMetaMapAnnotator.API_PORT_PARAMETER, "");
    }
}
