package de.cnc.expression.prepostfixoperators;

import de.cnc.expression.AbstractRuntimeEnvironment;
import de.cnc.expression.AbstractToken;
import de.cnc.expression.Types;
import de.cnc.expression.exceptions.ExpressionEvaluationException;
import de.cnc.expression.exceptions.ExpressionParseException;

/**
 * Abstrakte Oberklasse aller Prefix- und Postfix-Operatoren.
 * 
 * <br/>
 * <b>Author:</b> <a href="http://www.heinerkuecker.de" target="_blank">Heiner K�cker</a><br/>
 * <br/>
 *
 * @author Heiner K�cker
 * @version $Id: AbstractPrePostfixOperator.java 373 2005-08-30 14:37:33Z marschal $
 */
public abstract class AbstractPrePostfixOperator {

    protected int iLine;

    protected int iCol;

    protected String strSource;

    protected int iSrcLength = 0;

    protected String strOriginalSource;

    public AbstractPrePostfixOperator(String paStrOriginalSource, int paIntLine, int paIntCol) {
        this.strOriginalSource = paStrOriginalSource;
        this.iLine = paIntLine;
        this.iCol = paIntCol;
    }

    /**
   * gets the the source line number
   */
    public int getLine() {
        return this.iLine;
    }

    /**
   * gets the col position in the source code line
   */
    public int getCol() {
        return this.iCol;
    }

    public String getSource() {
        return this.strSource;
    }

    public int getSrcLength() {
        return this.iSrcLength;
    }

    public String getOriginalSource() {
        return this.strOriginalSource;
    }

    /**
   * Pr�froutine f�r Variablen-Namen . <br>
   * @see de.cnc.expression.tokencomplex.Variable#parse
   */
    public static boolean isOperator(String strPa) {
        return "!".equals(strPa) || "-".equals(strPa) || "++".equals(strPa);
    }

    /**
   * Parse prefix operators like !b, -i, --i and ++i
   *
   * @return geparsten Operator oder null wenn nichts passendes gefunden
   */
    public static AbstractPrePostfixOperator parsePrefixOperator(String paStr, int paIntLine, int paIntCol, String paStrOriginalSource) throws ExpressionParseException {
        AbstractPrePostfixOperator retOperator = null;
        boolean bFake = ((retOperator = PlusPlusOperator.parse(paStr, paIntLine, paIntCol, paStrOriginalSource)) != null) || ((retOperator = MinusMinusOperator.parse(paStr, paIntLine, paIntCol, paStrOriginalSource)) != null) || ((retOperator = MinusPrefixOperator.parse(paStr, paIntLine, paIntCol, paStrOriginalSource)) != null) || ((retOperator = NotPrefixOperator.parse(paStr, paIntLine, paIntCol, paStrOriginalSource)) != null);
        return retOperator;
    }

    /**
   * Parse postfix operators like i++ and i--
   *
   * @return geparsten Operator oder null wenn nichts passendes gefunden
   */
    public static AbstractPrePostfixOperator parsePostfixOperator(String paStr, int paIntLine, int paIntCol, String paStrOriginalSource) throws ExpressionParseException {
        AbstractPrePostfixOperator retOperator = null;
        boolean bFake = ((retOperator = PlusPlusOperator.parse(paStr, paIntLine, paIntCol, paStrOriginalSource)) != null) || ((retOperator = MinusMinusOperator.parse(paStr, paIntLine, paIntCol, paStrOriginalSource)) != null);
        return retOperator;
    }

    /**
   * debug output
   */
    public String toString() {
        String strRet = getClass().getName() + " source: " + getSource();
        return strRet;
    }

    /**
   * process the operator
   */
    public abstract Object eval(AbstractRuntimeEnvironment pRunEnv, AbstractToken paTok) throws ExpressionEvaluationException;

    /**
   * get the wahrscheinlichen return typen . <br>
   * Wahrscheinlicher Return Type zum vorbeugenden Pr�fen der Funktionsparameter
   * null wenn kein R�ckgabe-Wert,
   * mehrere wenn mehrere m�glich
   * ( z.B. Plus-Operator kann numerisch oder String liefern)
   */
    public Object[] getCheckReturnTypes(final Object[] pArrTypes) throws ExpressionParseException {
        if (!(Types.isInOrUnknown(Number.class, pArrTypes))) {
            throw new ExpressionParseException(getLine(), getCol(), getOriginalSource(), "numeric type expected");
        }
        return new Object[] { Number.class };
    }
}
