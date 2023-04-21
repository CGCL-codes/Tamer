package client.util;

import titanic.basic.Ball;
import titanic.basic.BilliardKey;
import titanic.basic.Game;
import titanic.basic.Vector3D;

/**
 * Represents billiard key with its position and power of the hit.
 * @author Danon
 */
public class SimpleBilliardKey implements BilliardKey {

    private Ball ball;

    private float angle;

    private float power;

    public SimpleBilliardKey() {
        ball = null;
        angle = 0;
        power = 0;
    }

    public Ball getBall() {
        return ball;
    }

    public float getAngle() {
        return angle;
    }

    public float getPower() {
        return power;
    }

    public void changeBall(Ball b) {
        if (ball != null) ball.setSelected(false);
        ball = b;
        if (ball != null) ball.setSelected(true);
    }

    public void setAngle(float a) {
        double s = Math.sin(a);
        double c = Math.cos(a);
        angle = (float) Math.asin(s);
        if (c >= 0.0) {
            if (angle <= 0.0) angle = (float) (2 * Math.PI) + angle;
        } else angle = (float) (Math.PI) - angle;
    }

    public void setPower(float p) {
        power = p;
    }

    public boolean validAngle(Game game) {
        if (ball == null) return true;
        Ball[] balls = game.getGameScene().getBalls();
        for (int i = 0; i < balls.length; i++) {
            if (ball.equals(balls[i])) continue;
            Vector3D v = ball.getCoordinates().multiply(-1).add(balls[i].getCoordinates());
            if (v.getNorm() > 1.5 * balls[i].getRadius() / Math.tan(Math.PI / 24)) continue;
            Vector3D w = new Vector3D((float) Math.sin(angle), -(float) Math.cos(angle)).multiply((float) v.getNorm());
            if (w.multiply(-1).add(v).getNorm() < balls[i].getRadius() / 1.3) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void makeHit() {
        if (ball == null) return;
        float ang = angle + (float) Math.PI / 2;
        ball.setSpeed(new Vector3D(power * (float) Math.cos(ang) * 25, power * (float) Math.sin(ang) * 25));
    }
}
