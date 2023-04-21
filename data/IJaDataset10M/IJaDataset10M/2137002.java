package com.lti.civil.webcam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.Image;
import com.lti.civil.awt.AWTImageConverter;
import com.lti.civil.utility.LoggerSingleton;
import com.lti.utils.synchronization.SynchronizedObjectHolder;

/**
 * Implementation of {@link CaptureObserver} which stores the most recent
 * image only.
 * @author Ken Larson
 *
 */
public class StoreMostRecent_CaptureObserver implements CaptureObserver {

    private static final Logger logger = LoggerSingleton.logger;

    private SynchronizedObjectHolder<byte[]> bytes = new SynchronizedObjectHolder<byte[]>();

    public void onNewImage(final CaptureStream sender, final Image image) {
        if (image == null) {
            bytes = null;
            return;
        }
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(AWTImageConverter.toBufferedImage(image), "JPG", os);
            os.close();
            bytes.setObject(os.toByteArray());
        } catch (IOException e) {
            logger.log(Level.WARNING, "" + e, e);
            bytes.setObject(null);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "" + t, t);
            bytes.setObject(null);
        }
    }

    public void onError(CaptureStream sender, CaptureException e) {
        logger.log(Level.WARNING, "" + e, e);
        bytes.setObject(null);
    }

    public byte[] getBytes() {
        return bytes.getObject();
    }
}
