package org.xteam.kenken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Group {

    public static class Wall {

        int x;

        int y;

        public Wall(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int hashCode() {
            return new Integer(x).hashCode() ^ new Integer(y).hashCode();
        }

        public boolean equals(Object o) {
            return ((Wall) o).x == x && ((Wall) o).y == y;
        }
    }

    private List<Case> cases;

    private String operation;

    private List<Wall> horizontal = new ArrayList<Wall>();

    private List<Wall> vertical = new ArrayList<Wall>();

    private Case head;

    public Group(String operation, List<Case> cases) {
        this.operation = operation;
        this.cases = cases;
        computeBorder();
    }

    private void computeBorder() {
        horizontal.clear();
        vertical.clear();
        head = null;
        for (Case c : cases) {
            testWall(new Wall(c.x(), c.y()), horizontal);
            testWall(new Wall(c.x(), c.y()), vertical);
            testWall(new Wall(c.x(), c.y() + 1), horizontal);
            testWall(new Wall(c.x() + 1, c.y()), vertical);
            if (head == null || c.x() < head.x() || c.y() < head.y()) {
                head = c;
            }
        }
    }

    private void testWall(Wall w, List<Wall> walls) {
        if (walls.contains(w)) walls.remove(w); else walls.add(w);
    }

    public boolean remove(Collection<Case> cases) {
        this.cases.removeAll(cases);
        computeBorder();
        return cases.isEmpty();
    }

    public List<Wall> getHorizontalWalls() {
        return horizontal;
    }

    public List<Wall> getVerticalWalls() {
        return vertical;
    }

    public Case getHead() {
        return head;
    }

    public String getOperation() {
        return operation;
    }
}
