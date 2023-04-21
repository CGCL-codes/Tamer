package gphoto.services.impl;

import gphoto.services.TransformationServices;
import gphoto.util.Conversion;
import java.awt.image.BufferedImage;

public class TransformationServicesImpl implements TransformationServices {

    public void redimensionner(String cheminPhoto, String cheminPhotoRedim, int tailleMax) {
        BufferedImage origImage = Conversion.toBufferedImage(cheminPhoto);
        int width = origImage.getWidth();
        int height = origImage.getHeight();
        if (width > height) {
            redimensionner(cheminPhoto, cheminPhotoRedim, tailleMax, height * tailleMax / width);
        } else {
            redimensionner(cheminPhoto, cheminPhotoRedim, width * tailleMax / height, tailleMax);
        }
    }

    private void redimensionner(String photo, String cheminPhotoRedim, int width, int height) {
    }
}
