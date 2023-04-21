package net.sourceforge.djindent.parser;

/**
 * This is used to represent an operator from the file that was just parsed.  It goes as far as
 * to break the operators down into different types, these are
 *  NUMERIC - %  - =  + ++ -- *= -= += /=  %= 
 *  BITWISE -  ^= | & ! ~ ~= &= |= ^=
 *  CONDITIONAL -  && ||  ^ ==
 */
public abstract class Operator implements Symbol {

    public static final int NUMERIC = 50;

    public static final int EQUALS = 51;

    public static final int CONDITIONAL = 52;

    protected int m_symbolI = OPERATOR;

    protected int m_operatorTypeI;

    protected String m_textString;

    private String m_typeString = "operator";

    /**
     * This is the constructor for operator, all that it does is to store the text of the operator
     * and the type of the operator
     * @param a_textString This is the text of the string
     * @param a_operatorTypeI This is the type of the operator
     */
    public Operator(String a_textString, int a_operatorTypeI) {
        m_textString = a_textString;
        m_operatorTypeI = a_operatorTypeI;
    }

    /**
     * This returns the type of operator that this contains
     */
    public int getOperatorType() {
        return m_operatorTypeI;
    }

    /**
     * This method is used to return the basic type of this symbol( STRING, OPERATOR, PUNCTUATION, WHITESPACE )
     * @return int This is the symbol type, and will refer to one of the constants that are specified in this class
     */
    public int getSymbolType() {
        return m_symbolI;
    }

    /**
     * This method is used to return the actual text of this Symbol
     * @return String This is the actual text of this symbol
     */
    public String getText() {
        return m_textString;
    }

    /**
     * This is used to set the text of this symbol
     * @param a_textString This is the new text string
     */
    public void setText(String a_textString) {
        m_textString = a_textString;
    }
}
