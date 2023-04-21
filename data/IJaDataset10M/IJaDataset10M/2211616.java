package com.afp.ines.component.persistence.rdbms;

import java.net.URL;
import org.iptc.ines.store.CatalogRepositoryStore;
import org.iptc.nar.core.model.Catalog;
import com.afp.ines.component.persistence.catalog.factory.CatalogFactory;
import com.afp.ines.component.persistence.catalog.factory.CatalogRepositoryFactory;
import com.afp.ines.component.persistence.model.catalog.CatalogRepository;
import com.afp.ines.component.persistence.rdbms.dao.CatalogRepositoryDao;

public class CatalogRepositoryStoreImpl implements CatalogRepositoryStore {

    private CatalogRepositoryDao m_catalogRepositoryDao;

    public CatalogRepositoryStoreImpl(CatalogRepositoryDao catalogRepositoryDao) {
        m_catalogRepositoryDao = catalogRepositoryDao;
    }

    public Catalog getCatalogFromLocation(URL remoteLocation) {
        CatalogRepository itemCatalog = m_catalogRepositoryDao.getCatalogFromURL(remoteLocation);
        if (itemCatalog == null) return null;
        Catalog catalog = CatalogFactory.instance.buildCatalog(itemCatalog);
        return catalog;
    }

    public void save(Catalog catalog, URL remoteLocation) {
        CatalogRepository itemCatalog = CatalogRepositoryFactory.instance.buildItemCatalog(catalog);
        itemCatalog.setCatalogHref(remoteLocation);
        m_catalogRepositoryDao.save(itemCatalog);
    }
}
