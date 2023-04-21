package jscript.engine;

public final class JavaVariable {

    private String name;

    private Class type;

    private boolean isFinal;

    public JavaVariable(Variable var) {
        name = var.getName();
        type = var.getType();
        isFinal = var.getIsFinal();
    }

    public void setValue(Object value, Class fromType, ParsingContext ctx) {
        ctx.setVariableValue(name, value, fromType);
    }

    public Object getValue(ParsingContext ctx) {
        return ctx.getVariableValue(name);
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public String toString() {
        return name + ": " + type.getName();
    }
}
