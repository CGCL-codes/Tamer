package ip.dip.commonsense.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Robot {

    private ShapeDrawable head = new BodyPart(0.20, 0.25, 0.22);

    private ShapeDrawable body = new BodyPart(0.4, 0.6, 0.3);

    private Limb rightArm = new Arm(0.16, 0.4);

    private Limb leftArm = new Arm(0.16, 0.4);

    private Limb rightLeg = new Leg(0.2, 0.4);

    private Limb leftLeg = new Leg(0.2, 0.4);

    private double angle;

    private int swings;

    private double length;

    private double x;

    private boolean flag = false;

    public Robot() {
        this(0);
    }

    public Robot(double angle) {
        this.angle = angle;
    }

    public void display(GLAutoDrawable drawable) {
        swings %= 360;
        double theta = Math.toRadians(angle);
        double robotX = Math.sin(theta) * 2;
        double robotY = Math.cos(theta) * 2;
        double rad = Math.cos(Math.toRadians(swings));
        if (rad < 0) {
            rad *= -1;
        }
        swings += 1;
        GL gl = drawable.getGL();
        gl.glPushMatrix();
        gl.glTranslated(0, 0, x + length);
        System.out.println(x);
        head.setPosition(0.0, 0.3, 0.0);
        head.display(drawable);
        body.display(drawable);
        rightArm.setPosition(-0.28, 0.0, 0.0);
        rightArm.setSwings(-swings);
        rightArm.display(drawable);
        leftArm.setPosition(0.28, 0.0, 0.0);
        leftArm.setSwings(swings);
        leftArm.display(drawable);
        rightLeg.setPosition(-0.1, -0.65, 0.0);
        rightLeg.setSwings(swings);
        rightLeg.display(drawable);
        leftLeg.setPosition(0.1, -0.65, 0.0);
        leftLeg.setSwings(-swings);
        leftLeg.display(drawable);
        gl.glPopMatrix();
        double l = leftLeg.getLengthOfStride();
        if (l < 0) {
            l = rightLeg.getLengthOfStride();
        }
        double tmp = length;
        length = l;
        if (tmp < length) {
            flag = false;
        } else {
            if (!flag) {
                x += length;
            }
            flag = true;
        }
        angle -= rad;
        if (angle >= 360.0f) {
            angle = 0.0f;
        }
    }
}
