package pcgen.core;

import pcgen.util.BigDecimalHelper;
import java.math.BigDecimal;

/**
 * @author Bryan McRoberts <merton_monk@users.sourceforge.net>
 * @version $Revision: 1.4 $
 */
public final class SkillUtilities {

    private SkillUtilities() {
    }

    /**
	 * Returns the maximum number of ranks a character can have in a class skill
	 * at the specified level. <p/>Should this be moved to PCClass?
	 *
	 * @param level
	 *            character level to get max skill ranks for
	 * @param pc
	 * @return The maximum allowed skill ranks
	 */
    public static BigDecimal maxClassSkillForLevel(final int level, final PlayerCharacter pc) {
        LevelInfo lInfo = (LevelInfo) Globals.getLevelInfo().get(String.valueOf(level));
        if (lInfo == null) lInfo = (LevelInfo) Globals.getLevelInfo().get("LEVEL");
        if ((level > 0) && lInfo != null) {
            return lInfo.getMaxClassSkillRank(level, pc);
        }
        return BigDecimalHelper.ZERO;
    }

    /**
	 * Returns the maximum number of ranks a character can <p/>have in a
	 * cross-class skill at the specified level.
	 *
	 * @param level
	 *            character level to get max skill ranks for
	 * @param pc
	 * @return The maximum allowed skill ranks
	 */
    public static BigDecimal maxCrossClassSkillForLevel(final int level, final PlayerCharacter pc) {
        LevelInfo lInfo = (LevelInfo) Globals.getLevelInfo().get(String.valueOf(level));
        if (lInfo == null) lInfo = (LevelInfo) Globals.getLevelInfo().get("LEVEL");
        if ((level > 0) && lInfo != null) {
            return lInfo.getMaxCrossClassSkillRank(level, pc);
        }
        return BigDecimalHelper.ZERO;
    }
}
