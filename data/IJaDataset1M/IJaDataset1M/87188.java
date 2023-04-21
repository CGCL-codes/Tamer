package org.eclipse.osgi.launch;

import java.util.Map;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 * The framework factory implementation for the Equinox framework.
 * @since 3.5
 */
public class EquinoxFactory implements FrameworkFactory {

    public Framework newFramework(Map configuration) {
        return new Equinox(configuration);
    }
}
