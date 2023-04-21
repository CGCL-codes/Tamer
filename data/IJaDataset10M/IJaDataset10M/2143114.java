package sisc.interpreter;

/**
 * Defines a visitor interface for performing Java -> Scheme calls 
 * in an 'atomic' fashion, where details of acquiring and releasing
 * the Interpreter context are done behind the scenes. 
 * 
 * @see Context
 */
public interface SchemeCaller {

    /**
     * The execute callback function is called by Context 
     * with a fresh Interpreter context which is valid only 
     * during the call to execute.
     * 
     * @param r An Interpreter context
     * @return Any arbitrary return value, which will
     *  be returned out of the call to Context.execute()
     * @see sisc.interpreter.Context#execute(AppContext, SchemeCaller)
     */
    public Object execute(Interpreter r) throws SchemeException;
}
