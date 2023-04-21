package rescuecore.commands;

public class Nozzle implements java.io.Serializable {

    private int target, direction, x, y, water;

    public Nozzle(int target, int direction, int x, int y, int water) {
        this.target = target;
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.water = water;
    }

    public int getTarget() {
        return target;
    }

    public int getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWater() {
        return water;
    }
}
