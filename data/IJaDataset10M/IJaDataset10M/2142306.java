package org.openejb.ui.jedi.openejb11.ejb;

import org.opentools.deployer.plugins.Editor;
import org.opentools.deployer.plugins.Plugin;

/**
 * Base class for all Editors in this package.  Provides default
 * implementations for infrequently-used methods of the interface.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision 1.0 $
 */
public abstract class OpenEJBEditor implements Editor {

    public OpenEJBEditor() {
    }

    public void updateRootMetaData(Object metaData, Plugin plugin) {
    }
}
