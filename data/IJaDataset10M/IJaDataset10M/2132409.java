package net.sf.jagg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class knows how to parse a property specification:
 * <code>property[([param[, param]*])]</code>.
 * 
 * @author Randy Gettman
 * @since 0.1.0
 */
public class PropertyParser {

    private static final boolean DEBUG = false;

    private String myPropertyText;

    private String myPropertyName;

    private boolean amIMethod;

    private List<Object> myParameters = new ArrayList<Object>();

    /**
    * Create a <code>PropertyParser</code>.
    */
    public PropertyParser() {
        setPropertyText("");
    }

    /**
    * Create a <code>PropertyParser</code> object that will parse the given property text.
    * @param propertyText The text of the property.
    */
    public PropertyParser(String propertyText) {
        setPropertyText(propertyText);
    }

    /**
    * Sets the property to the given property text and resets the parser.
    * @param propertyText The new property text.
    */
    public void setPropertyText(String propertyText) {
        myPropertyText = propertyText;
        reset();
    }

    /**
    * Resets this <code>PropertyParser</code>, usually at creation time and when new
    * input arrives.
    */
    private void reset() {
        myPropertyName = null;
        amIMethod = false;
        myParameters.clear();
    }

    /**
    * Parses the property text.
    */
    public void parse() {
        PropertyScanner scanner = new PropertyScanner(myPropertyText);
        String lexeme = "";
        boolean wasQuoted = false;
        PropertyScanner.Token token = scanner.getNextToken();
        if (token == PropertyScanner.Token.TOKEN_WHITESPACE) token = scanner.getNextToken();
        if (token == PropertyScanner.Token.TOKEN_STRING) {
            myPropertyName = scanner.getCurrLexeme();
        } else {
            throw new IllegalArgumentException("Illegal start to property or method name: \"" + myPropertyText + "\".");
        }
        token = scanner.getNextToken();
        if (token == PropertyScanner.Token.TOKEN_WHITESPACE) token = scanner.getNextToken();
        if (token == PropertyScanner.Token.TOKEN_EOI) {
            amIMethod = false;
            return;
        }
        if (token != PropertyScanner.Token.TOKEN_LEFT_PAREN) {
            throw new IllegalArgumentException("Property or method name must start with '(': \"" + myPropertyText + "\".");
        }
        amIMethod = true;
        token = scanner.getNextToken();
        while (token.getCode() >= 0 && token != PropertyScanner.Token.TOKEN_RIGHT_PAREN) {
            switch(token) {
                case TOKEN_WHITESPACE:
                    break;
                case TOKEN_STRING:
                    lexeme = scanner.getCurrLexeme();
                    break;
                case TOKEN_COMMA:
                    if (wasQuoted || lexeme.length() > 0) myParameters.add(getTypedParameter(lexeme, wasQuoted));
                    wasQuoted = false;
                    break;
                case TOKEN_DOUBLE_QUOTE:
                    wasQuoted = true;
                    break;
                case TOKEN_SINGLE_QUOTE:
                    wasQuoted = true;
                    break;
                case TOKEN_EOI:
                    throw new IllegalArgumentException("'(' found without ')': \"" + myPropertyText + "\".");
                default:
                    throw new IllegalArgumentException("Parse error occurred: \"" + myPropertyText + "\".");
            }
            token = scanner.getNextToken();
        }
        if (token.getCode() < 0) throw new IllegalArgumentException("A parse error occured: \"" + myPropertyText + "\".");
        if (wasQuoted || lexeme.length() > 0) {
            Object value = getTypedParameter(lexeme, wasQuoted);
            if (DEBUG) System.out.println("  value's class is \"" + value.getClass().getName() + "\".");
            myParameters.add(value);
        }
        token = scanner.getNextToken();
        if (token == PropertyScanner.Token.TOKEN_WHITESPACE) token = scanner.getNextToken();
        if (token != PropertyScanner.Token.TOKEN_EOI) throw new IllegalArgumentException("Extra input found after ')': \"" + myPropertyText + "\".");
    }

    /**
    * Attempts to determine the intended type of the input, and return it as an
    * instance of that type.  Enumerations are supported with the format
    * <code>class:name</code>.
    * @param parameter The parameter's string value.
    * @param wasQuoted Whether the string value was quoted.
    * @return An object of that type.
    */
    @SuppressWarnings("unchecked")
    private Object getTypedParameter(String parameter, boolean wasQuoted) {
        if (DEBUG) System.out.println("  fTP: \"" + parameter + "\", quoted: " + wasQuoted);
        Object result;
        if (wasQuoted) {
            if (DEBUG) System.out.println("  Quoted: String");
            if (parameter == null || parameter.length() == 0) return ""; else return parameter;
        }
        if (parameter.matches(".*[A-Za-z]+.*")) {
            if (DEBUG) System.out.println("  Letters found => String");
            if ("true".equalsIgnoreCase(parameter)) return true; else if ("false".equalsIgnoreCase(parameter)) return false; else if ("null".equalsIgnoreCase(parameter)) return null; else if (parameter.contains(":")) {
                int index = parameter.indexOf(":");
                String className = parameter.substring(0, index);
                String enumName = parameter.substring(index + 1);
                try {
                    if (DEBUG) System.out.println("    Enum parsing: class: \"" + className + "\", name: \"" + enumName + "\".");
                    Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(className);
                    return Enum.valueOf(enumClass, enumName);
                } catch (ClassNotFoundException e) {
                    throw new UnsupportedOperationException("Unrecognized Enum: \"" + className + "\"", e);
                } catch (ClassCastException e) {
                    throw new UnsupportedOperationException("Not an Enum: \"" + className + "\"", e);
                }
            }
            return parameter;
        } else if (parameter.matches("[-]?[0-9]+\\.[0-9]*")) {
            if (DEBUG) System.out.println("  Numbers and decimal point => Float/Double/BigDecimal");
            try {
                result = Double.valueOf(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            try {
                result = new BigDecimal(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            return parameter;
        } else if (parameter.matches("[-]?[0-9]+")) {
            if (DEBUG) System.out.println("  Numbers => Byte/Short/Integer/Long/BigInteger");
            try {
                result = Byte.valueOf(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            try {
                result = Short.valueOf(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            try {
                result = Integer.valueOf(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            try {
                result = Long.valueOf(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            try {
                result = new BigInteger(parameter);
                return result;
            } catch (NumberFormatException ignored) {
            }
            return parameter;
        } else {
            if (DEBUG) System.out.println("  Failed other attempts, falling back on String");
            return parameter;
        }
    }

    /**
    * Returns the property or method name.
    * @return The property or method name.
    */
    public String getPropertyName() {
        return myPropertyName;
    }

    /**
    * Returns whether this property text represents a method (with parentheses)
    * or a simple property name (without parentheses).
    * @return <code>true</code> if it represents a method, <code>false</code>
    *    if it represents a simple property name.
    */
    public boolean isMethod() {
        return amIMethod;
    }

    /**
    * Returns the <code>List</code> of parameters (possibly empty), or
    * <code>null</code> if this is a simple property name.
    * @return A <code>List</code> of parameters (possibly empty), or
    *    <code>null</code> if this is a simple property name.
    */
    public List<Object> getParameters() {
        if (!amIMethod) return null;
        return myParameters;
    }
}
