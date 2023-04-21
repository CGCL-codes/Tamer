package ai;

import java.util.Random;
import settings.Config;
import logic.common.player.Player;
import logic.nodes.TeamNode;
import logic.ships.hunter.Hunter;
import com.jme.intersection.PickResults;
import com.jme.math.Vector3f;

/**
 * Abstract Controller for pilots. Provides basic functionality to compute the current distance 
 * to the {@link Player}'s target, visibility-calculations and other useful stuff for any 
 * Controller for pilots.
 * 
 * @author Wasserleiche
 */
public abstract class AbstractPilotController extends AIController {

    private static final long serialVersionUID = 1L;

    protected Random rand;

    /** The current time this controller is active. */
    protected float targetTime;

    /** The agility determines the possible turning speed of the {@link Hunter}. */
    protected float agility;

    /** {@link PickResults} for computing visibility. */
    protected Player bot;

    protected boolean wasFlying;

    public AbstractPilotController(Player bot) {
        this(bot, 0f);
    }

    public AbstractPilotController(Player bot, float updateDelay) {
        super(bot.getHunter(), updateDelay);
        this.bot = bot;
        rand = new Random();
        agility = bot.getHunter().getTurnSpeed();
    }

    /**
	 * Computes the current distance to the current target of the {@link Player} and returns it.
	 * @return The current distance to the target.
	 */
    protected float getTargetDistance() {
        TeamNode target = bot.getBotTarget();
        return bot.getHunter().getLocalTranslation().distance(target.getLocalTranslation()) - target.getSize();
    }

    @Override
    public void delayedUpdate(float time) {
        targetTime += time;
        if (bot.getHunter().hasAttackingHeatSeeker()) bot.getHunter().useCounterMeasures();
    }

    protected boolean checkAIEnabled() {
        if (!Config.get().getEnableAI()) {
            wasFlying = !bot.getHunter().getMoveForwardController().isStopping();
            bot.getHunter().getMoveForwardController().stop(true);
            return false;
        }
        bot.getHunter().getMoveForwardController().stop(!wasFlying);
        return true;
    }

    /**
	 * Enables/Disables the boost of the {@link Hunter} of the {@link Player}.
	 * @param boost true, if the boost shall be enabled. false, if the boost shall be disabled.
	 */
    protected void boost(boolean boost) {
        bot.getHunter().getMoveForwardController().enableBoost(boost);
    }

    /**
	 * Sets the current target of this pilot. Only the current target-time will be reseted.
	 * @param target The new target of the {@link Player}.
	 */
    public void setTarget() {
        resetTargetTime();
    }

    /**
	 * Resets the current target-time to zero.
	 */
    public void resetTargetTime() {
        targetTime = 0f;
    }

    /**
	 * Returns the current target-time.
	 * @return The target-time.
	 */
    public float getTargetTime() {
        return targetTime;
    }

    protected Vector3f getRandomVector(int min, int max) {
        int x = getRandomValue(min, max);
        int y = getRandomValue(min, max);
        int z = getRandomValue(min, max);
        return new Vector3f(x, y, z);
    }

    protected int getRandomValue(int min, int max) {
        int v = min + rand.nextInt(max - min);
        return rand.nextBoolean() ? v : -v;
    }
}
