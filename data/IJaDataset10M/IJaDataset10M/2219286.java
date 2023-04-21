package jxl.biff.formula;

/**
 * A cell reference in a formula
 */
class UnaryMinus extends UnaryOperator implements ParsedThing {

    /** 
   * Constructor
   */
    public UnaryMinus() {
    }

    public String getSymbol() {
        return "-";
    }

    /**
   * Abstract method which gets the token for this operator
   *
   * @return the string symbol for this token
   */
    Token getToken() {
        return Token.UNARY_MINUS;
    }

    /**
   * Gets the precedence for this operator.  Operator precedents run from 
   * 1 to 5, one being the highest, 5 being the lowest
   *
   * @return the operator precedence
   */
    int getPrecedence() {
        return 2;
    }

    /**
   * If this formula was on an imported sheet, check that
   * cell references to another sheet are warned appropriately
   * Does nothing, as operators don't have cell references
   */
    void handleImportedCellReferences() {
    }
}
