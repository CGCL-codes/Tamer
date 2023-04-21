package net.sourceforge.huntforgold.logic;

import net.sourceforge.huntforgold.ai.AIManager;
import net.sourceforge.huntforgold.gui.LandGUI;
import net.sourceforge.huntforgold.model.Fleet;
import net.sourceforge.huntforgold.model.Position;
import net.sourceforge.huntforgold.model.Ship;
import net.sourceforge.huntforgold.model.Time;
import net.sourceforge.huntforgold.model.Town;
import net.sourceforge.huntforgold.model.World;
import net.sourceforge.huntforgold.model.map.MapManager;
import net.sourceforge.huntforgold.util.MediaTimer;
import net.sourceforge.huntforgold.sound.SndServer;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * The player is walking around on land.
 */
public class LandState extends GameState {

    /** The logger */
    private static Logger log = Logger.getLogger(LandState.class);

    /** Game world */
    private World world;

    /** Helps accessing the map */
    private MapManager mapManager;

    /** timer for framerate synchronization. */
    private MediaTimer timer;

    /** The time */
    private Time time;

    /** SoundServer */
    private SndServer snd;

    /**
   * Creates a new LandState
   */
    public LandState() {
        super(LAND);
        setRenderer(new LandGUI());
        world = World.getWorld();
        mapManager = MapManager.getMapManager();
        timer = MediaTimer.getMediaTimer();
        time = Time.getTime();
        snd = SndServer.getSndServer();
    }

    /**
   * Called when this state is entered
   */
    public void enter() {
        timer.reset();
        AIManager.getAIManager().setMode(AIManager.MAIN);
        snd.addToMusicPlaylist(SndServer.MEN_WALKING_ON_LAND);
        snd.play();
    }

    /** 
   * Updates the internal state - called once per frame.
   */
    public void update() {
        timer.update();
        time.increaseTime();
        double up = 0;
        double left = 0;
        if (keyboard.isUp()) {
            up = 1;
        } else if (keyboard.isDown()) {
            up = -1;
        }
        if (keyboard.isLeft()) {
            left = 1;
        } else if (keyboard.isRight()) {
            left = -1;
        }
        double delta = timer.getDelta();
        Position oldPosition = player.getCrewPosition();
        double deltaX = delta * left * 0.01;
        double deltaY = delta * up * 0.01;
        Position newPosition = oldPosition.move(deltaX, deltaY);
        boolean continueFlag = true;
        boolean walking = (up != 0) || (left != 0);
        if (walking) {
            player.updateCrewAnimationCounter(delta);
        }
        if (!mapManager.isInWorld(newPosition)) {
            newPosition = oldPosition;
            continueFlag = false;
        }
        if (continueFlag) {
            Set ships = world.getShips(newPosition.move(0.005, 0.005), newPosition.move(-0.005, -0.005));
            if (ships.size() > 0) {
                Fleet fleet = player.getFleet();
                Iterator it = ships.iterator();
                while (it.hasNext()) {
                    Ship current = (Ship) it.next();
                    if (fleet.contains(current)) {
                        continueFlag = false;
                        game.setCurrentState(SAILING);
                    }
                }
            }
        }
        if (continueFlag && mapManager.isInWater(newPosition)) {
            newPosition = oldPosition;
            continueFlag = false;
        }
        if (continueFlag) {
            Position topLeft = oldPosition.move(0.01, 0.01);
            Position bottomRight = oldPosition.move(-0.01, -0.01);
            Set towns = world.getTowns(topLeft, bottomRight, time.getYear());
            if (towns.size() > 0) {
                Iterator it = towns.iterator();
                Town town = (Town) it.next();
                log.info("Entering town: " + town.getName());
                player.setCurrentTown(town);
                game.setCurrentState(NEAR_TOWN_LAND);
                continueFlag = false;
            }
        }
        if (keyboard.isC()) {
            game.setCurrentState(CAPTAINS_CABIN);
        } else {
            if (continueFlag) {
                player.setCrewPosition(newPosition);
                mapManager.centerOnLocation(newPosition);
            }
        }
    }

    /**
   * Called whenever the game leaves this state.
   */
    public void leave() {
        AIManager.getAIManager().setMode(AIManager.NO_AI);
        snd.removeFromMusicPlaylist(SndServer.MEN_WALKING_ON_LAND);
        snd.stop();
    }
}
