package ihaterobots.entities.simulator;

import ihaterobots.entities.simulator.BaseEntitySimulator;
import ihaterobots.entities.EntityListener;
import ihaterobots.entities.VertJumper;
import ihaterobots.game.Utils;
import ihaterobots.game.map.GameMap;

/**
 *
 * EntityState.WALKING: going up/down
 * EntityState.STANDING: stay on floor/ceiling to bounce
 * @author cghislai
 */
public class VertJumperSimulator extends BaseEntitySimulator implements EntityListener {

    private int bounceTimer;

    private VertJumper vertJumper;

    public VertJumperSimulator(VertJumper vertJumper) {
        super(vertJumper);
        this.vertJumper = vertJumper;
        vertJumper.setMoving(true);
        vertJumper.addEntityListener(this);
    }

    @Override
    protected void updateStates(int delta) {
        super.updateStates(delta);
        if (vertJumper.isOnTeleport() && vertJumper.isOnGround()) {
            teleport();
        }
    }

    @Override
    protected void updateAccel(int delta) {
    }

    @Override
    protected void updateSpeed(int delta) {
        vertJumper.setSpeedY(0);
        if (vertJumper.isMoving()) {
            float speedY = VertJumper.JUMPING_SPEED * (vertJumper.isBouncingUp() ? -1 : 1);
            vertJumper.setSpeedY(speedY);
        }
    }

    @Override
    protected void checkMapCollisionNoPlaceOver(GameMap map, float centerX, float centerY, float leftX, float rightX, float topY, float botY, int tilePosX, int tilePosY) {
        final boolean top = map.isTileSolid(centerX, topY + 1);
        final boolean bottom = map.isTileSolid(centerX, botY - 1);
        int tileSize = Utils.TILE_SIZE;
        if (top) {
            centerY = (tilePosY) * tileSize + vertJumper.getHeigth() / 2;
            collideUp(centerY);
        }
        if (bottom) {
            centerY = (tilePosY + 1) * tileSize - vertJumper.getHeigth() / 2;
            collideDown(centerY);
        }
        checkTileType(map, leftX, botY, rightX, topY, centerX, centerY);
    }

    @Override
    protected void updateTimers(int delta) {
        super.updateTimers(delta);
        if (bounceTimer > 0 && !vertJumper.isTeleporting()) {
            bounceTimer -= delta;
            if (bounceTimer <= 0) {
                vertJumper.setMoving(true);
            }
        }
    }

    @Override
    public void collidedDown() {
        if (vertJumper.isMoving()) {
            vertJumper.setMoving(false);
            vertJumper.setBouncingUp(true);
            bounceTimer = VertJumper.BOUNCE_TIME;
            vertJumper.getDrawer().startBounce();
        }
    }

    @Override
    public void collidedUp() {
        if (vertJumper.isMoving()) {
            vertJumper.setMoving(false);
            vertJumper.setBouncingUp(false);
            bounceTimer = VertJumper.BOUNCE_TIME;
            vertJumper.getDrawer().startBounce();
        }
    }

    @Override
    public void movingChanged() {
    }

    @Override
    public void teleportingChanged() {
        if (!parent.isTeleporting()) {
            vertJumper.setMoving(true);
        }
    }

    @Override
    public void climbingChanged() {
    }

    @Override
    public void fallingChanged() {
    }

    @Override
    public void facingLeftChanged() {
    }

    @Override
    public void stuckChanged() {
    }

    @Override
    public void collidedLeft() {
    }

    @Override
    public void collidedRight() {
    }
}
