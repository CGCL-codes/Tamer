package fluid.java.operator;

import fluid.java.JavaOperator;
import fluid.tree.Operator;

public class Type extends JavaOperator implements TypeInterface {

    protected Type() {
    }

    public static final Type prototype = new Type();

    /** Use the interface to determine what operators are legal:
   */
    public boolean includes(Operator other) {
        return (other instanceof TypeInterface);
    }
}
