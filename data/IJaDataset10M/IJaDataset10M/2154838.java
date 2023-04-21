package edu.uiuc.android.scorch.ai;

import edu.uiuc.android.scorch.ScorchedPlayer;
import java.util.Random;

/**
 * This is an example AI class To create an AI class just extend AIPlayer and implement the takeTurn
 * function. The AI runs on a separate thread.
 */
public class DumbAIPlayer extends AIPlayer {

    /**
     * Auto-generated UID for serialization
     */
    private static final long serialVersionUID = -8863909908757901251L;

    Random mRandom;

    /**
     * Create a new DumbAIPlayer
     * 
     * @param id The unique ID for the AI
     * @param name The name of the AI
     * @param color The color for the player's tank
     */
    public DumbAIPlayer(int id, String name, int color) {
        super(id, name, color);
        mRandom = new Random();
    }

    /**
     * @see edu.uiuc.android.scorch.ai.AIPlayer#takeTurn()
     */
    @Override
    public void takeTurn() throws InterruptedException {
        for (ScorchedPlayer player : getPlayers()) {
            player.getHealth();
        }
        getTerrain().isTerrain(0, 0);
        setAngle(180);
        Thread.sleep(500);
        setAngle(0);
        Thread.sleep(500);
        setAngle(mRandom.nextInt(181));
        Thread.sleep(500);
        setAngle(mRandom.nextInt(181));
        Thread.sleep(500);
        setAngle(mRandom.nextInt(181));
        Thread.sleep(500);
        setPower(mRandom.nextInt(MAX_POWER + 1));
        fireWeapon();
    }

    /**
     * Buy weapons as appropriate
     */
    @Override
    public void doShopping() {
    }
}
