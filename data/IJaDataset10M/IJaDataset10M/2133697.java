package amqpgw.management;

import static amqpgw.tools.ManifestReader.*;

/**
 * Implements getters for immutable information.
 * 
 * @author tmfrei
 *
 */
public class Immutable implements ImmutableMBean {

    /**
	 * To be instantiated by {@link Registrar} only.
	 */
    Immutable() {
    }

    public String getMajorVendor() {
        return getMajVendor();
    }

    public String getVersion() {
        return getMajVersion();
    }

    public String getMinorVendor() {
        return getMinVendor();
    }

    public String getBuild() {
        return getMinVersion();
    }
}
