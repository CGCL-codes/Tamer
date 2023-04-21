package net.sourceforge.nattable.typeconfig.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

public class StyleConfigPersistor {

    List<IStylePreferencesStoreHandler> persistors = new ArrayList<IStylePreferencesStoreHandler>();

    PreferencesStore store = (PreferencesStore) new ConfigStoreFactory().userRoot();

    public void addStylePersistor(IStylePreferencesStoreHandler persistor) {
        persistors.add(persistor);
    }

    public void restoreConfigs(InputStream source) throws IOException, InvalidPreferencesFormatException {
        synchronized (store) {
            store.loadPreferences(source);
            for (IStylePreferencesStoreHandler persistor : persistors) {
                persistor.restoreConfigs(store);
            }
        }
    }

    /**
	 * This method will delegate to each {@link IStylePreferencesStoreHandler} it
	 * contains. Once the store is loaded, then it will serialize using the
	 * {@link Preferences} class.
	 * 
	 * @param source
	 * @throws BackingStoreException
	 * @throws IOException
	 */
    public void saveConfigs(OutputStream target) throws IOException {
        synchronized (store) {
            for (IStylePreferencesStoreHandler persistor : persistors) {
                persistor.persisteConfigs(store);
            }
            try {
                store.exportSubtree(target);
            } catch (BackingStoreException e) {
                throw new IOException("Unable to serialize to stream: " + e.getMessage());
            } finally {
                if (target != null) {
                    target.close();
                }
            }
        }
    }
}
