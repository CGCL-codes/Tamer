package activejava.vm.step;

import activejava.vm.AJIllegalAccessException;
import activejava.vm.AJRuntime;
import activejava.vm.AJRuntimeException;
import activejava.vm.Atom;

/** 
 * AcitveJava
 *
 *
 * @author �����
 * @email 17bity@gmail.com
 * @since 2006-8-25
 * @version 0.1a
 */
public abstract class Operator implements Step {

    protected Atom left;

    protected Atom right;

    public abstract int run(AJRuntime runtime) throws AJRuntimeException;

    public abstract String toString();

    public Atom getLeft() {
        return left;
    }

    public void setLeft(Atom left) {
        this.left = left;
    }

    public Atom getRight() {
        return right;
    }

    public void setRight(Atom right) {
        this.right = right;
    }
}
