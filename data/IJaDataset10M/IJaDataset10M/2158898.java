package padmig.lib;

/**
 * Empty stack frame.
 */
public class EmptyStackFrame extends StackFrame {

    public static final long serialVersionUID = 1L;

    /**
	 * Does nothing and allways returns null.
	 * 
	 * @see padmig.lib.StackFrame#continueExecution()
	 */
    @Override
    public Object continueExecution() throws Exception {
        return null;
    }
}
