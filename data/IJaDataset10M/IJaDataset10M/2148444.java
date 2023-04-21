package org.jcrpg.world.object;

import org.jcrpg.world.ai.abs.skill.SkillActForm;
import org.jcrpg.world.time.Time;

public class BonusSkillActFormDesc {

    public SkillActForm form;

    /**
	 * Level of the skill for the bonus skill act form use.
	 */
    public int skillLevel = 0;

    public static int FREQUENCY_INSTANT = 0;

    public static int FREQUENCY_MINUTE = 1;

    public static int FREQUENCY_HOUR = 1;

    public static int FREQUENCY_DAY = 2;

    public static int MAX_USE_UNLIMITED = 0;

    /**
	 * Max use of this bonus before exhaust.
	 */
    public int maxUsePerReplenish = MAX_USE_UNLIMITED;

    /**
	 * Frequency type
	 */
    public int replenishFrequency = FREQUENCY_INSTANT;

    public boolean isUsableNow(Time lastUse, Time current) {
        if (replenishFrequency == FREQUENCY_INSTANT) return true;
        if (maxUsePerReplenish == MAX_USE_UNLIMITED) return true;
        if (replenishFrequency == FREQUENCY_MINUTE) {
            if (lastUse.diffSeconds(current) > current.maxSecond) return true;
        } else if (replenishFrequency == FREQUENCY_HOUR) {
            if (lastUse.diffSeconds(current) > current.maxMinute * current.maxSecond) return true;
        } else if (replenishFrequency == FREQUENCY_DAY) {
            if (lastUse.diffSeconds(current) > current.maxHour * current.maxMinute * current.maxSecond) return true;
        }
        return false;
    }
}
