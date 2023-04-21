package pelletQuest.entities;

import java.util.ArrayList;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.util.Log;

public class Behaviors {

    public static void behave(Entity actor, String behavior, double direction) {
        if (behavior.equals("wander")) {
            if ((actor.getBox().getX() % 16 == 0 && actor.getBox().getY() % 16 == 0) || actor.getMoveDirection().x + actor.getMoveDirection().y == 0) {
                Behaviors.wanderAimlessly(actor);
            }
        } else if (behavior.equals("projectile")) {
            actor.setMoveDirection(new Vector2f((float) FastTrig.cos(direction), (float) FastTrig.sin(direction)));
            if (!actor.canMove(actor.getMoveDirection()) || isMovingOffMap(actor, actor.getMoveDirection())) {
                actor.destroy();
                return;
            }
        } else if (behavior.equals("hunt")) {
            if ((actor.getBox().getX() % 16 == 0 && actor.getBox().getY() % 16 == 0) || actor.getMoveDirection().x + actor.getMoveDirection().y == 0) {
                Entity max = null;
                ArrayList<Entity> entities = actor.getMap().getEntities();
                for (Entity e : entities) {
                    if (e.getName().equals("player")) {
                        max = e;
                        break;
                    }
                }
                if (max == null) {
                    Behaviors.wanderAimlessly(actor);
                } else {
                    Behaviors.hunt(actor, new Vector2f(max.getBox().getCenterX(), max.getBox().getCenterY()), false);
                }
            }
        } else if (behavior.equals("intercept")) {
            if ((actor.getBox().getX() % 16 == 0 && actor.getBox().getY() % 16 == 0) || actor.getMoveDirection().x + actor.getMoveDirection().y == 0) {
                Entity player = null;
                ArrayList<Entity> entities = actor.getMap().getEntities();
                for (Entity e : entities) {
                    if (e.getName().equals("player")) {
                        player = e;
                        break;
                    }
                }
                if (player == null) {
                    Behaviors.wanderAimlessly(actor);
                } else {
                    Behaviors.hunt(actor, new Vector2f(player.getBox().getCenterX() + player.getLastDirection().x * 64, player.getBox().getCenterY() + player.getLastDirection().y * 64), false);
                }
            }
        } else if (behavior.equals("flee")) {
            if ((actor.getBox().getX() % 16 == 0 && actor.getBox().getY() % 16 == 0) || actor.getMoveDirection().x + actor.getMoveDirection().y == 0) {
                Entity player = null;
                ArrayList<Entity> entities = actor.getMap().getEntities();
                for (Entity e : entities) {
                    if (e.getName().equals("player")) {
                        player = e;
                        break;
                    }
                }
                if (player == null) {
                    Behaviors.wanderAimlessly(actor);
                } else {
                    Vector2f myPos = new Vector2f(actor.getBox().getCenterX(), actor.getBox().getCenterY());
                    Vector2f playerPos = new Vector2f(player.getBox().getCenterX(), player.getBox().getCenterY());
                    double angle = Math.atan2(playerPos.y - myPos.y, playerPos.x - myPos.x) + Math.PI;
                    Vector2f target = new Vector2f((float) FastTrig.cos(angle) * 100 + myPos.x, (float) FastTrig.sin(angle) * 100 + myPos.y);
                    Behaviors.hunt(actor, target, true);
                }
            }
        } else if (behavior.equals("none")) {
            return;
        } else {
            Log.warn("Unkown \"" + behavior + "\" behavior for enemy.");
            return;
        }
    }

    public static void hunt(Entity hunter, Vector2f target, boolean reverse) {
        float xDistance = target.getX() - hunter.getBox().getCenterX();
        float yDistance = target.getY() - hunter.getBox().getCenterY();
        if (xDistance == 0) {
            xDistance = 1;
        }
        if (yDistance == 0) {
            yDistance = 1;
        }
        float xMove = xDistance / Math.abs(xDistance);
        float yMove = yDistance / Math.abs(yDistance);
        if (reverse) {
            if ((Math.abs(xDistance) > Math.abs(yDistance))) {
                moveToward(hunter, new Vector2f(0, yMove), new Vector2f(xMove, 0));
            } else {
                moveToward(hunter, new Vector2f(xMove, 0), new Vector2f(0, yMove));
            }
        } else {
            if ((Math.abs(xDistance) > Math.abs(yDistance))) {
                moveToward(hunter, new Vector2f(xMove, 0), new Vector2f(0, yMove));
            } else {
                moveToward(hunter, new Vector2f(0, yMove), new Vector2f(xMove, 0));
            }
        }
    }

    private static void moveToward(Entity mover, Vector2f primary, Vector2f secondary) {
        if (wantsToMoveInDirection(mover, primary)) {
            mover.setMoveDirection(primary);
            mover.setLastDirection(primary);
        } else if (wantsToMoveInDirection(mover, secondary)) {
            mover.setMoveDirection(secondary);
            mover.setLastDirection(secondary);
        } else if (wantsToMoveInDirection(mover, new Vector2f(-secondary.x, -secondary.y))) {
            mover.setMoveDirection(new Vector2f(-secondary.x, -secondary.y));
            mover.setLastDirection(new Vector2f(-secondary.x, -secondary.y));
        } else if (wantsToMoveInDirection(mover, new Vector2f(-primary.x, -primary.y))) {
            mover.setMoveDirection(new Vector2f(-primary.x, -primary.y));
            mover.setLastDirection(new Vector2f(-primary.x, -primary.y));
        }
    }

    public static void wanderAimlessly(Entity wanderer) {
        int rand = (int) (Math.random() * 4);
        Vector2f direction = new Vector2f(0, 0);
        if (rand == 0) {
            direction = new Vector2f(0, -1);
        } else if (rand == 1) {
            direction = new Vector2f(0, 1);
        } else if (rand == 2) {
            direction = new Vector2f(1, 0);
        } else if (rand == 3) {
            direction = new Vector2f(-1, 0);
        }
        if (wantsToMoveInDirection(wanderer, direction)) {
            wanderer.setMoveDirection(direction);
            wanderer.setLastDirection(wanderer.getMoveDirection());
        }
    }

    private static boolean wantsToMoveInDirection(Entity mover, Vector2f direction) {
        boolean wantsToTurnAround = (mover.getLastDirection().y != 0 && -direction.y == mover.getLastDirection().y) || (mover.getLastDirection().x != 0 && -direction.x == mover.getLastDirection().x);
        return (!wantsToTurnAround || isDeadEnd(mover, direction)) && canMoveIncludingEntities(mover, direction);
    }

    private static boolean isDeadEnd(Entity mover, Vector2f direction) {
        if (direction.y == -1) {
            return (!canMoveIncludingEntities(mover, new Vector2f(0, 1)) && !canMoveIncludingEntities(mover, new Vector2f(1, 0)) && !canMoveIncludingEntities(mover, new Vector2f(-1, 0)));
        } else if (direction.y == 1) {
            return (!canMoveIncludingEntities(mover, new Vector2f(0, -1)) && !canMoveIncludingEntities(mover, new Vector2f(1, 0)) && !canMoveIncludingEntities(mover, new Vector2f(-1, 0)));
        } else if (direction.x == 1) {
            return (!canMoveIncludingEntities(mover, new Vector2f(0, 1)) && !canMoveIncludingEntities(mover, new Vector2f(0, -1)) && !canMoveIncludingEntities(mover, new Vector2f(-1, 0)));
        } else if (direction.x == -1) {
            return (!canMoveIncludingEntities(mover, new Vector2f(0, 1)) && !canMoveIncludingEntities(mover, new Vector2f(1, 0)) && !canMoveIncludingEntities(mover, new Vector2f(0, -1)));
        }
        return false;
    }

    private static boolean canMoveIncludingEntities(Entity mover, Vector2f direction) {
        if (mover.canMove(direction)) {
            ArrayList<Entity> ent = mover.getCollisions(direction);
            for (Entity e : ent) {
                if (e.isSolid() && !e.getName().equals("player")) {
                    return false;
                }
            }
            if (!mover.canLeaveMap() && isMovingOffMap(mover, direction)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean isMovingOffMap(Entity actor, Vector2f direction) {
        return actor.getBox().getX() + direction.x <= 16 || actor.getBox().getX() + direction.x + actor.getBox().getWidth() >= 416 || actor.getBox().getY() + direction.y <= 16 || actor.getBox().getY() + direction.y + actor.getBox().getHeight() >= 272;
    }
}
