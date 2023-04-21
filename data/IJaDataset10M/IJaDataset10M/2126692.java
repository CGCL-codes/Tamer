package org.fudaa.ctulu;

import org.nfunk.jep.Variable;

/**
 * Des validateurs d'expressions formules.
 * 
 * @author Fred Deniger, marchand@deltacad.fr
 * @version $Id: CtuluExprStringValidators.java,v 1.6 2007-04-16 16:33:53 deniger Exp $
 */
public class CtuluExprStringValidators {

    /**
   * @author Fred Deniger
   * @version $Id: CtuluExprStringValidators.java,v 1.6 2007-04-16 16:33:53 deniger Exp $
   */
    public static class DoubleOption extends CtuluExprStringValidator {

        public DoubleOption() {
            this(new CtuluExpr());
        }

        public DoubleOption(final CtuluExpr _expr) {
            super(_expr, true);
        }

        public Object stringToValue(final String _string) {
            if (_string == null || _string.trim().length() == 0) {
                return null;
            }
            try {
                return Double.valueOf(_string);
            } catch (final NumberFormatException e) {
            }
            expr_.getParser().parseExpression(_string);
            if (expr_.getParser().hasError()) {
                return null;
            }
            final Variable[] vars = expr_.findUsedVar();
            if (vars == null || vars.length == 0) {
                return CtuluLib.getDouble(expr_.getParser().getValue());
            }
            return expr_;
        }
    }

    /**
   * @author Fred Deniger
   * @version $Id: CtuluExprStringValidators.java,v 1.6 2007-04-16 16:33:53 deniger Exp $
   */
    public static class IntegerOption extends CtuluExprStringValidator {

        public IntegerOption() {
            this(new CtuluExpr());
        }

        public IntegerOption(final CtuluExpr _expr) {
            super(_expr, true);
        }

        public Object stringToValue(final String _string) {
            expr_.getParser().parseExpression(_string);
            if (expr_.getParser().hasError()) {
                return null;
            }
            final Variable[] vars = expr_.findUsedVar();
            if (vars == null || vars.length == 0) {
                return Integer.valueOf((int) expr_.getParser().getValue());
            }
            return expr_;
        }
    }

    /**
   * @author Fred Deniger
   * @version $Id: CtuluExprStringValidators.java,v 1.6 2007-04-16 16:33:53 deniger Exp $
   */
    public static class LongOption extends CtuluExprStringValidator {

        public LongOption() {
            this(new CtuluExpr());
        }

        public LongOption(final CtuluExpr _expr) {
            super(_expr, true);
        }

        public Object stringToValue(final String _string) {
            expr_.getParser().parseExpression(_string);
            if (expr_.getParser().hasError()) {
                return null;
            }
            final Variable[] vars = expr_.findUsedVar();
            if (vars == null || vars.length == 0) {
                return Long.valueOf((long) expr_.getParser().getValue());
            }
            return expr_;
        }
    }

    /**
   * Un validateur pour une string retourn�e. Si le validator n'est pas
   * sur un mode expression, retourne la chaine. Sinon retourne la valeur de
   * l'expression.
   * 
   * @author marchand@deltacad.Fr
   * @version $Id: CtuluExprStringValidators.java,v 1.6 2007-04-16 16:33:53 deniger Exp $
   */
    public static class StringOption extends CtuluExprStringValidator {

        public StringOption() {
            this(new CtuluExpr());
        }

        /**
     * Par defaut, le champ n'attend pas de formule.
     * @param _expr 
     */
        public StringOption(final CtuluExpr _expr) {
            super(_expr, false);
        }

        public Object stringToValue(final String _string) {
            if (_string == null || _string.trim().length() == 0) {
                return isEmptyAccepted_ ? "" : null;
            }
            if (!exprIsExpected_) return _string;
            expr_.getParser().parseExpression(_string);
            if (expr_.getParser().hasError()) {
                return null;
            }
            return expr_;
        }

        public boolean isStringValid(final String _string) {
            if (_string == null || _string.length() == 0) {
                return isEmptyAccepted_;
            }
            if (exprIsExpected_) {
                expr_.getParser().parseExpression(_string);
                return !(expr_.getParser().hasError());
            }
            return true;
        }
    }
}
