package de.cnc.expression.tokencomplex;

import de.cnc.expression.AbstractToken;
import de.cnc.expression.Types;
import de.cnc.expression.exceptions.ExpressionParseException;

/**
 * Member-Dot-Operator <b>&lt;index&gt;.member</b>  zur 
 * Adressierung eines Members eines Objekts . <br>
 *
 * <br/>
 * <b>Author:</b> <a href="http://www.heinerkuecker.de" target="_blank">Heiner K�cker</a><br/>
 * <br/>
 *
 * @author Heiner K�cker
 * @version $Id: AbstractDotOperator.java 373 2005-08-30 14:37:33Z marschal $
 */
public abstract class AbstractDotOperator extends AbstractToken {

    /**
     * der Ausdruck, hinter dem ein Dot-Operator steht
     */
    protected AbstractToken dottedExpression;

    /**
     * constructor
     */
    public AbstractDotOperator(String paStrOriginalSource, int paIntLine, int paIntCol) {
        super(paStrOriginalSource, paIntLine, paIntCol);
    }

    /**
     *
     */
    public static AbstractDotOperator parse(final String pStr, final int pIntLine, final int pIntCol, final String pStrOriginalSource) throws ExpressionParseException {
        if (!pStr.startsWith(".")) {
            return null;
        }
        AbstractDotOperator dotElem = MethodDotOperator.parseMethodDotOperator(pStr, pIntLine, pIntCol, pStrOriginalSource);
        if (dotElem != null) {
            return dotElem;
        }
        dotElem = MemberDotOperator.parseMemberDotOperator(pStr, pIntLine, pIntCol, pStrOriginalSource);
        return dotElem;
    }

    /**
     * set the dotted expression.
     * Setzt den Ausdruck, der am Ende eine Index-Element-Operator []
     * tr�gt. Somit wird sozusagen der hinter dem indizierten Ausdruck stehende
     * Index-Operator vor den indizierten Ausdruck gesetzt.
     * Demzufolge wird erst der indzierte Ausdruck evaluiert und auf dessen Ergebnis
     * dann der Index-Operator angewendet.
     * 
     * @param token
     */
    public void setDottedExpression(AbstractToken pToken) {
        this.dottedExpression = pToken;
        this.iSrcLength += pToken.getSrcLength();
    }

    /**
     * get the wahrscheinlichen return typen . <br>
     * Wahrscheinlicher Return Type zum vorbeugenden Pr�fen der Funktionsparameter
     * null wenn kein R�ckgabe-Wert,
     * mehrere wenn mehrere m�glich
     * ( z.B. Plus-Operator kann numerisch oder String liefern)
     */
    public final Object[] getCheckReturnTypes() throws ExpressionParseException {
        return new Object[] { Types.UNKNOWN_CLASS };
    }
}
