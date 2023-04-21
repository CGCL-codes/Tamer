package com.elhakimz.matra.util;

import com.jme.scene.Node;
import com.elhakimz.matra.battle.BattleManager;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: abiel
 * Date: Feb 15, 2010
 * Time: 5:15:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class GlobalVars {

    public static Node tracksNode = new Node(), lightNode = new Node(), sceneNode = new Node();

    public static Node pickNode = new Node();

    public static Node desktopNode = new Node();

    public static float NWCOORD_LAT = 0;

    public static float NWCOORD_LON = 0;

    public static float SECOORD_LAT = 0;

    public static float SECOORD_LON = 0;

    public static BattleManager battleManager = new BattleManager();

    private GlobalVars() {
    }
}
