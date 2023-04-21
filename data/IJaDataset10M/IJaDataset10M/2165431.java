package santa.jpaint.kernel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;
import santa.nice.imaging.ImagingUtility;

/**
 * The history class that holds many stats of current image
 * @author Santa
 *
 */
public class History {

    /**
	 * The max history size
	 */
    public static final int MAX_SIZE = 100;

    /**
	 * The array of temp file
	 */
    File[] fileArray = new File[MAX_SIZE + 1];

    /**
	 * The head index of the cycle-deque fileArray
	 */
    int begin = 0;

    /**
	 * The end index of the cycle-deque fileArray
	 */
    int end = -1;

    /**
	 * The current index of the cycle-deque fileArray
	 */
    int pos = -1;

    /**
	 * We put images in this buffer, since when user is working quickly,
	 * the images will not be saved in time. By putting them in the buffer,
	 * we could let a new thread to check the buffer and saves them in disk.
	 */
    ConcurrentLinkedQueue<BufferedImage> bufferedMemory = new ConcurrentLinkedQueue<BufferedImage>();

    public History() {
        new Thread() {

            @Override
            public void run() {
                while (true) try {
                    while (bufferedMemory.isEmpty() == false) {
                        BufferedImage image = bufferedMemory.poll();
                        try {
                            pos = (pos + 1) % fileArray.length;
                            end = pos;
                            if (fileArray[end] == null) {
                                fileArray[end] = JPaintUtility.createTempFile();
                            }
                            ImageIO.write(image, "JPEG", fileArray[end]);
                        } catch (IOException e) {
                            JPaintUtility.handleException(e);
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
	 * Adds an entry to memorize
	 * @param image New image to be memorized
	 */
    public void memorize(BufferedImage image) {
        bufferedMemory.add(ImagingUtility.copyImage(image));
    }

    /**
	 * Go back in the history
	 * @return The image saved in history. Null if we can't go back
	 */
    public BufferedImage goBack() {
        if (couldGoBack()) {
            pos = (pos + fileArray.length - 1) % fileArray.length;
            try {
                return ImageIO.read(fileArray[pos]);
            } catch (IOException e) {
                JPaintUtility.handleException(e);
                return null;
            }
        } else return null;
    }

    /**
	 * Go foreward in the history
	 * @return The image in history, null if we can't go foreward
	 */
    public BufferedImage goForward() {
        if (couldGoForward()) {
            pos = (pos + 1) % fileArray.length;
            try {
                return ImageIO.read(fileArray[pos]);
            } catch (IOException e) {
                JPaintUtility.handleException(e);
                return null;
            }
        } else return null;
    }

    /**
	 * Whether we could back in history
	 * @return Boolean value indicating whether we could go back in history
	 */
    public boolean couldGoBack() {
        return pos != begin;
    }

    /**
	 * Whether we could go foreward in history
	 * @return Boolean value indicating whether we could go forward in history
	 */
    public boolean couldGoForward() {
        return pos != end;
    }
}
