package name.thivent.sandbox.samples.services.hello;

import javax.ejb.Stateless;

/**
 * {@inheritDoc}
 * <p>
 * Optionally add here javadoc additional to the one inherited from the parent javadoc.
 * </p>
 * 
 * @see name.thivent.sandbox.samples.services.hello.Echo
 * @version $Id: $
 * @since 1.0
 */
@Stateless
public class EchoBean implements Echo {

    /**
     * The class to which the job is actually delegated.
     */
    private final EchoPojo pojo = new EchoPojoImpl();

    /**
     * {@inheritDoc}
     * 
     * @see name.thivent.sandbox.samples.services.hello.Echo#echo(java.lang.String)
     */
    public final String echo(final String in) {
        return pojo.echo(in);
    }
}
