package net.sf.vex.css;

import org.w3c.css.sac.LexicalUnit;

/**
 * The CSS white-space property.
 */
public class WhiteSpaceProperty extends AbstractProperty {

    /**
     * Class constructor.
     */
    public WhiteSpaceProperty() {
        super(CSS.WHITE_SPACE);
    }

    /**
     *
     */
    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        if (isWhiteSpace(lu)) {
            return lu.getStringValue();
        } else {
            if (parentStyles != null) {
                return parentStyles.getWhiteSpace();
            } else {
                return CSS.NORMAL;
            }
        }
    }

    /**
     * Returns true if the given lexical unit represents a white space value.
     *
     * @param lu LexicalUnit to check.
     */
    public static boolean isWhiteSpace(LexicalUnit lu) {
        if (lu == null) {
            return false;
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            String s = lu.getStringValue();
            return s.equals(CSS.NORMAL) || s.equals(CSS.PRE) || s.equals(CSS.NOWRAP);
        } else {
            return false;
        }
    }
}
