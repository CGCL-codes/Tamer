package tron;

import java.awt.Point;
import java.io.*;

public class Snake implements Serializable {

    public static enum Direction implements Serializable {

        LEFT, RIGHT, DOWN, UP;

        private boolean isReversed(Direction d) {
            if ((this == LEFT && d == RIGHT) || (this == RIGHT && d == LEFT) || (this == DOWN && d == UP) || (this == UP && d == DOWN)) return true;
            return false;
        }

        public String toString() {
            if (this == Direction.LEFT) {
                return "LEFT";
            } else if (this == Direction.RIGHT) {
                return "RIGHT";
            } else if (this == Direction.DOWN) {
                return "DOWN";
            } else return "UP";
        }
    }

    public int number;

    public Direction direction;

    public Point point;

    public boolean isAlive;

    public Snake(int number, int x, int y, Direction direction) {
        this.number = number;
        isAlive = true;
        this.direction = direction;
        this.point = new Point(x, y);
    }

    public Point getNextPoint() {
        int x = point.x;
        int y = point.y;
        if (direction == Direction.LEFT) {
            return new Point(x - 1, y);
        } else if (direction == Direction.RIGHT) {
            return new Point(x + 1, y);
        } else if (direction == Direction.DOWN) {
            return new Point(x, y + 1);
        } else return new Point(x, y - 1);
    }

    public void updatePoint() {
        Point next = getNextPoint();
        point = next;
    }

    public boolean isHead(int x, int y) {
        return this.point.x == x && this.point.y == y;
    }

    public void kill() {
        isAlive = false;
        System.out.println("Snake " + number + " was killed");
    }

    public void setDirection(Direction d) {
        if (!direction.isReversed(d) && direction != d) {
            this.direction = d;
        }
    }

    public String toString() {
        return "number " + number + " " + point.x + " " + point.y + " " + direction;
    }
}
