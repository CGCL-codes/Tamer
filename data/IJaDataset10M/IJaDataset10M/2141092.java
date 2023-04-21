package joj.core.reloading;

/**
 * <p>
 * A general purpose resource store, should implement the Map
 * interface.  Possible to use it to store incoming files under
 * a certain size.  It is horribly innefficient just to use to
 * reload classes.
 * </p>
 *
 * @author tcurdt
 */
public interface ResourceStore {

    byte[] read(final String pResourceName);

    boolean remove(final String pResourceName);

    void write(final String pResourceName, final byte[] pResourceData);
}
