package org.dwgsoftware.raistlin.extension.manager.impl;

import org.dwgsoftware.raistlin.extension.Extension;
import org.dwgsoftware.raistlin.extension.manager.ExtensionManager;
import org.dwgsoftware.raistlin.extension.manager.OptionalPackage;

/**
 * A Noop ExtensionManager that can't provide any extensions.
 * This is for use in certain environments (ala Servlets) that
 * require apps to be be self-contained.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:37 $
 */
public class NoopExtensionManager implements ExtensionManager {

    /**
     * Return an empty array of {@link OptionalPackage}s.
     *
     * @param extension the extension looking for
     * @see ExtensionManager#getOptionalPackages
     */
    public OptionalPackage[] getOptionalPackages(final Extension extension) {
        return new OptionalPackage[0];
    }
}
