package net.sourceforge.huntforgold.ai;

import net.sourceforge.huntforgold.logic.FencingState;
import net.sourceforge.huntforgold.model.fencing.Fencer;
import net.sourceforge.huntforgold.util.MediaTimer;
import org.apache.log4j.Logger;

/**
 * Controls the battle between the men during a sword fight
 */
public class MenFencingAI implements AI {

    /** The logger */
    private static Logger log = Logger.getLogger(MenFencingAI.class);

    /** The singleton instance */
    private static MenFencingAI menFencingAI = null;

    /** Media timer */
    private MediaTimer timer;

    /** The fencer */
    private Fencer fencer;

    /** The player */
    private Fencer player;

    /** FencingState */
    private FencingState fencingState;

    /**
   * Constructor for MenFencingAI
   */
    private MenFencingAI() {
        timer = MediaTimer.getMediaTimer();
        fencingState = FencingState.getFencingState();
    }

    /**
   * Get the MenFencingAI instance
   * @return The MenFencingAI
   */
    public static synchronized MenFencingAI getMenFencingAI() {
        if (menFencingAI == null) {
            menFencingAI = new MenFencingAI();
        }
        return menFencingAI;
    }

    /**
   * This method is called to initialize the AI component.
   */
    public void initialize() {
        fencer = fencingState.getEnemy();
        player = fencingState.getPlayer();
    }

    /**
   * This method is called for each frame update in the
   * in the game. It is up to the AI component to determine
   * if an update should happen.
   */
    public void update() {
        double delta = timer.getDelta();
    }
}
