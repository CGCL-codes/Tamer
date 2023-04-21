package de.ueppste.ljb.client;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ueppste.ljb.share.Mp3File;
import de.ueppste.ljb.share.User;

public class LJBMP3Upload extends Thread {

    private File[] files;

    private LJBServer serverCon;

    private boolean threadStarted = false;

    private User user;

    private LJBView view;

    private static Logger logger = Logger.getLogger(LJBMP3Upload.class.getName());

    public LJBMP3Upload(File[] files, LJBServer serverCon, User user, LJBView view) {
        this.files = files;
        this.user = user;
        this.view = view;
        this.serverCon = serverCon;
    }

    public void startThread() {
        if (!this.threadStarted) {
            this.start();
            this.threadStarted = false;
        }
    }

    public void run() {
        int count = 0;
        this.view.showUloadPanel();
        for (File file : files) {
            if (!file.exists()) continue;
            count++;
            Mp3File mp3File = new Mp3File();
            try {
                mp3File.readIn(file.getAbsolutePath());
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
                continue;
            }
            this.view.setUloadFileCount(count, files.length);
            this.serverCon.addMp3ToServer(mp3File, user);
        }
        this.view.hideUloadPanel();
    }
}
