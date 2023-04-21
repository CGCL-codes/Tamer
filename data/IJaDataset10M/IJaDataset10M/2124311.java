package ip.dip.commonsense.opengl;

public class Leg extends Limb {

    public Leg(double girth, double length) {
        super(girth, length);
    }

    @Override
    protected double calcLowerJointAngle(int angleOfStride, double swings) {
        int forwardFactor = 1;
        if (swings < 0) {
            forwardFactor *= -1;
        }
        return (angleOfStride - 10) * (Math.cos(swings) + forwardFactor) * -forwardFactor;
    }
}
