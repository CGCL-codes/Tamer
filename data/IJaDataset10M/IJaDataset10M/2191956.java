package plugin.bonustokens;

import pcgen.core.bonus.MultiTagBonusObj;

/**
 * <code>SkillPoints</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public final class SkillPoints extends MultiTagBonusObj {

    private static final String[] bonusHandled = { "SKILLPOINTS" };

    private static final String[] bonusTags = { "NUMBER" };

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
