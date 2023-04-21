package org.kalypso.model.wspm.sobek.schema.schemata.dict;

import java.net.URL;
import org.kalypso.core.catalog.CatalogManager;
import org.kalypso.core.catalog.ICatalog;
import org.kalypso.core.catalog.ICatalogContribution;

public class KalypsoModelWspmSobekSchemaDictionaryCatalogContribution implements ICatalogContribution {

    public void contributeTo(final CatalogManager catalogManager) {
        final URL catalogURL = getClass().getResource("catalog.xml");
        final ICatalog baseCatalog = catalogManager.getBaseCatalog();
        baseCatalog.addNextCatalog(catalogURL);
    }
}
