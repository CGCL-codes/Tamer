package net.sf.croputils;

import java.text.DecimalFormat;

/**
 * Parameters describing a crop area. 
 * dx and dy define distance between centre of source image and centre og crop area. Distance is between 0 and 1, relative to the image size.
 * cropW and cropH give width and height of crop area. Number is between 0 and 1, relative to the image size.
 * rotate is rotation of crop area, in radians. 
 * 
 * @author Kristian Helgesen, Tarantell AS
 */
public class TransformParams {

    private float dx;

    private float dy;

    private float cropW;

    private float cropH;

    private float rotate;

    public TransformParams() {
        dx = 0.0f;
        dy = 0.0f;
        cropW = 1.0f;
        cropH = 1.0f;
        rotate = 0.0f;
    }

    public TransformParams(float dx, float dy, float cropW, float cropH, float rotate) {
        this.dx = dx;
        this.dy = dy;
        this.cropW = cropW;
        this.cropH = cropH;
        this.rotate = rotate;
    }

    public TransformParams clone() {
        return new TransformParams(getDx(), getDy(), getCropW(), getCropH(), getRotate());
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getCropW() {
        return cropW;
    }

    public float getCropH() {
        return cropH;
    }

    public float getRotate() {
        return rotate;
    }

    @Override
    public String toString() {
        DecimalFormat df3 = new DecimalFormat("0.000");
        return "dx:" + df3.format(dx) + ", dy:" + df3.format(dy) + ", cropW:" + df3.format(cropW) + ", cropH:" + df3.format(cropH) + ", a:" + df3.format(getRotate());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TransformParams)) return false;
        TransformParams tp = (TransformParams) obj;
        return tp.getDx() == getDx() && tp.getDy() == getDy() && tp.getCropH() == getCropH() && tp.getCropW() == getCropW() && tp.getRotate() == getRotate();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
