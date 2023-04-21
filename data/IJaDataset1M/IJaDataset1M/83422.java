package javax.imageio.spi;

/**
 * An interface which service providers may optionally implement in
 * order to get notified when they are added or removed from a {@link
 * ServiceRegistry}.
 *
 * @since 1.4
 *
 * @author Sascha Brawer (brawer@dandelis.ch)
 */
public interface RegisterableService {

    /**
   * Informs this service provider that it has been registered in a
   * {@link ServiceRegistry}. If this provider gets registered as an
   * implementor for several service categories, its
   * <code>onRegistration</code> method will be called multiple times.
   *
   * @param registry the registry to which this service provider has
   * been added.
   *
   * @param category the service category for which this provider has
   * been registered as an implementor.
   */
    void onRegistration(ServiceRegistry registry, Class category);

    /**
   * Informs this service provider that it has been de-registered from
   * a {@link ServiceRegistry}. If this provider had been registered
   * as an implementor for several service categories, its
   * <code>onDeregistration</code> method will be called multiple
   * times.
   *
   * @param registry the registry from which this service provider has
   * been removed.
   *
   * @param category the service category for which this provider has
   * been registered as an implementor.
   */
    void onDeregistration(ServiceRegistry registry, Class category);
}
