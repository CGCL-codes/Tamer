package co.edu.unal.ungrid.image;

import java.awt.image.BufferedImage;
import co.edu.unal.ungrid.client.util.AbstractDimension;

public class GrayScaleImage extends RgbImage {

    public static final long serialVersionUID = 1L;

    private static class Factory extends ImageFactory<RgbPlane> {

        protected GrayScaleImage create() {
            return new GrayScaleImage();
        }
    }

    static {
        ImageFactory.register("co.edu.unal.ungrid.image.GrayScaleImage", new Factory());
    }

    public GrayScaleImage() {
        super(1);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(int numPlanes) {
        super(numPlanes);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final AbstractDimension<Integer> size) {
        super(size);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final RgbPlane plane) {
        super(plane);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final RgbPlane[] pa) {
        super(pa);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final AbstractDimension<Integer> size, final int clear) {
        super(size, clear);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final AbstractDimension<Integer> size, int[] ia) {
        super(size, ia);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final BufferedImage bi) {
        super(bi);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final GrayScaleImage img) {
        super(img);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final RgbImage img) {
        super(img);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final AbstractImage<RgbPlane> img) {
        super(img);
        setType(TYPE_INT_GRAY);
    }

    public GrayScaleImage(final FloatImage img) {
        super(img);
        setType(TYPE_INT_GRAY);
    }

    @Override
    public AbstractImage<RgbPlane> getInstance(final AbstractDimension<Integer> size) {
        GrayScaleImage img = new GrayScaleImage(size);
        img.setFileName(getFileName());
        return img;
    }

    public static final int TYPE_INT_GRAY = RgbImage.TYPE_INT_ARGB;
}
