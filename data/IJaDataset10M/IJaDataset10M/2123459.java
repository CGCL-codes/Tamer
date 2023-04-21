package game.sprite.action;

import game.controls.Direction;
import game.graphics.map.Coordinate;
import game.graphics.map.Square;
import game.sprite.Sprite;

public class Move implements Action {

    private Coordinate target;

    private Sprite sprite;

    /**
	 * Constructor for the Move class
	 * @param sprite The sprite to be moved
	 * @param target The target where the sprite should be moved to
	 */
    public Move(Sprite sprite, Coordinate target) {
        this.sprite = sprite;
        this.target = target;
    }

    /**
	 * Change the coordinates of the sprite and the way it's facing
	 */
    public boolean execute() {
        Square current = sprite.getOutlineBorder();
        int newX;
        int newY;
        double dx = target.getX() - current.getX();
        double dy = target.getY() - current.getY();
        double angle = Math.atan2(dy, dx);
        if (angle >= Math.PI * -7 / 8 && angle < Math.PI * -5 / 8) sprite.setDirection(Direction.NORTHWEST); else if (angle >= Math.PI * -5 / 8 && angle < Math.PI * -3 / 8) sprite.setDirection(Direction.NORTH); else if (angle >= Math.PI * -3 / 8 && angle < Math.PI * -1 / 8) sprite.setDirection(Direction.NORTHEAST); else if (angle >= Math.PI * -1 / 8 && angle < Math.PI * 1 / 8) sprite.setDirection(Direction.EAST); else if (angle >= Math.PI * 1 / 8 && angle < Math.PI * 3 / 8) sprite.setDirection(Direction.SOUTHEAST); else if (angle >= Math.PI * 3 / 8 && angle < Math.PI * 5 / 8) sprite.setDirection(Direction.SOUTH); else if (angle >= Math.PI * 5 / 8 && angle < Math.PI * 7 / 8) sprite.setDirection(Direction.SOUTHWEST); else sprite.setDirection(Direction.WEST);
        if (current.getX() == target.getX()) newX = current.getX(); else if (current.getX() > target.getX()) newX = current.getX() - 1; else newX = current.getX() + 1;
        if (current.getY() == target.getY()) newY = current.getY(); else if (current.getY() > target.getY()) newY = current.getY() - 1; else newY = current.getY() + 1;
        Square newSquare = new Square(newX, newY, current.getHeight(), current.getWidth());
        sprite.setOutlineBorder(newSquare);
        if (newSquare.equals(current)) return true; else return false;
    }
}
