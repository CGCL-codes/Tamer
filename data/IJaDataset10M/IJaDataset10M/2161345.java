package trstudio.blueboxalife.bos.exception;

import trstudio.blueboxalife.bos.BOSVariableType;

/**
 * Exception lancé par une erreur liée à une incompatibilité de variable BOS.
 *
 * @author Sebastien Villemain
 */
public class BOSVariableException extends BOSException {

    public BOSVariableException(BOSVariableType variableTypeExpected, BOSVariableType variableTypeGot) {
        this(null, variableTypeExpected, variableTypeGot);
    }

    public BOSVariableException(String specialMessage, BOSVariableType variableTypeExpected, BOSVariableType variableTypeGot) {
        super(((specialMessage != null) ? specialMessage + " " : "") + "BOS variable type mismatch. Expected: " + variableTypeExpected + "; Got: " + variableTypeGot + ".");
    }
}
