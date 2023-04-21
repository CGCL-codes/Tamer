package santa.nice.imaging;

import java.awt.image.BufferedImage;

public class FlipImage extends InPlaceFilter {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    protected int direction;

    public FlipImage(int direction) {
        this.direction = direction;
    }

    @Override
    public void applyInPlace(BufferedImage image) {
        runningFlag = true;
        int width = image.getWidth();
        int height = image.getHeight();
        if (direction == HORIZONTAL) {
            for (int row = 0; row < height && cancelFlag == false; row++) {
                for (int col = 0; col < width / 2 && cancelFlag == false; col++) {
                    int c1 = image.getRGB(col, row);
                    int c2 = image.getRGB(width - col - 1, row);
                    image.setRGB(col, row, c2);
                    image.setRGB(width - col - 1, row, c1);
                }
                progress = 100 * row / height;
            }
        } else {
            for (int row = 0; row < height / 2 && cancelFlag == false; row++) {
                for (int col = 0; col < width && cancelFlag == false; col++) {
                    int c1 = image.getRGB(col, row);
                    int c2 = image.getRGB(col, height - row - 1);
                    image.setRGB(col, row, c2);
                    image.setRGB(col, height - row - 1, c1);
                }
                progress = 100 * row / height;
            }
        }
        cancelFlag = false;
        runningFlag = false;
    }
}
