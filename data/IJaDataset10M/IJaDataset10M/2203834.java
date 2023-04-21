package org.catacombae.jparted.lib.ps.container;

import org.catacombae.jparted.lib.DataLocator;

/**
 *
 * @author erik
 */
public abstract class ContainerHandlerFactory {

    public abstract ContainerHandler createHandler(DataLocator containerData);
}
