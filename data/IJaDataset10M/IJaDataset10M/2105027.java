package player;

import java.awt.event.KeyEvent;

/**
 * @author Hossomi
 * 
 * CLASS DirPad -------------------------------------------
 * Contém informações sobre os direcionais.
 */
public class DirPad {

    public enum Direction {

        UP, RIGHT, DOWN, LEFT
    }

    public void setDirection(Direction dir, boolean pressed) {
        if (dir == Direction.UP) up = pressed; else if (dir == Direction.RIGHT) right = pressed; else if (dir == Direction.DOWN) down = pressed; else if (dir == Direction.LEFT) left = pressed;
    }

    public boolean isDirectionPressed(Direction dir) {
        if (dir == Direction.UP) return up; else if (dir == Direction.RIGHT) return right; else if (dir == Direction.DOWN) return down; else if (dir == Direction.LEFT) return left;
        return false;
    }

    public static Direction KeyEvent2Direction(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) return Direction.UP; else if (e.getKeyCode() == KeyEvent.VK_RIGHT) return Direction.RIGHT; else if (e.getKeyCode() == KeyEvent.VK_DOWN) return Direction.DOWN; else if (e.getKeyCode() == KeyEvent.VK_LEFT) return Direction.LEFT;
        return null;
    }

    public static int Direction2X(Direction d) {
        if (d == Direction.RIGHT) return 1;
        if (d == Direction.LEFT) return -1;
        return 0;
    }

    public static int Direction2Y(Direction d) {
        if (d == Direction.DOWN) return 1;
        if (d == Direction.UP) return -1;
        return 0;
    }

    private boolean up = false;

    private boolean right = false;

    private boolean down = false;

    private boolean left = false;
}
