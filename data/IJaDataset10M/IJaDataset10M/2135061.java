package Euclid;

public abstract class VarType {

    public int type;

    public static final int TYPE_INT = 0;

    public static final int TYPE_FLOAT = 1;

    public String formula;

    public abstract VarType mult(VarType o);

    public abstract VarType add(VarType o);

    public abstract VarType subtract(VarType o);

    public abstract VarType div(VarType o);

    public abstract VarType mod(VarType o);

    public abstract VarType pow(VarType o);

    public abstract VarType negate();

    public abstract VarType not();

    public abstract String closeFormula();
}
