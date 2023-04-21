package pteam;

import simulator.RangeFinder;
import simulator.Sensor;

public class Intersection {

    public static final int NORTH = 0;

    public static final int EAST = 1;

    public static final int SOUTH = 2;

    public static final int WEST = 3;

    private int ways;

    private boolean[] openDirections;

    private boolean[] directionsTried;

    private int cameFrom;

    private int went;

    public Intersection(int ways, int directionFacing, boolean left, boolean ahead, boolean right) {
        went = -1;
        openDirections = new boolean[4];
        cameFrom = directionFacing;
        this.ways = ways;
        if (left) openDirections[getLeft(directionFacing)] = true;
        if (right) openDirections[getRight(directionFacing)] = true;
        if (ahead) openDirections[(directionFacing)] = true;
        openDirections[getReverse(directionFacing)] = true;
        directionsTried = new boolean[4];
        directionsTried[getReverse(directionFacing)] = true;
        System.out.println("INTERSECTION: LEFT: " + left + " RIGHT: " + right + " AHEAD: " + "" + ahead);
    }

    public static int directionFromString(String s) {
        if (s.equals("north")) {
            return NORTH;
        }
        if (s.equals("west")) {
            return WEST;
        }
        if (s.equals("east")) {
            return EAST;
        }
        if (s.equals("south")) {
            return SOUTH;
        }
        return -1;
    }

    public int getWent() {
        return went;
    }

    public void setWent(int went) {
        this.went = went;
    }

    public int getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(int cameFrom) {
        this.cameFrom = cameFrom;
    }

    public static String getString(int direction) {
        String s = "";
        switch(direction) {
            case NORTH:
                s = "north";
                break;
            case SOUTH:
                s = "south";
                break;
            case EAST:
                s = "east";
                break;
            case WEST:
                s = "west";
                break;
        }
        return s;
    }

    public static int getRight(int direction) {
        return (direction + 1) % 4;
    }

    public static int getLeft(int direction) {
        return (direction + 3) % 4;
    }

    public static int getReverse(int direction) {
        return (direction + 2) % 4;
    }

    public int getWays() {
        return ways;
    }

    public void setWays(int ways) {
        this.ways = ways;
    }

    public boolean[] getOpenDirections() {
        return openDirections;
    }

    public void setDirectionTried(int direction, boolean value) {
        directionsTried[direction] = value;
    }

    public void setDirectionOpen(int direction, boolean value) {
        openDirections[direction] = value;
    }

    public void setOpenDirections(boolean[] openDirections) {
        this.openDirections = openDirections;
    }

    public boolean[] getDirectionsTried() {
        return directionsTried;
    }

    public void setDirectionsTried(boolean[] directionsTried) {
        this.directionsTried = directionsTried;
    }

    public String toString() {
        String s = "";
        s += "Intersection>";
        s += getString(cameFrom) + ": ";
        for (int i = 0; i < 4; i++) {
            s += getString(i) + ": O" + openDirections[i] + ",T" + directionsTried[i] + " | ";
        }
        return s;
    }
}
