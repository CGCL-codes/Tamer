package fr.jade.fraclite.orb.registry;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.naming.Binder;
import org.objectweb.naming.Name;
import org.objectweb.naming.NamingException;
import fr.jade.fraclite.util.FracLite;

/**
 * Provides static methods to launch and get access to a Fractal registry.
 */
public class Registry {

    /**
   * The default port used to export the naming service interface.
   */
    public static final int DEFAULT_PORT = 1234;

    /**
   * Private constructor (uninstantiable class).
   */
    private Registry() {
    }

    /**
   * Launches a Fractal registry on the local host.
   *
   * @param args a single argument setting the port number where to pick the
   *      naming service interface.
   * @throws Exception if something goes wrong.
   */
    public static void main(final String[] args) throws Exception {
        int port = DEFAULT_PORT;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
            }
        }
        createRegistry(port);
    }

    /**
   * Creates {@link NamingService} component and exports its server interface.
   *
   * @param port the port number to be used to export the naming service
   *      interface.
   * @throws Exception if something goes wrong.
   */
    public static void createRegistry(final int port) throws Exception {
        Component boot = Fractal.getBootstrapComponent();
        TypeFactory tf = Fractal.getTypeFactory(boot);
        GenericFactory cf = Fractal.getGenericFactory(boot);
        ComponentType type = tf.createFcType(new InterfaceType[] { tf.createFcItfType("registry", "fr.jade.fraclite.orb.registry.NamingService", TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) });
        Component registry = cf.newFcInstance(type, "primitive", "fr.jade.fraclite.orb.registry.BasicNamingService");
        FracLite.getAttributeController(registry).setAttribute("port", String.valueOf(port));
        Fractal.getLifeCycleController(registry).startFc();
        System.out.println("Fractal registry is ready.");
    }

    /**
   * Returns a reference to the {@link NamingService} on the local host, for the
   * default {@link #DEFAULT_PORT port}.
   *
   * @return a reference to the {@link NamingService} on the local host, for the
   *      default {@link #DEFAULT_PORT port}.
   * @throws Exception if something goes wrong.
   */
    public static NamingService getRegistry() throws Exception {
        String host = InetAddress.getLocalHost().getHostName();
        return getRegistry(host);
    }

    /**
   * Returns a reference to the {@link NamingService} on the given host, for the
   * default {@link #DEFAULT_PORT port}.
   *
   * @param host the host where the naming service is located.
   * @return a reference to the {@link NamingService} on the given host, for the
   *      default {@link #DEFAULT_PORT port}.
   * @throws Exception if something goes wrong.
   */
    public static NamingService getRegistry(final String host) throws Exception {
        return getRegistry(host, DEFAULT_PORT);
    }

    /**
   * Returns a reference to the {@link NamingService} on the given host and
   * port.
   *
   * @param host the host where the naming service is located.
   * @param port the port that was used to export the naming service on the
   *      given host.
   * @return a reference to the {@link NamingService} on the given host and
   *      port.
   * @throws NamingException 
   * @throws Exception if something goes wrong.
   */
    public static NamingService getRegistry(final String host, final int port) throws NamingException {
        Binder binder;
        try {
            binder = createBinder();
            return getRegistry(host, port, binder);
        } catch (Exception e) {
            throw new NamingException("Cannot create binder " + e.toString());
        }
    }

    /**
   * Returns a reference to the {@link NamingService} on the given host, for the
   * default {@link #DEFAULT_PORT port}.
   *
   * @param host the host where the naming service is located.
   * @param binder the binder to be used to create the binding to the naming
   *      service interface.
   * @return a reference to the {@link NamingService} on the given host, for the
   *      default {@link #DEFAULT_PORT port}.
   * @throws NamingException 
   * @throws JonathanException if something goes wrong.
   */
    public static NamingService getRegistry(final String host, final Binder binder) throws NamingException {
        return getRegistry(host, DEFAULT_PORT, binder);
    }

    /**
   * Returns a reference to the {@link NamingService} on the given host and
   * port.
   *
   * @param host the host where the naming service is located.
   * @param port the port that was used to export the naming service on the
   *      given host.
   * @param binder the binder to be used to create the binding to the naming
   *      service interface.
   * @return a reference to the {@link NamingService} on the given host and
   *      port.
   * @throws NamingException 
   * @throws JonathanException if something goes wrong.
   */
    public static NamingService getRegistry(final String host, final int port, final Binder binder) throws NamingException {
        String name = "fr.jade.fraclite.orb.registry.NamingService::0::" + host + "::" + String.valueOf(port);
        Name n = binder.decode(name.getBytes());
        return (NamingService) binder.bind(n, null);
    }

    public static Binder createBinder() throws Exception {
        Component boot = Fractal.getBootstrapComponent();
        TypeFactory tf = Fractal.getTypeFactory(boot);
        GenericFactory cf = Fractal.getGenericFactory(boot);
        ComponentType type = tf.createFcType(new InterfaceType[] { tf.createFcItfType("binder", "org.objectweb.naming.Binder", TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) });
        Component binder = cf.newFcInstance(type, "basic", "fr.jade.fraclite.orb.RMIBinder");
        System.out.println("Binder is ready.");
        return (Binder) binder.getFcInterface("binder");
    }
}
