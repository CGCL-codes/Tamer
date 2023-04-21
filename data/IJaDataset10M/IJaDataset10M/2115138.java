package variables;

/**
 *
 * @author ebengtsonw
 */
public class IntVariable extends NumericVariable {

    /** Creates a new instance of IntVariable */
    public IntVariable(Integer number, int type) throws VariableTypeException {
        super(number, type);
        if (type != JPOX_DM.DISCRETE) {
            throw new VariableTypeException();
        }
    }

    public void nullValue() {
        this.value = new Integer(0);
    }

    public void add(Number n) {
        Integer v = (Integer) this.value;
        this.value = new Integer(v.intValue() + n.intValue());
    }

    public void substract(Number n) {
        Integer v = (Integer) this.value;
        this.value = new Integer(v.intValue() - n.intValue());
    }

    public void multiply(Number n) {
        Integer v = (Integer) this.value;
        this.value = new Integer(v.intValue() * n.intValue());
    }

    public void divide(Number n) {
        Integer v = (Integer) this.value;
        this.value = new Integer(v.intValue() / n.intValue());
    }
}
