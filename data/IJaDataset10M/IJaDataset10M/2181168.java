package geomwarsremake.objects.enemies;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import util.GeomWarUtil;
import geomwarsremake.objects.Enemy;
import geomwarsremake.objects.Level;
import geomwarsremake.objects.Shot;
import geomwarsremake.states.IngameState;

public class GreenSquare extends Enemy {

    private final float AVOID_RADIUS = 200;

    private final float AVOID_FORCE = 60;

    public GreenSquare(int posX, int posY) {
        setCircle(new Circle(posX, posY, 13));
        setSpeed(0.20f);
        weight = 2;
        score = 100;
    }

    public boolean isInstanceOf(Enemy enemy) {
        return enemy instanceof GreenSquare;
    }

    /**
	 * Draw this object
	 * @param g The graphics we are drawing on.
	 * @param debug Indicate if we are doing testing and we want to see the collision 
	 * circle
	 */
    public void draw(Graphics g, boolean debug) {
        render(g);
        g.setColor(Color.white);
        if (debug) {
            g.draw(circle);
        }
    }

    public void update(int deltaTime) {
        super.update(deltaTime);
        updateAnimation(deltaTime);
    }

    public float getAvoidForce(float distance) {
        return (AVOID_RADIUS - distance) / AVOID_RADIUS;
    }

    @Override
    public void updatePosition(int deltaTime) {
        float greenX = circle.getCenterX();
        float greenY = circle.getCenterY();
        float shipX = level.pship.getCircle().getCenterX();
        float shipY = level.pship.getCircle().getCenterY();
        float directionAngle = (float) GeomWarUtil.findAngle(shipX - greenX, shipY - greenY);
        speedX = (float) (getSpeed() * Math.cos(directionAngle));
        speedY = (float) (getSpeed() * Math.sin(directionAngle));
        for (AttractionHole hole : level.holes) {
            if (hole.isAttracting) {
                float holeX = hole.getCircle().getCenterX();
                float holeY = hole.getCircle().getCenterY();
                float deltaX = holeX - greenX;
                float deltaY = holeY - greenY;
                float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (distance < hole.getAttractionRadius()) {
                    float ax = hole.getAttractionForce(distance) / weight * (deltaX / distance);
                    float ay = hole.getAttractionForce(distance) / weight * (deltaY / distance);
                    speedX += ax * deltaTime / 1000;
                    speedY += ay * deltaTime / 1000;
                }
            }
        }
        float totalFx = 0;
        float totalFy = 0;
        float deltaX1 = shipX - greenX;
        float deltaY1 = shipY - greenY;
        float distanceShip = (float) Math.sqrt(deltaX1 * deltaX1 + deltaY1 * deltaY1);
        for (Shot shot : level.shots) {
            float shotX = shot.getCircle().getCenterX();
            float shotY = shot.getCircle().getCenterY();
            float deltaX = shotX - greenX;
            float deltaY = shotY - greenY;
            float distanceShot = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distanceShot < AVOID_RADIUS) {
                float deltaX2 = shipX - shotX;
                float deltaY2 = shipY - shotY;
                float distanceShipShot = (float) Math.sqrt(deltaX2 * deltaX2 + deltaY2 * deltaY2);
                if (distanceShipShot < distanceShip) {
                    float tempX = shot.getSpeedX();
                    float tempY = shot.getSpeedY();
                    float shotDirection = (float) Math.toDegrees(GeomWarUtil.findAngle(tempX, tempY));
                    float greenDirection = (float) Math.toDegrees(GeomWarUtil.findAngle(-deltaX1, -deltaY1));
                    float smallGreenDir = greenDirection - 80;
                    float bigGreenDir = greenDirection + 80;
                    if ((shotDirection > smallGreenDir && shotDirection < bigGreenDir) || (shotDirection + 360 > smallGreenDir && shotDirection + 360 < bigGreenDir) || (shotDirection - 360 > smallGreenDir && shotDirection - 360 < bigGreenDir)) {
                        totalFx += -getAvoidForce(distanceShot) * (deltaX / distanceShot);
                        totalFy += -getAvoidForce(distanceShot) * (deltaY / distanceShot);
                    }
                }
            }
        }
        float totalForce = (float) Math.sqrt(totalFx * totalFx + totalFy * totalFy);
        float fx = 0;
        float fy = 0;
        if (totalForce != 0) {
            fx = totalFx / totalForce * AVOID_FORCE;
            fy = totalFy / totalForce * AVOID_FORCE;
        }
        speedX += fx / weight * deltaTime / 1000;
        speedY += fy / weight * deltaTime / 1000;
        float deltaX = speedX * deltaTime;
        float deltaY = speedY * deltaTime;
        float newX = circle.getX() + deltaX;
        float newY = circle.getY() + deltaY;
        getCircle().setLocation(newX, newY);
    }

    /**
	 * THIS PART IS ABOUT ANIMATION ONLY
	 */
    final int animationTime = 1500;

    int currentTime = 0;

    int size = 30;

    int s = 2;

    private void updateAnimation(int deltaTime) {
        currentTime += deltaTime;
        currentTime = currentTime % animationTime;
    }

    private void render(Graphics g) {
        int cx = (int) circle.getCenterX();
        int cy = (int) circle.getCenterY();
        state.greenImage.setCenterOfRotation((size + s) / 2, (size + s) / 2);
        state.greenImage.rotate(getRotationAngle());
        state.greenImage.draw(cx - size / 2, cy - size / 2);
        state.greenImage.rotate(-getRotationAngle());
    }

    private float getRotationAngle() {
        return (float) (1.0 * currentTime / animationTime * 360);
    }
}
