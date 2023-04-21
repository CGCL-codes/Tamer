package transformations.bounds;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import model.ImageTransformation;
import model.Property;

public class FlipTransformation extends ImageTransformation {

    public static int FLIP_HORIZONTAL = 1;

    public static int FLIP_VERTICAL = 2;

    int flipType;

    public FlipTransformation(int flipType) {
        this.flipType = flipType;
    }

    public FlipTransformation() {
        this(FLIP_HORIZONTAL);
    }

    public BufferedImage applyTransformation(BufferedImage src) {
        if (flipType == FLIP_VERTICAL) {
            AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -src.getHeight(null));
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return op.filter(src, null);
        } else if (flipType == FLIP_HORIZONTAL) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-src.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return op.filter(src, null);
        } else throw new RuntimeException("badParameter");
    }

    public String shortDescription() {
        return "TODO";
    }

    public int getFlipType() {
        return flipType;
    }

    public void setFlipType(int flipType) {
        this.flipType = flipType;
    }

    public void changePropertyValue(String propId, Object value) {
    }

    public Object getPropertyValue(String propertyId) {
        return null;
    }

    public Property[] getProperties() {
        return null;
    }
}
