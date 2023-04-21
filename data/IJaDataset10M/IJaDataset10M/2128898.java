package org.daisy.braille.embosser;

import java.util.Iterator;
import javax.imageio.spi.ServiceRegistry;
import org.daisy.factory.FactoryCatalog;

/**
 * Provides a catalog of Embosser factories.
 * @author Joel Håkansson
 */
public abstract class EmbosserCatalog implements FactoryCatalog<Embosser> {

    protected EmbosserCatalog() {
    }

    /**
	 * Obtains a new EmbosserCatalog instance. If at least one implementation can be found 
	 * using the Services API, then the first one will be returned. Otherwise the default EmbosserCatalog
	 * will be used.
	 * 
	 * The default EmbosserCatalog will use the Services API to
	 * find EmbosserProviders. The combined result from all EmbosserProviders are available to
	 * the catalog.
	 * @return returns a new EmbosserCatalog instance. 
	 */
    public static EmbosserCatalog newInstance() {
        Iterator<EmbosserCatalog> i = ServiceRegistry.lookupProviders(EmbosserCatalog.class);
        while (i.hasNext()) {
            return i.next();
        }
        return new DefaultEmbosserCatalog();
    }
}
