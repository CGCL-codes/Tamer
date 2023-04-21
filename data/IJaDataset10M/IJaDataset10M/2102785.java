package santa.nice.imaging;

import java.awt.image.BufferedImage;

/**
 * Abstract class of all filters. It could be applied to an image, obtaining
 * a new image as result
 * @author Santa
 *
 */
public abstract class Filter {

    /**
	 * Apply an filter to an image. The result image will be allocated.
	 * @param image Original image
	 * @return Processed image
	 */
    public abstract BufferedImage apply(BufferedImage image);

    protected boolean cancelFlag = false;

    protected boolean runningFlag = false;

    protected int progress = 0;

    public final void cancel() {
        if (runningFlag == true) {
            cancelFlag = true;
        }
    }

    public int getProgress() {
        return progress;
    }

    public final boolean isRunning() {
        return runningFlag;
    }
}
