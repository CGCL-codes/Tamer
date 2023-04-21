package pcgen.core.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PCStat;
import pcgen.core.PlayerCharacter;
import pcgen.core.bonus.BonusObj;
import pcgen.core.bonus.BonusUtilities;

public class StatAnalysis {

    public static int getBaseStatFor(PlayerCharacter aPC, PCStat stat) {
        if (!aPC.hasUnlockedStat(stat)) {
            Number val = aPC.getLockedStat(stat);
            if (val != null) {
                return val.intValue();
            }
        }
        int z = aPC.getVariableValue("BASE." + stat.getAbb(), "").intValue();
        if (z != 0) {
            return z;
        }
        Integer score = aPC.getAssoc(stat, AssociationKey.STAT_SCORE);
        return score == null ? 0 : score;
    }

    public static List<BonusObj> getBonusListOfType(PlayerCharacter pc, final String aType, final String aName) {
        final List<BonusObj> aList = new ArrayList<BonusObj>();
        for (PCStat stat : pc.getUnmodifiableStatList()) {
            aList.addAll(BonusUtilities.getBonusFromList(stat.getSafeListFor(ListKey.BONUS), aType, aName));
        }
        return aList;
    }

    public static int getModForNumber(PlayerCharacter ownerPC, int aNum, PCStat stat) {
        String aString = stat.getStatMod();
        aString = aString.replaceAll(Pattern.quote("SCORE"), Integer.toString(aNum));
        return ownerPC.getVariableValue(aString, "").intValue();
    }

    public static int getStatModFor(PlayerCharacter ownerPC, PCStat stat) {
        return ownerPC.getVariableValue(stat.getStatMod(), "STAT:" + stat.getAbb()).intValue();
    }

    /**
	 * Calculate the total for the requested stat. If equipment or temporary
	 * bonuses should be excluded, getPartialStatFor should be used instead.
	 *
	 * @param aStat The abbreviation of the stat to be calculated
	 * @return The value of the stat
	 */
    public static int getTotalStatFor(PlayerCharacter ownerPC, PCStat stat) {
        int y = getBaseStatFor(ownerPC, stat);
        final PlayerCharacter aPC = ownerPC;
        if (!aPC.hasUnlockedStat(stat)) {
            Number val = aPC.getLockedStat(stat);
            if (val != null) {
                return val.intValue();
            }
        }
        y += aPC.getTotalBonusTo("STAT", stat.getAbb());
        return y;
    }

    /**
	 * Retrieve a correctly calculated attribute value where one or more
	 * types are excluded.
	 *
	 * @param aStat The abbreviation of the stat to be calculated
	 * @param useTemp Should temporary bonuses be included?
	 * @param useEquip Should equipment bonuses be included?
	 * @return The value of the stat
	 */
    public static int getPartialStatFor(PlayerCharacter aPC, PCStat stat, boolean useTemp, boolean useEquip) {
        int y = getBaseStatFor(aPC, stat);
        Number val = aPC.getLockedStat(stat);
        if (val != null) {
            return val.intValue();
        }
        y += aPC.getPartialStatBonusFor(stat, useTemp, useEquip);
        return y;
    }
}
