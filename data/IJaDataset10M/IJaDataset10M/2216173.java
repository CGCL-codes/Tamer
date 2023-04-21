package interpreter;

public abstract class Expression {

    public static final int NONE = -1;

    public static final int BOOL = 0;

    public static final int INT = 1;

    public static final int STR = 2;

    public static final int ADD = 0;

    public static final int SUB = 1;

    public static final int MUL = 2;

    public static final int DIV = 3;

    public static final int REM = 4;

    public static final int AND = 5;

    public static final int OR = 6;

    public static final int NOT = 7;

    public static final int EQ = 8;

    public static final int NE = 9;

    public static final int LT = 10;

    public static final int GT = 11;

    public static final int LE = 12;

    public static final int GE = 13;

    public abstract int getType();

    public abstract Object eval();

    public boolean evalBool() {
        return ((Boolean) eval()).booleanValue();
    }

    public int evalInt() {
        return ((Integer) eval()).intValue();
    }

    public String evalString() {
        return (String) eval();
    }
}
