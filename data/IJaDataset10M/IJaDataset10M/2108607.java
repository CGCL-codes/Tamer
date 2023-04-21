package org.jomc.logging.ri.commons;

import java.util.logging.Level;

@javax.annotation.Generated(value = "org.jomc.tools.JavaSources", comments = "See http://jomc.sourceforge.net/jomc/1.0-alpha-11/jomc-tools")
public class CommonsObjectManagementListener implements org.jomc.spi.Listener {

    public void onLog(final Level level, final String message, final Throwable throwable) {
        if (level != null) {
            if (level.equals(Level.CONFIG) || level.equals(Level.FINE)) {
                this.getLogger().debug(message, throwable);
            } else if (level.equals(Level.FINER) || level.equals(Level.FINEST)) {
                this.getLogger().trace(message, throwable);
            } else if (level.equals(Level.INFO)) {
                this.getLogger().info(message, throwable);
            } else if (level.equals(Level.SEVERE)) {
                this.getLogger().error(message, throwable);
            } else if (level.equals(Level.WARNING)) {
                this.getLogger().warn(message, throwable);
            } else {
                this.getLogger().trace(message, throwable);
            }
        }
    }

    /** Creates a new {@code CommonsObjectManagementListener} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.JavaSources", comments = "See http://jomc.sourceforge.net/jomc/1.0-alpha-11/jomc-tools")
    public CommonsObjectManagementListener() {
        super();
    }

    /**
     * Gets the {@code Logger} dependency.
     * <p>This method returns the "{@code JOMC Logging Commons Logging}" object of the {@code org.jomc.logging.Logger} specification at specification level 1.0.</p>
     * <p>That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.</p>
     * <p><b>Properties</b><dl>
     * <dt>"{@code name}"</dt>
     * <dd>Property of type {@code java.lang.String}.
     * </dd>
     * </dl>
     * @return The {@code Logger} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.JavaSources", comments = "See http://jomc.sourceforge.net/jomc/1.0-alpha-11/jomc-tools")
    private org.jomc.logging.Logger getLogger() {
        final org.jomc.logging.Logger _d = (org.jomc.logging.Logger) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "Logger");
        assert _d != null : "'Logger' dependency not found.";
        return _d;
    }
}
