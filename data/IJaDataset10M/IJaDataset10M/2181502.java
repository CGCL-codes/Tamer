package abid.password.types;

import abid.password.MutableBlock;
import abid.password.MutablePassword;
import abid.password.MutablePasswordBuilder;
import abid.password.parameters.TimeParameter;

/**
 * Convenient builder class to build Extended password.
 * 
 * @author Abid
 * 
 */
public class ExtendedPasswordBuilder extends MutablePasswordBuilder {

    /**
   * Builds Extended Password.
   */
    public ExtendedPasswordBuilder() {
        super(ExtendedPassword.PASSWORD_TYPE);
    }

    /**
   * Create the mutable password based on the input values.
   * 
   * @param text
   * @param timeValue
   * @return mutable password
   */
    public MutablePassword createPassword(String text, TimeParameter timeValue) {
        return createPassword(text, timeValue.getTextField());
    }

    /**
   * Create the mutable password based on the input values.
   * 
   * @param text
   * @param expression
   * @return mutable password
   */
    public MutablePassword createPassword(String text, String expression) {
        MutableBlock block = createMutableBlock(expression);
        return new ExtendedPassword(text + block);
    }
}
