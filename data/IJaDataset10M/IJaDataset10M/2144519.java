package plugin.bonustokens;

import pcgen.core.bonus.MultiTagBonusObj;

/**
 * <code>Combat</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public final class Combat extends MultiTagBonusObj {

    private static final String[] bonusHandled = { "COMBAT" };

    private static final String[] bonusTags = { "AC", "ATTACKS", "ATTACKS-SECONDARY", "BAB", "DAMAGE", "DAMAGESIZE", "DAMAGE-PRIMARY", "DAMAGE-SECONDARY", "DAMAGE-SHORTRANGE", "DEFENSE", "INITIATIVE", "REACH", "TOHIT", "TOHIT-PRIMARY", "TOHIT-SECONDARY", "TOHIT-SHORTRANGE" };

    protected String[] getBonusesHandled() {
        return bonusHandled;
    }

    protected String getBonusTag(final int tagNumber) {
        return bonusTags[tagNumber];
    }

    protected int getBonusTagLength() {
        return bonusTags.length;
    }
}
