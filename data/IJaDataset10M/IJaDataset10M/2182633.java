package plugin.bonustokens;

import pcgen.core.bonus.BonusObj;

/**
 * <code>Feat</code>
 *
 * @author  Devon Jones <soulcatcher@eviloft.org>
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public final class Feat extends BonusObj {

    private static final String[] bonusHandled = { "FEAT" };

    protected boolean parseToken(final String token) {
        if ("POOL".equals(token)) {
            addBonusInfo(token);
            return true;
        }
        return false;
    }

    protected String unparseToken(final Object obj) {
        return (String) obj;
    }

    protected String[] getBonusesHandled() {
        return bonusHandled;
    }
}
