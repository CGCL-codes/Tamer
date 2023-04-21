package mx4j.tools.adaptor.interceptor;

/**
 * Abstraction for object that may be invoked.
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1633 $
 */
public interface Invocable {

    /**
	 * Invocation method
	 */
    public InvocationResult invoke(Invocation invocation) throws Exception;
}
