package org.escapek.mofparser.exceptions;

/**
 * Exception thrown for every exception occuring during parse process. See exception message
 * and args for details about each instance occuring.
 * Note that MOFParser doesn't throw these exception by itself. Instead it calls its handler's error method 
 * and passes it the exception instance created. The handler implementation is responsible of managing
 * the exception and throw it if needed.
 * @author nico
 *
 */
public class MOFParserException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7860068645287249469L;

    /**
	 * The production type currently being parsed is unkown. See CIM specifications
	 * for a list of allowed production types. Currently, supported production types are:
	 * <ul>
	 * <li>compilerDirective</li>
	 * <li>classDeclaration</li>
	 * <li>qualifierDeclaration</li>
	 * <li>instanceDeclaration</li>
	 * </ul>
	 */
    public static String UNKNOWN_PRODUCTION_TYPE = "UNKNOWN_PRODUCTION_TYPE";

    /**
	 * A property declaration specifies an invalid name.
	 * <code>arg1</code> contains the invalid name, if any.
	 */
    public static String INVALID_PROPERTY_NAME = "INVALID_PROPERTY_NAME";

    /**
	 * Class declaration expect at least 1 argument: <code>className</code>. 
	 * This error indicates that the class being parsed doesn't provide this element.
	 * See <code>arg1</code> content to know how many arguments were found instead of 1.
	 */
    public static String INVALID_CLASS_DECL_ARGUMENT_COUNT = "INVALID_CLASS_DECL_ARGUMENT_COUNT";

    /**
	 * A class declaration specifies an invalid class name.
	 * <code>arg1</code> contains the invalid name, if any.
	 */
    public static String INVALID_CLASS_NAME = "INVALID_CLASS_NAME";

    /**
	 * Compiler directive production expect 2 arguments: <code>pragmaName</code> and 
	 * <code>pragmaParameter</code>. This error indicates that the compiler directive being
	 * parsed doesn't provide those both elements.
	 * See <code>arg1</code> content to know how many arguments were found instead of 2.
	 */
    public static String INVALID_COMPILER_DIRECTIVE_ARGUMENT_COUNT = "INVALID_COMPILER_DIRECTIVE_ARGUMENT_COUNT";

    /**
	 * Qualifer declaration expect at least 2 arguments: <code>qualifierName</code> and 
	 * <code>qualifierType</code>. This error indicates that the qualifier declaration being
	 * parsed doesn't provide at least those both elements.
	 * See <code>arg1</code> content to know how many arguments were found.
	 */
    public static String INVALID_QUALIFIER_DECL_ARGUMENT_COUNT = "INVALID_QUALIFIER_DECL_ARGUMENT_COUNT";

    /**
	 *  The compiler directive being parsed in invalid. Currently, CIM 2.3 spec. compiler directives
	 *  are supported:
	 *  <ul>
	 *  <li>include()</li>
	 *  <li>instancelocale()</li>
	 *  <li>locale()</li>
	 *  <li>namespace()</li>
	 *  </ul>
	 *  <code>arg1</code> contains the name of the unsupported compiler directives.
	 */
    public static String INVALID_COMPILER_DIRECTIVE = "INVALID_COMPILER_DIRECTIVE";

    /**
	 * A type declaration contains invalid informations and can't be parsed.
	 */
    public static String INVALID_TYPE_TREE = "INVALID_TYPE_TREE";

    /**
	 * A qualifier declaration specifies an invalid qualifier name.
	 * <code>arg1</code> contains the invalid name, if any.
	 */
    public static String INVALID_QUALIFIER_NAME = "INVALID_QUALIFIER_NAME";

    /**
	 * A reference declaration contains an invalid class name
	 * <code>arg1</code> contains the invalid class name, if any.
	 */
    public static String INVALID_CLASS_REF = "INVALID_CLASS_REF";

    /**
	 * A data type being parsed is of an invalid type. Currently, CIM 2.3 spec. data type are 
	 * supported:
	 * <ul>
	 * <li>UINT8</li>
	 * <li>SINT8</li>
	 * <li>UINT16</li>
	 * <li>SINT16</li>
	 * <li>UINT32</li>
	 * <li>SINT32</li>
	 * <li>UINT64</li>
	 * <li>SINT64</li>
	 * <li>STRING</li>
	 * <li>BOOLEAN</li>
	 * <li>REAL32</li>
	 * <li>REAL64</li>
	 * <li>DATETIME</li>
	 * <li>REF</li>
	 * <li>CHAR16</li>
	 * </ul> 
	 * <code>arg1</code> contains the name of the unsupported type.
	 */
    public static String INVALID_DATA_TYPE = "INVALID_DATA_TYPE";

    /**
	 * A class method declaration specifies an invalid name.
	 * <code>arg1</code> contains the invalid name, if any.
	 */
    public static String INVALID_METHOD_NAME = "INVALID_METHOD_NAME";

    /**
	 * A class method parameter declaration specifies an invalid name.
	 * <code>arg1</code> contains the invalid name, if any.
	 */
    public static String INVALID_PARAMETER_NAME = "INVALID_PARAMETER_NAME";

    private Object arg1;

    private Object arg2;

    public MOFParserException(String message) {
        super(message);
    }

    public MOFParserException(String message, Object arg1) {
        super(message);
        setArg1(arg1);
    }

    public MOFParserException(String message, Object arg1, Object arg2) {
        super(message);
        setArg1(arg1);
        setArg2(arg2);
    }

    public MOFParserException(Throwable cause) {
        super(cause);
    }

    /**
	 * Get exception first argument, if any.
	 * @return exception first argument
	 */
    public Object getArg1() {
        return arg1;
    }

    public void setArg1(Object arg1) {
        this.arg1 = arg1;
    }

    /**
	 * Get exception second argument, if any.
	 * @return exception second argument
	 */
    public Object getArg2() {
        return arg2;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }
}
