package ca.ucalgary.cpsc.ebe.fitClipse.FixtureGenerater;

public class JavaSourceUtil {

    public static final String TYPE_BOOLEAN = "boolean";

    public static final String TYPE_INT = "int";

    public static final String TYPE_CHAR = "char";

    public static final String TYPE_BYTE = "byte";

    public static final String TYPE_FLOAT = "float";

    public static final String TYPE_LONG = "long";

    public static final String TYPE_DOUBLE = "double";

    public static final String TYPE_STRING = "String";

    public static final String TYPE_VOID = "void";

    public JavaClass makeBasicClass(String name) {
        return new JavaClass(name, "public");
    }
}
