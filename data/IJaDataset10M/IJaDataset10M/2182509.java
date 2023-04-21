package plugin.bonustokens;

import pcgen.core.bonus.BonusObj;

/**
 * <code>ToHit</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public final class ToHit extends BonusObj {

    private static final String[] bonusHandled = { "TOHIT" };

    private static final String[] bonusTags = { "TOHIT" };

    protected boolean parseToken(final String token) {
        for (int i = 0; i < bonusTags.length; ++i) {
            if (bonusTags[i].equals(token)) {
                addBonusInfo(new Integer(i));
                return true;
            }
        }
        if (token.startsWith("TYPE=")) {
            addBonusInfo(token.replace('=', '.'));
        } else {
            addBonusInfo(token);
        }
        return true;
    }

    protected String unparseToken(final Object obj) {
        if (obj instanceof Integer) {
            return bonusTags[((Integer) obj).intValue()];
        }
        return (String) obj;
    }

    protected String[] getBonusesHandled() {
        return bonusHandled;
    }
}
