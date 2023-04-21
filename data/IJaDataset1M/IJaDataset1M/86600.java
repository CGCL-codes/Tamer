package edu.vub.at.exceptions;

import edu.vub.at.objects.ATTypeTag;
import edu.vub.at.objects.coercion.NativeTypeTags;

/**
 * An XIllegalParameter exception is raised when the interpreter detects a faulty
 * formal parameter list of a function or method when it is invoked. The detection
 * of such faulty parameter lists is done when the function is defined since its
 * paramter list is then partially evaluated to produce a correct argument binding 
 * function.
 * <p>
 * Illegal parameter lists can be formed when the rest-parameter (@arg) is not the
 * last parameter or when optional parameters are followed by mandatory parameters.
 *
 * @see edu.vub.at.eval.PartialBinder#calculateResidual(String, ATTable)
 * 
 * @author tvcutsem
 */
public class XIllegalParameter extends InterpreterException {

    private static final long serialVersionUID = -8300108453776535995L;

    /**
	 * Constructor taking the name of the function whose parameter list is being partially 
	 * evaluated and the message detailing the precise error.
	 */
    public XIllegalParameter(String ofFun, String msg) {
        super("Illegal parameter list for " + ofFun + ": " + msg);
    }

    public ATTypeTag getType() {
        return NativeTypeTags._ILLPARAM_;
    }
}
