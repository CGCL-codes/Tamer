package de.ios.framework.gui;

import java.awt.event.*;
import de.ios.framework.basic.*;

/**
 * Input-Field for Text, supported Objects: String.
 * Implementation of several Listener-Interfaces by the Basic-Class only for internal use!
 * For further description
 * @see IoSTextField
 * @version $Id: StringField.java,v 1.1.1.1 2004/03/24 23:00:48 nanneb Exp $
 */
public class StringField extends IoSTextField {

    /** Input-Field-Size in Columns. */
    public static final int DEFAULT_LENGTH = 25;

    /** Limited Character-Set of this Field (no Limitations). */
    public static final String STRING_CHARSET = null;

    protected boolean doTrim = false;

    protected boolean emptyIsNull = true;

    /** If true, convert input characters to upper case; if false, convert input characters to lower case; if null, do not convert input characters. */
    protected Boolean convCase = null;

    /** If true, use the standard uppercase method, else use the local simpleUpperCase (problems with '�') */
    protected boolean useStandardUpperCase = true;

    /**
   * Default constructor
   */
    public StringField() {
        this(DEFAULT_LENGTH);
    }

    /**
   * Constructor
   * @param cols the column number 
   */
    public StringField(int cols) {
        super(cols, STRING_CHARSET);
    }

    /**
   * Constructor defining the auto-formating and illegal-value-focus-keeping.
   */
    public StringField(boolean _autoFormat, boolean _keepFocus) {
        this(DEFAULT_LENGTH, _autoFormat, _keepFocus);
    }

    /**
   * Constructor defining the auto-formating and illegal-value-focus-keeping.
   */
    public StringField(int cols, boolean _autoFormat, boolean _keepFocus) {
        super(cols, STRING_CHARSET, _autoFormat, _keepFocus);
    }

    /**
   * Set the text of the StringField
   * @param value the text
   */
    public void setValue(String value) {
        if (value == null) setText(null);
        if (doTrim) value = value.trim();
        if (convCase != null) value = (convCase.booleanValue() ? ((useStandardUpperCase) ? value.toUpperCase() : simpleUpperCase(value)) : value.toLowerCase());
        setText(value);
    }

    /**
   * Get the text of the StringField
   * @return the text
   */
    public String getValue() {
        String s = getFormatedText();
        if (s != null) {
            if (emptyIsNull) if (s.length() == 0) return null;
        } else if (!emptyIsNull) s = "";
        return s;
    }

    /**
   * Format the Value (automatically called on lostFocus, manually called on getValue/Date()).
   * @return false, if formating fails due to an illegal Value
   * (results in keeping the Focus, if requested by setKeepFocusOnIllegalValue).
   */
    public boolean formatValue() {
        String s = getNullText();
        if (s != null) {
            if (doTrim) s = s.trim();
            if (convCase != null) s = (convCase.booleanValue() ? ((useStandardUpperCase) ? s.toUpperCase() : simpleUpperCase(s)) : s.toLowerCase());
        }
        setValue(s);
        return true;
    }

    /**
   * Method for simple uppercase operation.
   * Workaround for problems with "�" -> "SS"-Convertion in String.toUpperCase().
   */
    private static final String simpleUpperCase(String val) {
        if (val == null) return "";
        int n = val.length();
        StringBuffer sb = new StringBuffer(n);
        for (int i = 0; i < n; i++) sb.append(Character.toUpperCase(val.charAt(i)));
        return sb.toString();
    }

    /**
   * Get the text of the StringField
   * @return the text
   */
    public String getEncryptedValue() {
        return StringTool.encrypt(getValue());
    }

    /**
   * Define the Upper/Lower-Case-Conversion.
   * @param u If true, convert input characters to upper case; if false, convert input characters to lower case; if null, do not convert input characters.
   * @param su If true, use the standard uppercase method, else use the local simpleUpperCase
   */
    public StringField setConversion(Boolean u, boolean su) {
        convCase = u;
        useStandardUpperCase = su;
        return this;
    }

    /**
   * Enable case conversion.
   * @param b if true, convert input characters to upper case; if false, convert input characters to lower case; if null, do not convert input characters.
   */
    public void setConvertCase(Boolean b) {
        convCase = b;
    }

    /**
   * Specify case conversion mode.
   * @param b if true, use the standard uppercase method, else use the local simpleUpperCase
   */
    public void setConvertCaseMode(boolean b) {
        useStandardUpperCase = b;
    }

    /**
   * Define whether strings should be trimmed.
   * @param t if true, strings will be trimmed
   */
    public StringField trimString(boolean t) {
        doTrim = t;
        return this;
    }

    /**
   * Define whether empty strings should be returned as NULL value.
   * @param en if true, empty strings are returned as NULL.
   */
    public StringField returnEmptyAsNull(boolean en) {
        emptyIsNull = en;
        return this;
    }
}
