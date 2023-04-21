package abid.password.types;

import java.util.Map;
import abid.password.PasswordException;
import abid.password.StatefulMutablePassword;
import abid.password.evaluator.EvaluationException;
import abid.password.evaluator.ExpressionEvaluator;
import abid.password.parameters.Parameter;
import abid.password.parameters.ParameterRegistery;

/**
 * Basically, a Caesar cipher.
 * 
 * Evaluates the password based on a state value, the state can be set before
 * evaluating the password.
 * 
 * @author Abid
 * 
 */
public class CaesarCipherPassword extends StatefulMutablePassword {

    public static final String PASSWORD_TYPE = "caesarCipher";

    public static final String STATE_EXPRESSION_PARAM = "state";

    /**
   * Constructs a Caesar Cipher Password object with the password argument.
   * 
   * @param password
   */
    public CaesarCipherPassword(String password) {
        super(password);
    }

    /**
   * Shift the alphabet (A-Z) by the argument value.
   * 
   * @param shiftBy
   * @return shifted value
   */
    protected String getShiftedPassword(int shiftBy) {
        StringBuilder shiftPassword = new StringBuilder();
        for (char character : getText().toCharArray()) {
            if (character >= 65 && character <= 90) {
                char shiftedChar = (char) ((((character - 65) + shiftBy) % 26) + 65);
                shiftPassword.append(shiftedChar);
            } else if (character >= 97 && character <= 122) {
                char shiftedChar = (char) ((((character - 97) + shiftBy) % 26) + 97);
                shiftPassword.append(shiftedChar);
            } else {
                shiftPassword.append(character);
            }
        }
        return shiftPassword.toString();
    }

    @Override
    public String getEvaluatedPassword() throws EvaluationException {
        String expression = getExpression();
        int shiftBy = 0;
        if (STATE_EXPRESSION_PARAM.equalsIgnoreCase(expression)) {
            shiftBy = getState();
        } else {
            ExpressionEvaluator expressionEvaluator = getEvaluator();
            Map<String, Parameter> parameters = ParameterRegistery.getParameters();
            String evaluation = expressionEvaluator.evaluate(expression, parameters);
            shiftBy = Integer.valueOf(evaluation);
        }
        return getShiftedPassword(shiftBy);
    }

    @Override
    public boolean confirmPassword(String confirmPassword) throws PasswordException {
        try {
            String evaluatedPassword = getEvaluatedPassword();
            return evaluatedPassword.equals(confirmPassword);
        } catch (EvaluationException e) {
            throw new PasswordException(e);
        }
    }

    public String getPasswordType() {
        return PASSWORD_TYPE;
    }
}
