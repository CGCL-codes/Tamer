package net.sf.openrock.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class World {

    private List<Stone> stones = new ArrayList<Stone>();

    private List<Stone> added = new ArrayList<Stone>();

    private double broomX;

    private double broomY;

    private volatile boolean clear;

    public World() {
    }

    public boolean step(double dt) {
        int steps = 10;
        if (clear) {
            clear = false;
            stones.clear();
        }
        synchronized (added) {
            stones.addAll(added);
            added.clear();
        }
        for (int step = 0; step < steps; step++) {
            List<Stone> remove = new ArrayList<Stone>();
            for (Stone stone : stones) {
                stone.step(dt / steps);
                Vect2d pos = stone.getPosition();
                if (pos.getX() + stone.getRadius() > CurlingConstants.ICE_WIDTH / 2 || pos.getX() - stone.getRadius() < -CurlingConstants.ICE_WIDTH / 2 || pos.getY() - stone.getRadius() > CurlingConstants.TEE_TO_CENTER + CurlingConstants.TWELVE_FEET_RADIUS) {
                    remove.add(stone);
                } else if (!stone.isLive() && !stone.isHit() && pos.getY() - stone.getRadius() < CurlingConstants.TEE_TO_CENTER - CurlingConstants.TEE_TO_HOG) {
                    remove.add(stone);
                }
            }
            stones.removeAll(remove);
            for (int i = 0; i < stones.size(); i++) {
                Stone s1 = stones.get(i);
                for (int j = i + 1; j < stones.size(); j++) {
                    Stone s2 = stones.get(j);
                    if (collides(s1, s2)) {
                        solveCollision(s1, s2);
                        s1.setHit(true);
                        s2.setHit(true);
                    }
                }
            }
        }
        boolean live = false;
        for (Stone stone : stones) {
            if (stone.isLive()) {
                live = true;
                break;
            }
        }
        return live;
    }

    private void solveCollision(Stone s1, Stone s2) {
        Vect2d n = s1.getPosition().minus(s2.getPosition()).normalized();
        Vect2d t = n.cross(-1);
        double r1 = s1.getRadius();
        double r2 = s2.getRadius();
        Vect2d p1 = n.times(-r1);
        Vect2d p2 = n.times(r2);
        Vect2d v1 = s1.getVelocity().plus(Vect2d.cross(s1.getDa(), p1));
        Vect2d v2 = s2.getVelocity().plus(Vect2d.cross(s2.getDa(), p2));
        Vect2d v = v1.minus(v2);
        if (v.dot(n) >= 0) {
            return;
        }
        double m1 = s1.getMass();
        double m2 = s2.getMass();
        double i1 = s1.getMomentOfInertia();
        double i2 = s2.getMomentOfInertia();
        double e = 1.0;
        double f = 0.3;
        double nq = 1 / m1 + 1 / m2;
        double nj = -(1 + e) * v.dot(n) / nq;
        double tq = nq + r1 * r1 / i1 + r2 * r2 / i2;
        double tj = -(1 + e) * v.dot(t) / tq;
        if (Math.abs(tj) > f * nj) {
            tj = Math.signum(tj) * f * nj;
        }
        Vect2d impulse = n.times(nj).plus(t.times(tj));
        s1.applyImpulse(impulse, p1);
        s2.applyImpulse(impulse.times(-1), p2);
    }

    private boolean collides(Stone s1, Stone s2) {
        Vect2d diff = s2.getPosition().minus(s1.getPosition());
        double d = diff.length();
        return d < s1.getRadius() + s2.getRadius();
    }

    public void addStone(Stone s) {
        synchronized (added) {
            added.add(s);
        }
    }

    public void clearStones() {
        clear = true;
    }

    public void replaceStones(List<Stone> ss) {
        clear = true;
        added.clear();
        added.addAll(ss);
    }

    public List<Stone> getStones() {
        return Collections.unmodifiableList(stones);
    }

    public void setBroomLocation(double x, double y) {
        broomX = x;
        broomY = y;
    }

    public double getBroomX() {
        return broomX;
    }

    public double getBroomY() {
        return broomY;
    }

    private static final Vect2d CENTER = new Vect2d(0.0, CurlingConstants.TEE_TO_CENTER);

    public void markFreeGuards(int team) {
        for (Stone s : stones) {
            if (s.getTeam() == team) {
                continue;
            }
            if (s.getPosition().getY() + s.getRadius() >= CurlingConstants.TEE_TO_CENTER) {
                continue;
            }
            double d = s.getPosition().minus(CENTER).length() - s.getRadius();
            if (d <= CurlingConstants.TWELVE_FEET_RADIUS) {
                continue;
            }
            s.setFreeGuard(true);
        }
    }

    public int getBestTeam() {
        if (stones.isEmpty()) {
            return -1;
        }
        List<Stone> sorted = new ArrayList<Stone>(stones);
        Collections.sort(sorted, new BestStoneComp());
        Stone s = sorted.get(0);
        double d = s.getPosition().minus(CENTER).length() - s.getRadius();
        if (d > CurlingConstants.TWELVE_FEET_RADIUS) {
            return -1;
        } else {
            return s.getTeam();
        }
    }

    public int getPoints() {
        if (stones.isEmpty()) {
            return 0;
        }
        List<Stone> sorted = new ArrayList<Stone>(stones);
        Collections.sort(sorted, new BestStoneComp());
        int p = 0;
        int team = sorted.get(0).getTeam();
        for (Stone s : sorted) {
            if (s.getTeam() != team) {
                break;
            }
            double d = s.getPosition().minus(CENTER).length() - s.getRadius();
            if (d > CurlingConstants.TWELVE_FEET_RADIUS) {
                break;
            }
            p++;
        }
        return p;
    }

    private static class BestStoneComp implements Comparator<Stone> {

        @Override
        public int compare(Stone o1, Stone o2) {
            double d1 = o1.getPosition().minus(CENTER).length();
            double d2 = o2.getPosition().minus(CENTER).length();
            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
