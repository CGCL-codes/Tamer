package pcgen.core.bonus;

import pcgen.core.bonus.util.SpellCastInfo;

/**
 * <code>SpellKnownMult</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
final class SpellKnownMult extends BonusObj {

    private static final String[] bonusHandled = { "SPELLKNOWNMULT" };

    boolean parseToken(final String token) {
        int idx = token.indexOf(";LEVEL=");
        if (idx < 0) {
            idx = token.indexOf(";LEVEL.");
        }
        if (idx < 0) {
            return false;
        }
        final String level = token.substring(idx + 7);
        addBonusInfo(new SpellCastInfo(token.substring(0, idx), level));
        return true;
    }

    String unparseToken(final Object obj) {
        final StringBuffer sb = new StringBuffer(30);
        final SpellCastInfo sci = (SpellCastInfo) obj;
        if (sci.getType() != null) {
            sb.append("TYPE.").append(((SpellCastInfo) obj).getType());
        } else if (sci.getPcClassName() != null) {
            sb.append("CLASS.").append(((SpellCastInfo) obj).getPcClassName());
        }
        sb.append(";LEVEL.").append(((SpellCastInfo) obj).getLevel());
        return sb.toString();
    }

    String[] getBonusesHandled() {
        return bonusHandled;
    }
}
