package jxl.biff.formula;

/**
 * A cell reference in a formula
 */
class Power extends BinaryOperator implements ParsedThing {

    /** 
   * Constructor
   */
    public Power() {
    }

    public String getSymbol() {
        return "^";
    }

    /**
   * Abstract method which gets the token for this operator
   *
   * @return the string symbol for this token
   */
    Token getToken() {
        return Token.POWER;
    }

    /**
   * Gets the precedence for this operator.  Operator precedents run from 
   * 1 to 5, one being the highest, 5 being the lowest
   *
   * @return the operator precedence
   */
    int getPrecedence() {
        return 1;
    }
}
