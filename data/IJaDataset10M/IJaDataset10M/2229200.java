package mx4j.adaptor.interceptor;

/**
 * The interceptor interface for JMX adaptors. <p>
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 7 $
 */
public interface Interceptor extends Invocable {

    /**
	 * Returns the type of this interceptor
	 */
    public String getType();

    /**
	 * Sets the next interceptor in the chain
	 */
    public void setNext(Interceptor interceptor);
}
