package EDU.Washington.grad.noth.cda;

import java.awt.image.ImageObserver;
import java.awt.Image;

public class ImageButtonImageObserver implements ImageObserver {

    ImageButton imageButton;

    public ImageButtonImageObserver(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if ((infoflags & ALLBITS) != 0) {
            imageButton.repaint();
            return false;
        }
        return true;
    }
}
