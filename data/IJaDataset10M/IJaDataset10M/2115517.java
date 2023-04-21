package org.fao.fenix.persistence.map.geoserver;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.fao.fenix.domain.map.TabularLayer;
import org.fao.fenix.domain.map.geoserver.CoverageStore;
import org.fao.fenix.domain.map.geoserver.DataStore;
import org.fao.fenix.domain.map.geoserver.FeatureType;
import org.fao.fenix.domain.map.geoserver.GeoServer;
import org.fao.fenix.persistence.util.DBTableContent;
import org.fao.fenix.persistence.util.DBTableInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author etj
 */
@Repository
@Transactional
public class GeoServerDao {

    private DBTableInfo dbTableInfo;

    private DBTableContent dbTableContent;

    @PersistenceContext
    private EntityManager entityManager;

    public List<GeoServer> findAllGeoServers() {
        return entityManager.createQuery("from GeoServer").getResultList();
    }

    public List<DataStore> findAllDataStores() {
        return entityManager.createQuery("from DataStore").getResultList();
    }

    public GeoServer findById(long id) {
        return entityManager.find(GeoServer.class, id);
    }

    @SuppressWarnings("unchecked")
    public void save(GeoServer geoServer) {
        entityManager.persist(geoServer);
    }

    public GeoServer update(GeoServer geoServer) {
        return entityManager.merge(geoServer);
    }

    public void delete(GeoServer geoServer) {
        entityManager.remove(geoServer);
    }

    @SuppressWarnings("unchecked")
    public void save(DataStore dataStore) {
        entityManager.persist(dataStore);
    }

    public DataStore update(DataStore dataStore) {
        return entityManager.merge(dataStore);
    }

    public void delete(DataStore dataStore) {
        entityManager.remove(dataStore);
    }

    @SuppressWarnings("unchecked")
    public void save(CoverageStore coverageStore) {
        entityManager.persist(coverageStore);
    }

    public CoverageStore update(CoverageStore coverageStore) {
        return entityManager.merge(coverageStore);
    }

    public void delete(CoverageStore coverageStore) {
        entityManager.remove(coverageStore);
    }

    @SuppressWarnings("unchecked")
    public void save(FeatureType featureType) {
        entityManager.persist(featureType);
    }

    public void delete(FeatureType featureType) {
        entityManager.remove(featureType);
    }

    public List<String> getColumnNames(TabularLayer tabularLayer) {
        String layerName = tabularLayer.getFeatureType().getLayerName();
        List<DBTableInfo.Field> fields = dbTableInfo.getFields(layerName);
        List<String> ret = new ArrayList<String>();
        for (DBTableInfo.Field field : fields) {
            String name = field.getName();
            ret.add(name);
        }
        return ret;
    }

    public List<Object[]> getColumnValues(TabularLayer tabularLayer, List<String> colNames) {
        FeatureType featureType = tabularLayer.getFeatureType();
        return dbTableContent.getRows(featureType.getLayerName(), colNames);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setDbTableInfo(DBTableInfo dbTableInfo) {
        this.dbTableInfo = dbTableInfo;
    }

    public void setDbTableContent(DBTableContent dbTableContent) {
        this.dbTableContent = dbTableContent;
    }
}
