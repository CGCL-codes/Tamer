package javacream.scene;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Transform
 * 
 * @author Glenn Powell
 *
 */
public class Transform {

    private Point2D.Float position = new Point2D.Float(0, 0);

    private Point2D.Float scale = new Point2D.Float(1, 1);

    private Point2D.Float anchor = new Point2D.Float(0, 0);

    private float angle = 0;

    private AffineTransform transform = new AffineTransform();

    private boolean needsUpdate = false;

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setPosition(float x, float y) {
        position.setLocation(x, y);
        needsUpdate = true;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public void setScale(float scale) {
        setScale(scale, scale);
        needsUpdate = true;
    }

    public void setScale(float x, float y) {
        scale.setLocation(x, y);
        needsUpdate = true;
    }

    public float getAnchorX() {
        return anchor.x;
    }

    public float getAnchorY() {
        return anchor.y;
    }

    public void setAnchor(float x, float y) {
        anchor.setLocation(x, y);
        needsUpdate = true;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        needsUpdate = true;
    }

    public void setAffineTransfrom(AffineTransform transform) {
        this.transform = transform;
        needsUpdate = false;
    }

    public AffineTransform getAffineTransform() {
        if (needsUpdate) {
            transform.setToTranslation(getX(), getY());
            transform.scale(getScaleX(), getScaleY());
            if (getAngle() != 0) transform.rotate(getAngle(), getAnchorX(), getAnchorY());
            needsUpdate = false;
        }
        return transform;
    }
}
