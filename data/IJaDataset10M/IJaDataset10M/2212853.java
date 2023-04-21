package de.wadndadn.dailydilbert.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import de.wadndadn.dailydilbert.DailyDilbertPlugin;

/**
 * This initializer is used to initialize default preference values for
 * {@link DailyDilbertPreferencePage}.
 * 
 * @author SchubertCh
 */
public final class DailyDilbertPreferenceInitializer extends AbstractPreferenceInitializer {

    /**
     * Default constructor.
     */
    public DailyDilbertPreferenceInitializer() {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = DailyDilbertPlugin.getDefault().getPreferenceStore();
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_ENABLE_AUTODOWNLOAD, DailyDilbertPreferenceConstants.ENABLE_AUTODOWNLOAD);
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_ENABLE_UPDATE, DailyDilbertPreferenceConstants.ENABLE_UPDATE);
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_UPDATE_INTERVAL, DailyDilbertPreferenceConstants.UPDATE_INTERVAL);
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_SELECTED_FEED, DailyDilbertPreferenceConstants.FEED_DAILY_STRIPS);
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_FEED_DAILY_STRIPS, DailyDilbertPreferenceConstants.FEED_DAILY_STRIPS);
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_FEED_MOST_POPULAR, DailyDilbertPreferenceConstants.FEED_MOST_POPULAR);
        store.setDefault(DailyDilbertPreferenceConstants.PREFERENCE_DILBERT_HOMEPAGE, DailyDilbertPreferenceConstants.DILBERT_HOMEPAGE);
    }
}
