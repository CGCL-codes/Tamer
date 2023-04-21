package org.designerator.video;

import org.designerator.video.views.VideoView;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class Initializer extends AbstractPreferenceInitializer {

    public static final String USER = "Youtubeuser";

    public static final String PASS = "YoutubePass";

    public static final String VIDEOPLAYER = "Videoplayer";

    public static final String FFMPEGPATH = "ffmpegpath";

    public Initializer() {
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = VideoPlugin.getDefault().getPreferenceStore();
        store.setDefault(PASS, "");
        store.setDefault(USER, "");
        store.setDefault(FFMPEGPATH, "");
        store.setDefault(VIDEOPLAYER, VideoView.WMP);
    }
}
