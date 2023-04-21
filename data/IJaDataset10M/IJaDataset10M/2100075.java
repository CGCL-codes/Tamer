package pelletQuest.entities;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import pelletQuest.map.GameMap;
import pelletQuest.database.*;
import pelletQuest.resources.GraphicsManager;

public class EntityPhase {

    protected String spriteset;

    protected Vector2f drawFrom;

    protected float direction;

    protected String behavior;

    protected int moveSpeed;

    protected int animSpeed;

    protected boolean canTeleport;

    protected boolean collidesWithSolids;

    protected boolean canMoveOff;

    protected boolean canMoveThrough;

    protected boolean isSolid;

    protected int damage;

    protected int layer;

    protected String[] onEnter;

    protected String[] onCollide;

    protected String[] onUpdate;

    protected String[] onHurt;

    protected String[] passableTerrains;

    protected int duration;

    public EntityPhase(String spriteset, String behavior, String[] onCollide, String[] onUpdate, String[] onEnter, String[] onHurt, String[] passableTerrains, Vector2f drawFrom, boolean isSolid, int layer, int moveSpeed, int animSpeed, int damage, double direction, int duration, boolean canTeleport, boolean collidesWithSolids, boolean canMoveOff, boolean canMoveThrough) {
        this.spriteset = spriteset;
        this.drawFrom = drawFrom;
        this.behavior = behavior;
        this.moveSpeed = moveSpeed;
        this.animSpeed = animSpeed;
        this.canTeleport = canTeleport;
        this.collidesWithSolids = collidesWithSolids;
        this.canMoveOff = canMoveOff;
        this.canMoveThrough = canMoveThrough;
        this.isSolid = isSolid;
        this.damage = damage;
        this.onCollide = onCollide;
        this.onUpdate = onUpdate;
        this.onEnter = onEnter;
        this.onHurt = onHurt;
        this.layer = layer;
        this.passableTerrains = passableTerrains;
        this.direction = (float) Math.toRadians(direction);
        this.duration = duration;
    }

    public void render(Graphics g, int x, int y, Vector2f mavingDirection, int frame) {
        if (spriteset != null) {
            GraphicsManager.getSpriteset(spriteset).render(g, (int) (x + drawFrom.x), (int) (y + drawFrom.y), mavingDirection, direction, frame);
        }
    }

    public int getLengthOfSpriteset(Vector2f mavingDirection) {
        if (spriteset != null) {
            return GraphicsManager.getSpriteset(spriteset).getLengthOfSequence(mavingDirection);
        } else {
            return 0;
        }
    }

    public void onUpdate(Entity self, GameMap map, int x, int y) {
        for (int i = 0; i < onUpdate.length; i++) {
            Trigger.activate(onUpdate[i], self, self, map, x, y);
        }
    }

    public void onCollide(Entity self, Entity other, GameMap map, int x, int y) {
        for (int i = 0; i < onCollide.length; i++) {
            Trigger.activate(onCollide[i], self, other, map, x, y);
        }
    }

    public void onEnter(Entity self, GameMap map, int x, int y) {
        for (int i = 0; i < onEnter.length; i++) {
            Trigger.activate(onEnter[i], self, self, map, x, y);
        }
    }

    public void onHurt(Entity self, GameMap map, int x, int y) {
        for (int i = 0; i < onHurt.length; i++) {
            Trigger.activate(onHurt[i], self, self, map, x, y);
        }
    }

    public boolean isSolid() {
        return isSolid;
    }

    public int getDamage() {
        return damage;
    }

    public boolean cycles() {
        return duration > 0;
    }

    public int getDuration() {
        return duration;
    }

    public void setDirection(double angle) {
        this.direction = (float) Math.toRadians(angle);
    }

    public double getDirection() {
        return direction;
    }

    public String[] getPassableTerrains() {
        return passableTerrains;
    }

    public int getLayer() {
        return layer;
    }

    public boolean hasBehavior() {
        return behavior != null;
    }

    public String getBehavior() {
        return behavior;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public boolean collidesWithSolids() {
        return collidesWithSolids;
    }

    public boolean canTeleport() {
        return canTeleport;
    }

    public int getAnimSpeed() {
        return animSpeed;
    }

    public boolean canMoveOff() {
        return canMoveOff;
    }

    public boolean canMoveThrough() {
        return canMoveThrough;
    }
}
