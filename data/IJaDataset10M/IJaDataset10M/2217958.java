package tm.javaLang.analysis;

import java.util.Hashtable;
import tm.clc.analysis.LFConst;
import tm.clc.analysis.LFlags;

/**
* This interface represents the Java LFlags.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*/
public interface Java_LFlags {

    /** just loads <code>String</code> representations of some flags
     * for messages and debugging
     */
    public static FlagStringLoader fls = new FlagStringLoader();

    /** flag indicating nothing set/specified */
    public static final int EMPTY = LFlags.EMPTY;

    public static final LFConst EMPTY_LF = LFlags.EMPTY_LF;

    /** language entities flag range (0xFFF)*/
    public static final int LELEMENT = LFlags.LELEMENT;

    /** lookup context / directive flag range */
    public static final int LCONTEXT = LFlags.LCONTEXT;

    public static final int LDEFAULT = LFlags.LDEFAULT;

    /** flag indicating a local variable/ parameter (0x1) */
    public static final int LOCAL_VARIABLE = LFlags.VARIABLE;

    public static final LFConst LOCAL_VARIABLE_LF = new LFConst(LOCAL_VARIABLE);

    /** flag indicating a function (0x6)*/
    public static final int METHOD = 0x2;

    public static final LFConst METHOD_LF = new LFConst(METHOD);

    public static final int CONSTRUCTOR = 0x4;

    public static final LFConst CONSTRUCTOR_LF = new LFConst(CONSTRUCTOR);

    public static final int FUNCTION = METHOD + CONSTRUCTOR;

    public static final LFConst FUNCTION_LF = new LFConst(FUNCTION);

    /** flag indicating class members */
    public static final int FIELD_VARIABLE = 0x8;

    public static final LFConst FIELD_VARIABLE_LF = new LFConst(FIELD_VARIABLE);

    /** composite flag for variables */
    public static final int VARIABLE = LOCAL_VARIABLE + FIELD_VARIABLE;

    public static final LFConst VARIABLE_LF = new LFConst(VARIABLE);

    /** flag indicating an import statement */
    public static final int IMPORT = 0x10;

    public static final LFConst IMPORT_LF = new LFConst(IMPORT);

    /** flag indicating a class (0x20) */
    public static final int CLASS = LFlags.CLASS;

    public static final LFConst CLASS_LF = new LFConst(CLASS);

    /** flag indicating a Interface */
    public static final int INTERFACE = 0x40;

    public static final LFConst INTERFACE_LF = new LFConst(INTERFACE);

    /** language element flag indicating a type */
    public static final int TYPE = CLASS + INTERFACE;

    public static final LFConst TYPE_LF = new LFConst(TYPE);

    /** flag indicating a package */
    public static final int PACKAGE = 0x80;

    public static final LFConst PACKAGE_LF = new LFConst(PACKAGE);

    /** flag indicating a compilation unit */
    public static final int COMPILATION_UNIT = 0x100;

    public static final LFConst COMPILATION_UNIT_LF = new LFConst(COMPILATION_UNIT);

    /** flag indicating Expression Name */
    public static final int EXPRESSION = VARIABLE;

    public static final LFConst EXPRESSION_LF = new LFConst(EXPRESSION);

    /** flag indicating Ambiguous Name */
    public static final int AMBIGUOUS = 0x400;

    public static final LFConst AMBIGUOUS_LF = new LFConst(AMBIGUOUS);

    /** flag indicating PackageOrType Name */
    public static final int PACKAGEORTYPE = 0x800;

    public static final LFConst PACKAGEORTYPE_LF = new LFConst(PACKAGEORTYPE);

    /** flags indicating access restrictions. */
    public static final int PRIVATE_ACCESS = 0x1000;

    public static final LFConst PRIVATE_ACCESS_LF = new LFConst(PRIVATE_ACCESS);

    public static final int PACKAGE_ACCESS = 0x2000;

    public static final LFConst PACKAGE_ACCESS_LF = new LFConst(PACKAGE_ACCESS);

    public static final int PROTECTED_ACCESS = 0x4000;

    public static final LFConst PROTECTED_ACCESS_LF = new LFConst(PROTECTED_ACCESS);

    public static final int PUBLIC_ACCESS = 0x8000;

    public static final LFConst PUBLIC_ACCESS_LF = new LFConst(PUBLIC_ACCESS);

    public static final int PUBLIC_LOOKUP = PUBLIC_ACCESS;

    public static final LFConst PUBLIC_LOOKUP_LF = new LFConst(PUBLIC_LOOKUP);

    public static final int PROTECTED_LOOKUP = PUBLIC_LOOKUP + PROTECTED_ACCESS;

    public static final LFConst PROTECTED_LOOKUP_LF = new LFConst(PROTECTED_LOOKUP);

    public static final int PACKAGE_LOOKUP = PROTECTED_LOOKUP + PACKAGE_ACCESS;

    public static final LFConst PACKAGE_LOOKUP_LF = new LFConst(PACKAGE_LOOKUP);

    public static final int PRIVATE_LOOKUP = PACKAGE_LOOKUP + PRIVATE_ACCESS;

    public static final LFConst PRIVATE_LOOKUP_LF = new LFConst(PRIVATE_LOOKUP);

    public static final int STATIC = 0x10000;

    public static final LFConst STATIC_LF = new LFConst(STATIC);

    public static final int NOT_STATIC = 0x20000;

    public static final LFConst NOT_STATIC_LF = new LFConst(NOT_STATIC);

    /** just loads string representations into
     * <code>LFlags.flagStrings</code> table
     */
    public static class FlagStringLoader {

        static {
            Hashtable fs = LFlags.flagStrings;
            fs.put(new Integer(LOCAL_VARIABLE), "local variable");
            fs.put(new Integer(FIELD_VARIABLE), "field variable");
            fs.put(new Integer(VARIABLE), "variable");
            fs.put(new Integer(CLASS), "class");
            fs.put(new Integer(INTERFACE), "interface");
            fs.put(new Integer(TYPE), "type");
            fs.put(new Integer(PACKAGE), "package");
            fs.put(new Integer(IMPORT), "import");
            fs.put(new Integer(METHOD), "method");
            fs.put(new Integer(CONSTRUCTOR), "constructor");
            fs.put(new Integer(FUNCTION), "function");
            fs.put(new Integer(COMPILATION_UNIT), "compilation unit");
            fs.put(new Integer(AMBIGUOUS), "ambiguous name");
            fs.put(new Integer(PACKAGEORTYPE), "package or type name");
            fs.put(new Integer(PRIVATE_ACCESS), "private");
            fs.put(new Integer(PROTECTED_ACCESS), "protected");
            fs.put(new Integer(PACKAGE_ACCESS), "package");
        }
    }
}
