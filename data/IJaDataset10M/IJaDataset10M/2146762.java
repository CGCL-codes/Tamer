package org.iptc.ines.component.persistence.catalog.factory;

import java.util.List;
import org.iptc.ines.component.persistence.model.catalog.CatalogEntry;
import org.iptc.ines.component.persistence.model.catalog.CatalogRepository;
import org.iptc.nar.core.datatype.SchemaDeclarationType;
import org.iptc.nar.core.model.ACatalog;
import org.iptc.nar.core.model.Catalog;
import org.iptc.nar.core.model.CatalogRef;

public class CatalogRepositoryFactory {

    public static CatalogRepositoryFactory instance = new CatalogRepositoryFactory();

    public CatalogRepositoryFactory() {
        super();
    }

    private void buildFromACatalog(ACatalog catalog, CatalogRepository itemCatalog) {
        itemCatalog.setIdentity(catalog.getIdentity());
    }

    public CatalogRepository buildItemCatalog(Catalog catalog) {
        CatalogRepository catalogRepository = new CatalogRepository();
        buildFromACatalog(catalog, catalogRepository);
        List<SchemaDeclarationType> entries = catalog.getSchemeDeclarations();
        for (SchemaDeclarationType entry : entries) {
            catalogRepository.addCalatogEntry(buildCatalogEntry(entry));
        }
        return catalogRepository;
    }

    public CatalogRepository buildItemCatalog(CatalogRef catalogRef) {
        CatalogRepository catalogRepository = new CatalogRepository();
        buildFromACatalog(catalogRef, catalogRepository);
        catalogRepository.setCatalogHref(catalogRef.getCatalogRef());
        return catalogRepository;
    }

    /**
	 * 
	 * @param schemaType
	 * @return
	 */
    private CatalogEntry buildCatalogEntry(SchemaDeclarationType schemaType) {
        CatalogEntry catalogEntry = new CatalogEntry();
        catalogEntry.setAlias(schemaType.getSchemeAlias());
        catalogEntry.setScheme(schemaType.getSchemeURI().toString());
        return catalogEntry;
    }
}
