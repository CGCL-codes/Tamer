package org.osmorc.run;

import com.intellij.openapi.util.InvalidDataException;
import org.jdom.Element;

/**
 * Used to load legacy Osmorc run configurations as OSGi run configurations.
 *
 * @author Robert F. Beeger (robert@beeger.net)
 */
public interface LegacyOsgiRunConfigurationLoader {

    boolean readExternal(final Element element, OsgiRunConfiguration osgiRunConfiguration) throws InvalidDataException;

    void finishAfterModulesAreAvailable(OsgiRunConfiguration osgiRunConfiguration);
}
