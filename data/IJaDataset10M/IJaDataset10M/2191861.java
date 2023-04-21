package server;

import java.util.LinkedList;
import middleend.items.*;

public class MonsterTimes {

    Monster monster;

    long lastMoveTime;

    C target;

    LinkedList<MonsterTimes> mt = new LinkedList<MonsterTimes>();

    boolean active;

    public MonsterTimes(Monster monster, long time, C target, LinkedList<MonsterTimes> mt) {
        this.monster = monster;
        this.lastMoveTime = time;
        this.target = target;
        this.mt = mt;
        this.active = true;
    }

    public void move() {
        if (System.currentTimeMillis() - lastMoveTime > (long) ((1.0 / monster.getSpeed()) * 50000)) {
            lastMoveTime = System.currentTimeMillis();
            while (Math.abs(target.getX() - monster.getX()) > 1 || Math.abs(target.getY() - monster.getY()) > 1) {
                int distX = target.getX() - monster.getX();
                int distY = target.getY() - monster.getY();
                if ((Math.abs(distX) >= Math.abs(distY))) {
                    if (distX < 0) monster.setXY(monster.getX() - 1, monster.getY()); else if (distX > 0) monster.setXY(monster.getX() + 1, monster.getY());
                } else {
                    if (distY < 0) monster.setXY(monster.getX(), monster.getY() - 1); else if (distY > 0) monster.setXY(monster.getX(), monster.getY() + 1);
                }
            }
        }
        if (!monster.isInAggroRange(target)) {
        }
    }
}
