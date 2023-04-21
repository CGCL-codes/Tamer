package net.java.dev.joode.graphics2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.openmali.FastMath;
import net.java.dev.joode.geom.Circle;

/**
 * A graphical representation of a circle geom 
 * @author s0570397
 *
 */
public class BoundCircle implements Drawable2D {

    private final Circle binding;

    Color color = Color.WHITE;

    Color marker = Color.BLACK;

    public BoundCircle(Circle binding) {
        this.binding = binding;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        AffineTransform original = g.getTransform();
        AffineTransform tx = new AffineTransform(original);
        float rotX = (binding.getRotation().get(0, 0));
        float rotY = (binding.getRotation().get(0, 1));
        float thetaC1 = FastMath.acos(rotX);
        float thetaC2 = -thetaC1;
        float thetaS1 = FastMath.asin(rotY);
        float thetaS2 = FastMath.PI - thetaS1;
        if (thetaS2 > FastMath.PI) thetaS2 -= FastMath.TWO_PI;
        float theta;
        if (thetaC1 + .001 > thetaS1 && thetaC1 - .001 < thetaS1) {
            theta = thetaC1;
        } else if (thetaC1 + .001 > thetaS2 && thetaC1 - .001 < thetaS2) {
            theta = thetaC1;
        } else {
            theta = thetaC2;
        }
        tx.concatenate(AffineTransform.getTranslateInstance(binding.getPosition().getX(), binding.getPosition().getY()));
        tx.concatenate(AffineTransform.getRotateInstance(theta));
        tx.concatenate(AffineTransform.getScaleInstance(binding.getRadius(), binding.getRadius()));
        g.setTransform(tx);
        g.fillOval(-1, -1, 2, 2);
        g.setColor(marker);
        tx.concatenate(AffineTransform.getScaleInstance(.01f, .01f));
        g.setTransform(tx);
        g.drawLine(0, 0, 100, 0);
        g.setTransform(original);
    }
}
