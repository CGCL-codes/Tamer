package fi.gemwars.gameobjects;

import java.awt.Point;
import fi.gemwars.gameobjects.map.Map;
import fi.gemwars.io.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/**
 * Enemies of the player. Stupid ants that move according to 
 * a certain pattern so they can be predicted.
 * 
 */
public class Monster extends AEntity {

    private Animation walkingRight;

    private Animation walkingLeft;

    private Animation walkingUp;

    private Animation walkingDown;

    private double distance;

    /**
	 * This is used when moster is reseted after player is died 
	 */
    private Point startingPosition;

    public int positionX;

    public int positionY;

    private Map map;

    private boolean dead = false;

    /**
	 * Creates a new monster
	 * @param x position x
	 * @param y position y
	 * @param map link to the map we are on
	 */
    public Monster(int x, int y, Map map) {
        walkingRight = ResourceManager.fetchAnimation("MONSTER_RIGHT");
        walkingLeft = ResourceManager.fetchAnimation("MONSTER_LEFT");
        walkingUp = ResourceManager.fetchAnimation("MONSTER_UP");
        walkingDown = ResourceManager.fetchAnimation("MONSTER_DOWN");
        positionX = x;
        positionY = y;
        startingPosition = new Point(x, y);
        this.map = map;
        speed = 0.25;
        this.direction = Direction.STATIONARY;
    }

    @Override
    public void render(GameContainer cont, Graphics grap) throws SlickException {
        int drawX = positionX * Item.TILE_WIDTH;
        int drawY = positionY * Item.TILE_HEIGHT;
        if (direction == Direction.UP) {
            walkingUp.draw(drawX, Math.round(drawY + Item.TILE_HEIGHT - distance));
        }
        if (direction == Direction.DOWN) {
            walkingDown.draw(drawX, Math.round(drawY - Item.TILE_HEIGHT + distance));
        }
        if (direction == Direction.LEFT) {
            walkingLeft.draw(Math.round(drawX + Item.TILE_WIDTH - distance), drawY);
        }
        if (direction == Direction.RIGHT) {
            walkingRight.draw(Math.round(drawX - Item.TILE_WIDTH + distance), drawY);
        }
    }

    @Override
    public void update(GameContainer cont, int delta) throws SlickException {
        if (direction == Direction.STATIONARY) {
            direction = Direction.RIGHT;
            distance = 0;
            changeDirection();
        }
        if (direction != Direction.STATIONARY && distance <= Item.TILE_HEIGHT) {
            distance += speed * delta;
        } else {
            if (distance > 0) distance = distance - Item.TILE_HEIGHT;
            changeDirection();
        }
    }

    /**
	 * Reset monster position to starting position where it was when it was created.
	 */
    public void putBackToStartingPosition() {
        positionX = startingPosition.x;
        positionX = startingPosition.y;
        direction = Direction.STATIONARY;
    }

    /**
	 * The AI of the monster.
	 * <p>
	 * Monsters "hug" the wall on their left. If they encounter a wall, they turn left until they can get out of the situation.
	 * However, if there is no wall, they should go forward...
	 * 
	 * @throws SlickException if something goes wrong...
	 */
    private void changeDirection() throws SlickException {
        if (direction == Direction.RIGHT) {
            if (!map.isMonsterColliding(positionX, positionY - 1) && map.isMonsterColliding(positionX - 1, positionY - 1)) {
                positionY--;
                direction = Direction.UP;
                return;
            } else if (!map.isMonsterColliding(positionX + 1, positionY)) {
                positionX++;
                return;
            } else {
                direction = Direction.UP;
            }
        }
        if (direction == Direction.UP) {
            if (!map.isMonsterColliding(positionX - 1, positionY) && map.isMonsterColliding(positionX - 1, positionY + 1)) {
                positionX--;
                direction = Direction.LEFT;
                return;
            } else if (!map.isMonsterColliding(positionX, positionY - 1)) {
                positionY--;
                return;
            } else {
                direction = Direction.LEFT;
            }
        }
        if (direction == Direction.LEFT) {
            if (!map.isMonsterColliding(positionX, positionY + 1) && map.isMonsterColliding(positionX + 1, positionY + 1)) {
                positionY++;
                direction = Direction.DOWN;
                return;
            } else if (!map.isMonsterColliding(positionX - 1, positionY)) {
                positionX--;
                return;
            } else {
                direction = Direction.DOWN;
            }
        }
        if (direction == Direction.DOWN) {
            if (!map.isMonsterColliding(positionX + 1, positionY) && map.isMonsterColliding(positionX + 1, positionY - 1)) {
                positionX++;
                direction = Direction.RIGHT;
                return;
            } else if (!map.isMonsterColliding(positionX, positionY + 1)) {
                positionY++;
                return;
            } else {
                direction = Direction.RIGHT;
                distance = Item.TILE_HEIGHT + 0.01;
            }
        }
    }

    /**
	 * Kills the monster.
	 */
    public void kill() {
        dead = true;
    }

    /**
	 * To check whether or not the monster is dead.
	 * @return true, if it is dead, false, if not
	 */
    public boolean isDead() {
        return dead;
    }
}
