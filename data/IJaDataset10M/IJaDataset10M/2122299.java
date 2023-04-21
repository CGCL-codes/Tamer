package net.sourceforge.djindent.parser;

import java.util.Map;
import java.util.HashMap;

/**
 * This is the class that is used to represent whitespace in the parser.  Whitepspace is either
 * a carriage return, a space, or a tab.
 */
public class Whitespace implements Symbol {

    public static final int TAB = 50;

    public static final int SPACE = 51;

    public static final int CARRIAGE_RETURN = 52;

    private static final String TAB_STRING = "TAB";

    private static final String SPACE_STRING = "SPACE";

    private static final String CARRIAGE_RETURN_STRING = "CARRIAGE_RETURN";

    private static Map m_typeMappings;

    private static Object lock = new Object();

    private String m_textString;

    private int m_whitespaceTypeI;

    private int m_numberOfI = 0;

    private String m_typeString = "whitespace";

    /**
     * This is the whitespace constructor, what it does is to just save the actual text for 
     * the whitespace and the type of whitespace that this
     * @param a_textString This is the text that represents the whitespace
     * @param a_whitespaceTypeI This is the type of whitespace that this represetns
     */
    public Whitespace(String a_textString, int a_whitespaceTypeI) {
        m_textString = a_textString;
        m_whitespaceTypeI = a_whitespaceTypeI;
        m_numberOfI = m_textString.length();
    }

    /**
     * This method is used to cone this object with the new text string, and make it of the 
     * new description type.
     * @param a_textString This is the text for the new symbol
     * @param a_descriptionString This is the description of the new string
     * @return Symbol This is a clone of this symbol
     * @throws UnknownSymbolException This is the exception that is thrown if there is a problem during the clone
     */
    public Symbol clone(String a_textString, String a_descriptionString) throws UnknownSymbolException {
        Symbol toReturn = null;
        if ((a_textString != null) && (a_descriptionString != null)) {
            toReturn = new Whitespace(a_textString, resolveSymbolType(a_descriptionString));
        }
        if (toReturn == null) {
            throw new UnknownSymbolException("Unable to create symbol with text " + a_textString + " and description " + a_descriptionString);
        }
        return toReturn;
    }

    /**
     * This is the number of this type of whitespace that this represents
     */
    public int getNumberOf() {
        return m_numberOfI;
    }

    /**
     * This method is used to return the basic type of this symbol( STRING, OPERATOR, PUNCTUATION, WHITESPACE )
     * @return int This is the symbol type, and will refer to one of the constants that are specified in this class
     */
    public int getSymbolType() {
        return WHITESPACE;
    }

    public String getText() {
        return m_textString;
    }

    /**
     * This returns the type of whitespace that this represents
     * @return int This is the type of whitespace that this is supposed to represent, it refers to one of the constants declared in this class
     */
    public int getWhitespaceType() {
        return m_whitespaceTypeI;
    }

    /**
     * This method is used to retrieve the map that contain all of the type mappings.  
     * @return Map This is the type mapping map
     */
    private static Map getTypeMappings() {
        if (m_typeMappings == null) {
            synchronized (lock) {
                if (m_typeMappings == null) {
                    m_typeMappings = new HashMap();
                    m_typeMappings.put(TAB_STRING, new Integer(TAB));
                    m_typeMappings.put(SPACE_STRING, new Integer(SPACE));
                    m_typeMappings.put(CARRIAGE_RETURN_STRING, new Integer(CARRIAGE_RETURN));
                }
            }
        }
        return m_typeMappings;
    }

    /**
     * This method is used to determine if a type string is descriping this symbol type
     * @param a_typeSting This is the type that is being tested to see if it describes this Symbol
     * @return boolean Whether or not the type string describes this symbol
     */
    public boolean myType(String a_typeString) {
        boolean toReturn = false;
        if ((a_typeString != null) && (a_typeString.toLowerCase().equals(m_typeString))) {
            toReturn = true;
        }
        return toReturn;
    }

    /**
     * This method is used to resolve the description of a type into the actual type integer constant that represents that type
     * @param a_symbolTypeString This is the type of the symbol, in string format
     * @return int This is the type of the symbol, in int format
     * @throws UnknownSymbolException This is the exception that is thrown if the symbol type that was passed in is unknown
     */
    public static int resolveSymbolType(String a_symbolTypeString) throws UnknownSymbolException {
        Integer toReturn = (Integer) getTypeMappings().get(a_symbolTypeString);
        if (toReturn != null) {
            return toReturn.intValue();
        } else {
            throw new UnknownSymbolException("Unknown symbol " + a_symbolTypeString);
        }
    }

    /**
     * This is used to increment the number of pieces of whitespace that this represents 
     */
    public void increment() {
        m_numberOfI++;
    }

    /**
     * This is used to set the text of this symbol
     * @param a_textString This is the new text string
     */
    public void setText(String a_textString) {
        m_textString = a_textString;
    }
}
