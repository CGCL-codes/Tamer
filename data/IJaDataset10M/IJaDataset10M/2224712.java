package net.sourceforge.djindent.parser;

import net.sourceforge.djindent.parser.UnknownSymbolException;
import java.util.Map;
import java.util.HashMap;

public class Punctuation implements Symbol {

    public static final int LEFT = 50;

    public static final int RIGHT = 51;

    public static final int NONE = 52;

    public static final int BRACE = 100;

    public static final int BRACKET = 101;

    public static final int SEMICOLON = 102;

    public static final int PARENTHESIS = 103;

    public static final int COLON = 104;

    public static final int PERIOD = 105;

    public static final int DOUBLE_QUOTE = 106;

    public static final int SINGLE_QUOTE = 107;

    public static final int COMMA = 108;

    public static final int BLOCK_COMMENT = 109;

    public static final int LINE_COMMENT = 110;

    public static final int BACKSLASH = 111;

    public static final int AT = 112;

    public static final int QUESTION_MARK = 113;

    private static final String BRACE_STRING = "BRACE";

    private static final String BRACKET_STRING = "BRACKET";

    private static final String SEMICOLON_STRING = "SEMICOLON";

    private static final String PARENTHESIS_STRING = "PARENTHESIS";

    private static final String COLON_STRING = "COLON";

    private static final String PERIOD_STRING = "PERIOD";

    private static final String DOUBLE_QUOTE_STRING = "DOUBLE_QUOTE";

    private static final String SINGLE_QUOTE_STRING = "SINGLE_QUOTE";

    private static final String COMMA_STRING = "COMMA";

    private static final String BLOCK_COMMENT_STRING = "BLOCK_COMMENT";

    private static final String LINE_COMMENT_STRING = "LINE_COMMENT";

    private static final String BACKSLASH_STRING = "BACKSLASH";

    private static final String AT_STRING = "AT";

    private static final String QUESTION_MARK_STRING = "QUESTION_MARK";

    private static Map m_typeMappings;

    private static Map m_orientationMap;

    private static Object lock = new Object();

    private String m_textString;

    private int m_punctuationSymbolI;

    private int m_orientationI;

    private int m_symbolTypeI;

    private String m_typeString = "punctuation";

    /**
     * This is the simple constructor for punctuation, it just stores the text of the punctuation
     * and the symbol itself
     * @param a_textString This is the string that represents the punctuation
     * @param a_punctuationSymbolI This is the type of punctuation that this represents
     */
    public Punctuation(String a_textString, int a_punctuationSymbolI) {
        m_textString = a_textString;
        m_punctuationSymbolI = a_punctuationSymbolI;
        m_orientationI = findOrientation(a_textString);
        m_symbolTypeI = PUNCTUATION;
    }

    /**
     * This is a constructor for punctuation, it just stores the text of the punctuation
     * and the symbol itself as well as the orientation
     * @param a_textString This is the string that represents the punctuation
     * @param a_punctuationSymbolI This is the type of punctuation that this represents
     * @param a_orientationI This is orientation of this symbol( NONE, RIGHT, or LEFT
     */
    public Punctuation(String a_textString, int a_punctuationSymbolI, int a_orientationI) {
        this(a_textString, a_punctuationSymbolI);
        m_orientationI = a_orientationI;
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
            toReturn = new Punctuation(a_textString, resolveSymbolType(a_descriptionString));
        }
        if (toReturn == null) {
            throw new UnknownSymbolException("Unable to create symbol with text " + a_textString + " and description " + a_descriptionString);
        }
        return toReturn;
    }

    /**
     * This is how you can determine the orientation of a text symbol, whether it is left, right 
     * or none.
     * @param a_textString This is the text string that you are trying to find of
     * @return int This is the orientation
     */
    public static int findOrientation(String a_textString) {
        int toReturn = NONE;
        Integer orientation = (Integer) getOrientationMap().get(a_textString);
        if (orientation != null) {
            toReturn = orientation.intValue();
        }
        return toReturn;
    }

    /**
     * This is used to retrieve the map that describes the orientation of the symbols
     * @return Map This is the orientation map
     */
    public static Map getOrientationMap() {
        if (m_orientationMap == null) {
            synchronized (lock) {
                if (m_orientationMap == null) {
                    Integer left = new Integer(LEFT);
                    Integer right = new Integer(RIGHT);
                    m_orientationMap = new HashMap();
                    m_orientationMap.put("{", left);
                    m_orientationMap.put("}", right);
                    m_orientationMap.put("[", left);
                    m_orientationMap.put("]", right);
                    m_orientationMap.put("(", left);
                    m_orientationMap.put(")", right);
                    m_orientationMap.put("/*", left);
                    m_orientationMap.put("*/", right);
                }
            }
        }
        return m_orientationMap;
    }

    /**
     * This returns the puctuation orientation.  This is used for something like parentthese
     * where you can have a left or right parentheses, so the punctution type would be set
     * to parenthesis and the orientation would be set to left or right.  In the case of
     * something that has no orientation, this is set to NONE.
     * @return int This is the orientation type
     */
    public int getPunctuationOrientation() {
        return m_orientationI;
    }

    /**
     * This is the type of punctuation that this is.  It referes to one of the constants above
     * @return int This is the punctuation type
     */
    public int getPunctuationType() {
        return m_punctuationSymbolI;
    }

    /**
     * This method is used to return the basic type of this symbol( STRING, OPERATOR, PUNCTUATION, WHITESPACE )
     * @return int This is the symbol type, and will refer to one of the constants that are specified in this class
     */
    public int getSymbolType() {
        return m_symbolTypeI;
    }

    /**
     * This method is used to return the actual text of this Symbol
     * @return String This is the actual text of this symbol
     */
    public String getText() {
        return m_textString;
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
                    m_typeMappings.put(BRACE_STRING, new Integer(BRACE));
                    m_typeMappings.put(BRACKET_STRING, new Integer(BRACKET));
                    m_typeMappings.put(SEMICOLON_STRING, new Integer(SEMICOLON));
                    m_typeMappings.put(PARENTHESIS_STRING, new Integer(PARENTHESIS));
                    m_typeMappings.put(COLON_STRING, new Integer(COLON));
                    m_typeMappings.put(PERIOD_STRING, new Integer(PERIOD));
                    m_typeMappings.put(DOUBLE_QUOTE_STRING, new Integer(DOUBLE_QUOTE));
                    m_typeMappings.put(SINGLE_QUOTE_STRING, new Integer(SINGLE_QUOTE));
                    m_typeMappings.put(COMMA_STRING, new Integer(COMMA));
                    m_typeMappings.put(BLOCK_COMMENT_STRING, new Integer(BLOCK_COMMENT));
                    m_typeMappings.put(LINE_COMMENT_STRING, new Integer(LINE_COMMENT));
                    m_typeMappings.put(AT_STRING, new Integer(AT));
                    m_typeMappings.put(QUESTION_MARK_STRING, new Integer(QUESTION_MARK));
                    m_typeMappings.put(BACKSLASH_STRING, new Integer(BACKSLASH));
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
     * This is used to set the text of this symbol
     * @param a_textString This is the new text string
     */
    public void setText(String a_textString) {
        m_textString = a_textString;
    }
}
