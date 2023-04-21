package abid.password.types;

import abid.password.StatefulMutablePassword;

/**
 * Rotates the mutable part of the password using the state.
 * 
 * The state will need to be stored somewhere and set when the mutable password
 * is initialised.
 * 
 * @author Abid
 * 
 */
public class RotatingPassword extends StatefulMutablePassword {

    /** How many characters to rotate. */
    public static final int ROTATING_WIDTH = 1;

    /** Password type name. */
    public static final String PASSWORD_TYPE = "rotating";

    /**
   * Takes the String password, which is then separated into the text and
   * mutable block. The mutable block is split into the password type and
   * expression.
   * 
   * @param password
   */
    public RotatingPassword(String password) {
        super(password);
    }

    @Override
    public String getEvaluatedPassword() {
        String expression = getExpression();
        int normalisedState = normaliseState(expression.length(), getState());
        String subString = expression.substring(normalisedState, normalisedState + ROTATING_WIDTH);
        String evaluatedPassword = getText() + subString;
        return evaluatedPassword;
    }

    private int normaliseState(int maxLength, int currentState) {
        if (currentState < 0) {
            currentState *= -1;
        }
        return currentState % maxLength;
    }

    @Override
    public boolean confirmPassword(String confirmPassword) {
        String evaluatedPassword = getEvaluatedPassword();
        if (evaluatedPassword.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    public String getPasswordType() {
        return PASSWORD_TYPE;
    }
}
