package plugin.bonustokens;

import pcgen.core.bonus.BonusObj;

/**
 * <code>SkillPoints</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public final class SkillPool extends BonusObj {

    private static final String[] bonusHandled = { "SKILLPOOL" };

    protected boolean parseToken(final String token) {
        if (token.startsWith("CLASS")) {
            addBonusInfo(token.replace('=', '.'));
        } else if (token.startsWith("LEVEL")) {
            addBonusInfo(token.replace('=', '.'));
        } else if ("NUMBER".equals(token)) {
            addBonusInfo(token);
        } else {
            return false;
        }
        return true;
    }

    protected String unparseToken(final Object obj) {
        return (String) obj;
    }

    protected String[] getBonusesHandled() {
        return bonusHandled;
    }
}
