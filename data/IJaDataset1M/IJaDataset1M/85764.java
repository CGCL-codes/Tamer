package es.eucm.eadventure.engine.core.control.functionaldata.functionalhighlights;

import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import es.eucm.eadventure.engine.core.gui.GUI;

public class FunctionalHighlightBorder extends FunctionalHighlight {

    public FunctionalHighlightBorder(boolean animated) {
        this.animated = animated;
        this.time = System.currentTimeMillis();
    }

    @Override
    public Image getHighlightedImage(Image image) {
        if (animated) calculateDisplacements(image.getWidth(null), image.getHeight(null));
        if (oldImage == null || oldImage != image) {
            BufferedImage temp = GUI.getInstance().getGraphicsConfiguration().createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);
            temp.getGraphics().drawImage(image, 0, 0, null);
            for (int i = 0; i < image.getWidth(null); i++) {
                for (int j = 0; j < image.getHeight(null); j++) {
                    temp.setRGB(i, j, temp.getRGB(i, j) & 0xff000000);
                }
            }
            temp.getGraphics().drawImage(image, 5, 5, image.getWidth(null) - 10, image.getHeight(null) - 10, null);
            oldImage = image;
            newImage = temp;
        }
        return newImage.getScaledInstance((int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), Image.SCALE_SMOOTH);
    }
}
