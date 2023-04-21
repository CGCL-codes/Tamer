package de.cnc.expression.functions;

import java.math.BigDecimal;
import de.cnc.expression.AbstractRuntimeEnvironment;
import de.cnc.expression.exceptions.ExpressionEvaluationException;
import de.cnc.expression.exceptions.ExpressionParseException;

/**
 * acos( &lt;valueNum&gt; ) . <br>
 * Arcus Cosinus.<br>
 *
 * <br/>
 * <b>Author:</b> <a href="http://www.heinerkuecker.de" target="_blank">Heiner K�cker</a><br/>
 * <br/>
 *
 * @author Heiner K�cker
 * @version $Id: ArcusCosinusFunction.java 373 2005-08-30 14:37:33Z marschal $
 */
public class ArcusCosinusFunction extends AbstractFunction {

    /**
   * constructor
   */
    public ArcusCosinusFunction(String paStrOriginalSource, int paIntLine, int paIntCol) {
        super(paStrOriginalSource, paIntLine, paIntCol);
    }

    /**
   * process the token
   */
    public final Object eval(final AbstractRuntimeEnvironment pRunEnv) throws ExpressionEvaluationException {
        Number numValue = getNumberParamNoNull(pRunEnv, 0);
        try {
            double acosValue = Math.acos(numValue.doubleValue());
            return new BigDecimal(acosValue);
        } catch (Exception e) {
            throw new ExpressionEvaluationException(getLine(), getCol(), getSource(), getOriginalSource(), "" + e);
        }
    }

    /**
   * get the wahrscheinlichen return typen . <br>
   * Wahrscheinlicher Return Type zum vorbeugenden Pr�fen der Funktionsparameter
   * null wenn kein R�ckgabe-Wert,
   * mehrere wenn mehrere m�glich
   * ( z.B. Plus-Operator kann numerisch oder String liefern)
   */
    public final Object[] getCheckReturnTypes(final AbstractRuntimeEnvironment pRunEnv) throws ExpressionParseException {
        checkParamCount(1);
        return checkParameterForNumber(0, pRunEnv);
    }
}
