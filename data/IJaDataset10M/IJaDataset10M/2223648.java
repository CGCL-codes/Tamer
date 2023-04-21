package com.evolution.player.internal.jamendo.transfer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import com.evolution.player.core.EvolutionPlayerCore;
import com.evolution.player.core.media.MediaInfo;
import com.evolution.player.internal.jamendo.JamendoPlugin;
import com.evolution.player.internal.jamendo.JamendoUIUtils;
import com.evolution.player.internal.jamendo.core.JamendoUtils;
import com.evolution.player.internal.jamendo.preferences.JamendoPreferences;
import com.evolution.player.internal.jamendo.preferences.JamendoPreferences.JamendoPreference;
import com.evolution.player.jamendo.core.JamendoReader;

/**
 * @since 0.5
 */
public class JamendoDownloader {

    private final class DownloadThread extends Thread {

        private boolean fStopped;

        private final Random fRandom;

        public DownloadThread() {
            fRandom = new Random();
        }

        public void setStopped(boolean stopped) {
            fStopped = stopped;
        }

        @Override
        public void run() {
            while (!fStopped) {
                SafeRunner.run(new ISafeRunnable() {

                    public void run() throws Exception {
                        do {
                            try {
                                Thread.sleep(60 * 15 * 1000);
                            } catch (InterruptedException e) {
                            }
                        } while (!JamendoPreferences.isEnabled(JamendoPreference.AUTO_DOWNLOAD));
                        while (EvolutionPlayerCore.getDownloadManager().getDownloadCount() > 0) {
                            try {
                                Thread.sleep(1 * 60 * 1000);
                            } catch (InterruptedException e) {
                            }
                        }
                        try {
                            int nsuffel = Math.max(30 + (int) (Math.abs(fRandom.nextGaussian()) * 100.0), 200);
                            JamendoReader topReader = new JamendoReader(new URL("http://api.jamendo.com/get2/id/album/json/?order=rating_desc&n=1&nshuffle=" + nsuffel));
                            topReader.run();
                            String[] albumIds = topReader.getResult();
                            if (albumIds.length == 1) {
                                Thread.sleep(1000);
                                JamendoReader albumReader = new JamendoReader(new URL("http://api.jamendo.com/get2/id/track/json/?album_id=" + albumIds[0]));
                                albumReader.run();
                                String[] ids = albumReader.getResult();
                                if (ids.length > 0) {
                                    int n = fRandom.nextInt(ids.length);
                                    MediaInfo info = JamendoUtils.getInfo(ids[n]);
                                    if (info != null) JamendoUIUtils.download(ids[n], info, fDownloadFolder);
                                }
                            }
                        } catch (IOException e) {
                            JamendoPlugin.log(e);
                        }
                    }

                    public void handleException(Throwable exception) {
                        JamendoPlugin.log(exception);
                    }
                });
            }
        }
    }

    private DownloadThread fDownloadThread;

    private final File fDownloadFolder;

    public JamendoDownloader() {
        fDownloadFolder = EvolutionPlayerCore.getDownloadFolder();
    }

    public void start() {
        if (fDownloadThread != null) return;
        fDownloadThread = new DownloadThread();
        fDownloadThread.start();
    }

    public void stop() {
        if (fDownloadThread == null) return;
        fDownloadThread.setStopped(true);
        fDownloadThread = null;
    }
}
