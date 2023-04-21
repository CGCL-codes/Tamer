package util;

public class Waypoint {

    private Position position = null;

    private String name;

    public Waypoint(Position position) {
        this.position = position;
    }

    public Waypoint(double x, double y) {
        position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public double getPositionX() {
        return position.getX();
    }

    public double getPositionY() {
        return position.getY();
    }

    public void setPostion(Position position) {
        this.position = position;
    }

    public void setPosition(double x, double y) {
        position.setLocation(x, y);
    }

    @Override
    public String toString() {
        return new String(getName() + ": " + getPosition());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
