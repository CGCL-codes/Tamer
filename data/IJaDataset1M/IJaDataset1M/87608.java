package org.chernovia.lib.sims.ca.fishbowl;

import org.chernovia.lib.graphics.lib3d.P2D;
import org.chernovia.lib.sims.ca.CA_Engine;

public class FishBowl2D extends CA_Engine {

    public boolean DEBUG = false, FISHWRAP = true, WRAPFLIP = false, WRAPRAND = false, GRAVITY = false, SYMETTRY = true, MAGNET = false;

    public static final int REP_NONE = 0, REP_DEFLECT = 1, REP_RAND = 2, REP_FLIP = 3;

    public static final String[] RepulseStyles = { "None", "Deflect", "Mingle", "Huddle" };

    private int REPULSE_STYLE = REP_DEFLECT;

    private Fish2D[] fish;

    private P2D magnet;

    private double magStr = 1;

    private double bowlsize = 1000.0;

    private int num_fish;

    public FishBowl2D() {
        this(8, 8, 16, 24);
    }

    public FishBowl2D(int n, double rad, double vol, int tail) {
        super();
        initFish(n, rad, vol, tail);
    }

    private void initFish(int n, double rad, double vol, int tail) {
        num_fish = n;
        fish = new Fish2D[num_fish];
        for (int i = 0; i < num_fish; i++) fish[i] = new Fish2D(newFishLoc(), num_fish, rad, vol, tail);
        magnet = new P2D(0, 0);
    }

    public P2D getMagnet() {
        return magnet;
    }

    public double getMagStr() {
        return magStr;
    }

    public void setMagStr(double str) {
        magStr = str;
    }

    public P2D newFishLoc() {
        return new P2D(Math.random() * bowlsize, Math.random() * bowlsize);
    }

    public int getNumFish() {
        return fish.length;
    }

    public Fish2D[] getFish() {
        return fish;
    }

    public Fish2D getFish(int i) {
        return fish[i];
    }

    public double getBowlSize() {
        return bowlsize;
    }

    public void setRepulsion(int r) {
        REPULSE_STYLE = r;
    }

    @Override
    public void nextTick() {
        if (isPaused()) return;
        mingle();
        for (int i = 0; i < num_fish; i++) {
            fish[i].nextTail();
        }
        incrTick();
    }

    public void mingle() {
        for (int i = 0; i < num_fish; i++) {
            P2D tv = new P2D(0, 0);
            for (int j = 0; j < num_fish; j++) {
                if (i != j) {
                    tv.tr(getVec(i, j));
                }
            }
            if (MAGNET) {
                tv.tr(getMagVec(fish[i]));
                tv.scale(1 / (double) (num_fish + 1));
            } else tv.scale(1 / (double) num_fish);
            fish[i].tr(tv);
            fish[i].setVec(tv);
            if (fishWrap(i)) {
                if (WRAPFLIP) fish[i].flipMatrix(); else if (WRAPRAND) fish[i].generateRandomMatrix();
            }
        }
    }

    public P2D getMagVec(Fish2D f) {
        P2D vec = new P2D(0, 0);
        double angle = f.angle(magnet);
        double dist = f.distance(magnet);
        if (GRAVITY) {
            vec.x = (Math.sin(angle) * (bowlsize * 2) * magStr) / dist;
            vec.y = (Math.cos(angle) * (bowlsize * 2) * magStr) / dist;
        } else {
            vec.x = Math.sin(angle) * magStr;
            vec.y = Math.cos(angle) * magStr;
        }
        if (SYMETTRY && (f.y > magnet.y)) vec.mult(-1);
        return vec;
    }

    public P2D getVec(int f1, int f2) {
        P2D vec = new P2D(0, 0);
        double angle = fish[f1].angle(fish[f2]);
        double dist = fish[f1].distance(fish[f2]);
        if (dist < (fish[f1].radius + fish[f2].radius)) {
            switch(REPULSE_STYLE) {
                case REP_DEFLECT:
                    fish[f1].matrix[f2] *= -1;
                    break;
                case REP_RAND:
                    fish[f1].generateRandomMatrix();
                    break;
                case REP_FLIP:
                    fish[f1].flipMatrix();
                    break;
            }
            fish[f1].collidedFish = f2;
            if (DEBUG) System.out.println("Collision detected: " + f1 + " -> " + f2);
            return vec;
        }
        if (GRAVITY) {
            vec.x = (Math.sin(angle) * (bowlsize * 2) * fish[f1].matrix[f2]) / dist;
            vec.y = (Math.cos(angle) * (bowlsize * 2) * fish[f1].matrix[f2]) / dist;
        } else {
            vec.x = Math.sin(angle) * fish[f1].matrix[f2];
            vec.y = Math.cos(angle) * fish[f1].matrix[f2];
        }
        if (SYMETTRY && (fish[f1].y > fish[f2].y)) vec.mult(-1);
        return vec;
    }

    public boolean fishWrap(int f) {
        double v = 0;
        boolean wrap = false;
        v = fish[f].x - bowlsize;
        if (v > 0) {
            fish[f].x = (-bowlsize) + v;
            wrap = true;
        } else if (v < (-bowlsize * 2)) {
            fish[f].x = bowlsize + (v + (bowlsize * 2));
            wrap = true;
        }
        v = fish[f].y - bowlsize;
        if (v > 0) {
            fish[f].y = (-bowlsize) + v;
            wrap = true;
        } else if (v < (-bowlsize * 2)) {
            fish[f].y = bowlsize + (v + (bowlsize * 2));
            wrap = true;
        }
        return wrap;
    }

    public double avgFishProx(int f) {
        double sumDist = 0;
        for (int i = 0; i < num_fish; i++) {
            if (f != i) sumDist += fish[f].distance(fish[i]);
        }
        return sumDist / ((num_fish - 1));
    }
}
