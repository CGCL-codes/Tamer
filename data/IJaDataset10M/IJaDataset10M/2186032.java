package org.game.controll;

import java.util.Random;
import org.game.alg.Algorithm1;
import org.game.map.GWMap;
import org.game.obj.building.GWCastle;
import org.game.obj.building.GWFarm;
import org.game.obj.building.GWForest;
import org.game.obj.building.GWIronOrePit;
import org.game.player.GWPlayer;

/**
 * GWController controls order of actions taken by
 * GWPlayerControllers.
 * @author Inborn
 *
 */
public class GWController {

    private GWPlayerController pc1, pc2;

    private int time;

    public static int TIME = 10;

    private static final Random R = new Random();

    private boolean endGame = false;

    public GWController(GWMap map, GWPlayer pl1, GWPlayer pl2) {
        this.pc1 = new Algorithm1(map, pl1);
        this.pc2 = new Algorithm1(map, pl2);
        time = 0;
        pc1.addBuilding(new GWCastle(3, 16, 1));
        pc2.addBuilding(new GWCastle(40, 16, 2));
        map.add(new GWForest(0, 5));
        map.add(new GWForest(40, 5));
        map.add(new GWIronOrePit(0, 50));
        map.add(new GWIronOrePit(40, 50));
        map.add(new GWFarm(2, 30));
        map.add(new GWFarm(40, 30));
    }

    public void action() {
        if (time < TIME) {
            time++;
            return;
        }
        time = 0;
        int r1 = R.nextInt(100);
        int r2 = R.nextInt(100);
        if (r1 > r2) {
            pc1.createAction();
            pc2.createAction();
        } else {
            pc2.createAction();
            pc1.createAction();
        }
        for (int i = 0; i < GWPlayer.UNIT_LIMIT; i++) {
            r1 = R.nextInt(100);
            r2 = R.nextInt(100);
            if (r1 > r2) {
                pc1.gatherResourcesAction(i);
                pc2.gatherResourcesAction(i);
                pc1.bringResourcesAction(i);
                pc2.bringResourcesAction(i);
                pc1.attackAction(i);
                pc2.attackAction(i);
                pc1.moveAction(i);
                pc2.moveAction(i);
            } else {
                pc2.gatherResourcesAction(i);
                pc1.gatherResourcesAction(i);
                pc2.bringResourcesAction(i);
                pc1.bringResourcesAction(i);
                pc2.attackAction(i);
                pc1.attackAction(i);
                pc2.moveAction(i);
                pc1.moveAction(i);
            }
        }
        pc1.releaseUnits();
        pc2.releaseUnits();
        if (pc1.getPlayer().getCastle().isDead()) {
            endGame = true;
        }
        if (pc2.getPlayer().getCastle().isDead()) {
            endGame = true;
        }
    }

    public boolean isEndGame() {
        return endGame;
    }
}
