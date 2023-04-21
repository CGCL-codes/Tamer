package net.sourceforge.djindent.parser;

/**
 * This method is used to represent a symbol that was parsed out by the parser, a symbol being a String
 * an operator, punctuation or whitespace.  Where each is defined as follows:
 *  STRING - This is a free foarmed piece of text that was discovered in the parsed file
 *  OPERATOR - ~ ! % & * - = +  <  > / |
 *  PUNCTUATION - ( ) ; , [ ] { } : . ? " '
 *  WHITESPACE space, tab, carriage return
 */
public interface Symbol {

    public final int STRING = 1;

    public final int OPERATOR = 2;

    public final int PUNCTUATION = 3;

    public final int WHITESPACE = 4;

    /**
     * This method is used to cone this object with the new text string, and make it of the 
     * new description type.
     * @param a_textString This is the text for the new symbol
     * @param a_descriptionString This is the description of the new string
     * @return Symbol This is a clone of this symbol
     * @throws UnknownSymbolException This is the exception that is thrown if there is a problem during the clone
     */
    public Symbol clone(String a_textString, String a_descriptionString) throws UnknownSymbolException;

    /**
     * This method is used to return the basic type of this symbol( STRING, OPERATOR, PUNCTUATION, WHITESPACE )
     * @return int This is the symbol type, and will refer to one of the constants that are specified in this class
     */
    public int getSymbolType();

    /**
     * This method is used to return the actual text of this Symbol
     * @return String This is the actual text of this symbol
     */
    public String getText();

    /**
     * This method is used to determine if a type string is descriping this symbol type
     * @param a_typeSting This is the type that is being tested to see if it describes this Symbol
     * @return boolean Whether or not the type string describes this symbol
     */
    public boolean myType(String a_typeString);

    /**
     * This is used to set the text of this symbol
     * @param a_textString This is the new text string
     */
    public void setText(String a_textString);
}
