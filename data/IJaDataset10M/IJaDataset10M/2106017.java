package net.sf.jukebox.jmx;

/**
 * Interface to be used to declare that the object <strong>needs</strong> to be exposed via JMX.
 *
 * Whether or not the specific class <strong>can</strong> be exposed via JMX is decided by the {@link JmxWrapper},
 * taking into account {@link JmxAttribute} annotations.
 *
 * Whether or not the specific <strong>instance</strong> of an object that <strong>can</strong> be exposed
 * <strong>will</strong> be exposed depends on the return value of {@link #getJmxDescriptor()}.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2007-2009
 */
public interface JmxAware {

    /**
     * Get the JMX descriptor.
     *
     * @return The descriptor, or {@code null} if this specific instance of the object doesn't need to be exposed.
     */
    JmxDescriptor getJmxDescriptor();
}
