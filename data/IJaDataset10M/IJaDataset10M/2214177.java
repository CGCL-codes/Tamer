package PolynomialExpressionGenerator;

/**
 *
 * @author Nicholson.Bill
 */
public class Expression {

    private String numerator;

    private String denominator;

    /**
     * Constructor with no args. Numerator and denominator default to something
     */
    public Expression() {
        numerator = "numerator";
        denominator = "denominator";
    }

    /**
     * Constructor
     * @param numerator The numerator of the expression
     * @param denominator the denominator of the expression
     */
    public Expression(String numerator, String denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     *
     * @return the value of the numerator in the expression
     */
    public String getNumerator() {
        return numerator;
    }

    /**
     *
     * @return the value of the denominator in the expression
     */
    public String getDenominator() {
        return denominator;
    }

    /**
     *
     * @param numerator the numerator in the expression
     * @return the numerator in the expression
     */
    public String setNumerator(String numerator) {
        this.numerator = numerator;
        return numerator;
    }

    /**
     *
     * @param denominator the denominator in the expression
     * @return the denominator in the expression
     */
    public String setDenominator(String denominator) {
        this.denominator = denominator;
        return denominator;
    }
}
