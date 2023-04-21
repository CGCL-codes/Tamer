package thread;

import metadata.fingerprint.FingerprintExtractor;
import org.apache.log4j.Logger;
import ui.UIController;
import util.Constants;
import util.types.MP3File;
import util.types.MP3FileStatus;

public class FingerprintThread implements Runnable {

    final Logger logger = Logger.getLogger(FingerprintThread.class);

    static int threadCount = 0;

    UIController controller;

    MP3File file;

    public FingerprintThread(MP3File file, UIController controller) {
        logger.debug("FingerprintThread constructed");
        this.controller = controller;
        this.file = file;
    }

    public void run() {
        if (file.getStatus() != MP3FileStatus.NEW) {
            logger.debug("file not new, skipping!");
        } else {
            while (threadCount > Constants.MAX_FINGERPRINTTHREADS) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadCount++;
            logger.debug("FingerprintThread started");
            file = FingerprintExtractor.retrieveMP3FileData(file);
            file.setStatus(MP3FileStatus.FINGERPRINTING_FINISHED);
            controller.fireMP3Update(file);
            logger.debug("FingerprintThread finished");
            threadCount--;
        }
    }
}
