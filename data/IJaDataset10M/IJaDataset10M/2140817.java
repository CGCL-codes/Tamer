package gx.calc;

public abstract class CalculatorKeyDescriptor {

    public String displayValue;

    public String descriptionValue;

    public char keyBinding;

    public boolean enabled;

    public enum TYPE {

        EXPRESSION, INTERFACE
    }

    ;

    protected TYPE type;

    public abstract String toString();

    public abstract Object[] toArray();

    protected CalculatorKeyDescriptor(TYPE type) {
        this.type = type;
    }
}
