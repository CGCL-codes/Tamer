package jaxlib.spi;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: AbstractServiceProvider.java 2708 2009-02-11 13:42:00Z joerg_wassmer $
 */
public abstract class AbstractServiceProvider<S> extends Object {

    protected AbstractServiceProvider() {
        super();
    }

    protected S init(final ServiceProviderType<S, ?> providerType) {
        return (S) this;
    }

    @Override
    public boolean equals(final Object b) {
        return (b == this) || ((b != null) && (getClass() == b.getClass()));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
