package org.jazzteam.edu.rodion.robocode;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class RodiKilla extends AdvancedRobot {

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }

    double moveAmount;

    @Override
    public void run() {
        setAdjustRadarForRobotTurn(true);
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        while (true) {
            turnRadarRight(360);
            ahead(moveAmount);
            turnGunRight(360);
            turnRight(130);
            back(100);
            turnGunRight(360);
        }
    }
}
