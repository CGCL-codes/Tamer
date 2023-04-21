package org.jampa.preferences;

import java.net.Proxy;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Util;
import org.jampa.Activator;
import org.jampa.controllers.ui.ToolbarController.ToolBarSize;
import org.jampa.engine.PlaybackEngine;
import org.jampa.engine.gstreamer.GStreamerOutputType;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_LIBRARY_PATH, "");
        store.setDefault(PreferenceConstants.P_MPLAYER_PATH, "");
        store.setDefault(PreferenceConstants.PLAYBACK_ENGINE, PlaybackEngine.GSTREAMER.toString());
        store.setDefault(PreferenceConstants.USE_STATISTICS, true);
        store.setDefault(PreferenceConstants.MPLAYER_FORCE_PROXY_SETTINGS, false);
        store.setDefault(PreferenceConstants.MPLAYER_ADDITIONAL_OPTIONS, "");
        store.setDefault(PreferenceConstants.MPLAYER_USE_VOLUME_NORMALIZATION, false);
        if (Util.isWindows()) {
            store.setDefault(PreferenceConstants.MPLAYER_USE_VOLUME_COMMAND, true);
        } else {
            store.setDefault(PreferenceConstants.MPLAYER_USE_VOLUME_COMMAND, false);
        }
        store.setDefault(PreferenceConstants.MPLAYER_JOB_LAUNCH_MODE, true);
        store.setDefault(PreferenceConstants.GSTREAMER_OUPUT_TYPE, GStreamerOutputType.getValueOf(GStreamerOutputType.AUTODETECT));
        store.setDefault(PreferenceConstants.GSTREAMER_CUSTOM_SINK, "");
        store.setDefault(PreferenceConstants.TRAY_SHOW_IN_TRAY, false);
        store.setDefault(PreferenceConstants.TRAY_SHOW_REVERSED, false);
        store.setDefault(PreferenceConstants.TRAY_SHOW_PREV_BUTTON, true);
        store.setDefault(PreferenceConstants.TRAY_SHOW_STOP_BUTTON, true);
        store.setDefault(PreferenceConstants.TRAY_SHOW_PAUSE_BUTTON, true);
        store.setDefault(PreferenceConstants.TRAY_SHOW_NEXT_BUTTON, true);
        store.setDefault(PreferenceConstants.TRAY_RADIO_SHOW_FAVORITES, false);
        store.setDefault(PreferenceConstants.PLAY_MODE, 0);
        store.setDefault(PreferenceConstants.PLAYBACK_VOLUME, 70);
        store.setDefault(PreferenceConstants.PLAYLISTSVIEW_DOUBLECLICK_ACTION, "0");
        store.setDefault(PreferenceConstants.DISKVIEW_FOLDER_DOUBLECLICK_ACTION, "0");
        store.setDefault(PreferenceConstants.DISKVIEW_DEFAULT_DOUBLECLICK_ACTION, "0");
        store.setDefault(PreferenceConstants.LIBRARYVIEW_FOLDER_DOUBLECLICK_ACTION, "0");
        store.setDefault(PreferenceConstants.LIBRARYVIEW_DEFAULT_DOUBLECLICK_ACTION, "0");
        store.setDefault(PreferenceConstants.EQUALIZER_ACTIVATED, false);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND0, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND1, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND2, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND3, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND4, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND5, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND6, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND7, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND8, 0.0);
        store.setDefault(PreferenceConstants.EQUALIZER_BAND9, 0.0);
        store.setDefault(PreferenceConstants.AUTOMATIC_LIBRARY_UPDATE, true);
        store.setDefault(PreferenceConstants.LIBRARY_DISPLAY_MODE, "0");
        store.setDefault(PreferenceConstants.LIBRARY_SORT_MODE, "0");
        store.setDefault(PreferenceConstants.DISKVIEW_SORT_MODE, "0");
        store.setDefault(PreferenceConstants.DISKVIEW_DISPLAY_MODE, "0");
        store.setDefault(PreferenceConstants.DISKVIEW_MERGE_FOLDER, true);
        store.setDefault(PreferenceConstants.PLAYERVIEW_SHOW_IN_TOOLBAR, true);
        store.setDefault(PreferenceConstants.PLAYERVIEW_TOOLBAR_SIZE, ToolBarSize.MEDIUM.getValue());
        store.setDefault(PreferenceConstants.PLAYERVIEW_SEEK_STEP, "10");
        store.setDefault(PreferenceConstants.STATISTICVIEW_NB_MAX_ITEMS, "250");
        store.setDefault(PreferenceConstants.RADIOVIEW_SORT_MODE, "0");
        store.setDefault(PreferenceConstants.RADIOVIEW_SHOW_FAVORITES, false);
        if (Util.isWindows()) {
            store.setDefault(PreferenceConstants.PLAYERVIEW_USE_NATIVE_SLIDER, false);
        } else {
            store.setDefault(PreferenceConstants.PLAYERVIEW_USE_NATIVE_SLIDER, true);
        }
        store.setDefault(PreferenceConstants.DISPLAY_DATE_FORMAT, "MM-dd-yyyy");
        store.setDefault(PreferenceConstants.DISPLAY_TIME_FORMAT, "HH:mm:ss");
        store.setDefault(PreferenceConstants.OSD_SHOW, false);
        store.setDefault(PreferenceConstants.OSD_ONLY_SHOW_MINIMIZED, false);
        store.setDefault(PreferenceConstants.OSD_DELAY, 3000);
        store.setDefault(PreferenceConstants.OSD_USE_ALPHA, true);
        store.setDefault(PreferenceConstants.OSD_SHOW_ON_PODCAST_UPDATE, true);
        store.setDefault(PreferenceConstants.OSD_SHOW_ON_PODCAST_DOWNLOAD_END, true);
        store.setDefault(PreferenceConstants.SEARCH_SCOPE_TITLE, true);
        store.setDefault(PreferenceConstants.SEARCH_SCOPE_ARTIST, true);
        store.setDefault(PreferenceConstants.SEARCH_SCOPE_ALBUM, true);
        store.setDefault(PreferenceConstants.SEARCH_SCOPE_GENRE, true);
        store.setDefault(PreferenceConstants.SEARCH_SCOPE_YEAR, false);
        store.setDefault(PreferenceConstants.SEARCH_OPTION_CASESENSITIVE, false);
        store.setDefault(PreferenceConstants.SEARCH_OPTION_WHOLEWORD, false);
        store.setDefault(PreferenceConstants.VERSION_CHECK_AT_STARTUP, true);
        store.setDefault(PreferenceConstants.VERSION_CHECK_URL, "http://jampa.sourceforge.net/");
        store.setDefault(PreferenceConstants.VERSION_UPDATE_URL, "http://sourceforge.net/projects/jampa/files/");
        store.setDefault(PreferenceConstants.VERSION_CHECK_FILE, "JAMPA-LASTEST");
        store.setDefault(PreferenceConstants.VERSION_CHECK_MIN_TIME, 10);
        store.setDefault(PreferenceConstants.VERSION_CHECK_MAX_TIME, 111);
        store.setDefault(PreferenceConstants.PODCAST_UPDATE_AT_STARTUP, true);
        store.setDefault(PreferenceConstants.PODCAST_AUTOMATIC_UPDATE, true);
        store.setDefault(PreferenceConstants.PODCAST_AUTOMATIC_UPDATE_INTERVAL, 60);
        store.setDefault(PreferenceConstants.PODCAST_DOWNLOAD_BEFORE_PLAY, false);
        store.setDefault(PreferenceConstants.PODCAST_PLAY_AFTER_DOWNLOAD, true);
        store.setDefault(PreferenceConstants.PODCAST_CLEAN_DOWNLOAD_CACHE_ON_UPDATE, true);
        store.setDefault(PreferenceConstants.PODCAST_DOWNLOAD_BUFFER_SIZE, 1024);
        store.setDefault(PreferenceConstants.PODCAST_DOWNLOAD_DISPLAY_BUFFER_SIZE, 8196);
        store.setDefault(PreferenceConstants.PROXY_TYPE, Proxy.Type.DIRECT.toString());
        store.setDefault(PreferenceConstants.PROXY_URL, "");
        store.setDefault(PreferenceConstants.PROXY_PORT, 80);
        store.setDefault(PreferenceConstants.PROXY_USERNAME, "");
        store.setDefault(PreferenceConstants.PROXY_PASSWORD, "");
        store.setDefault(PreferenceConstants.RADIO_PLAYLIST_PATTERN_LIST, ".\\.asx$;.\\.m3u$;.\\.ram$");
    }
}
