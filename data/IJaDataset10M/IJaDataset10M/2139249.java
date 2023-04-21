package org.fao.fenix.web.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.fao.fenix.domain.filter.DataList;
import org.fao.fenix.domain.info.dataset.Dataset;
import org.fao.fenix.domain.map.GeoView;
import org.fao.fenix.domain.map.ProjectedData;
import org.fao.fenix.domain.map.TabularLayer;
import org.fao.fenix.domain.map.WMSMapProvider;
import org.fao.fenix.domain.map.geoserver.BoundingBox;
import org.fao.fenix.domain.map.layer.DBFeatureLayer;
import org.fao.fenix.domain.perspective.MapView;
import org.fao.fenix.domain.perspective.mapcontext.filter.Dimension;
import org.fao.fenix.map.geoserver.ProjectedDataMetadao;
import org.fao.fenix.map.geoserver.io.GeoserverPublisher;
import org.fao.fenix.persistence.info.dataset.DatasetDao;
import org.fao.fenix.persistence.map.GeoViewDao;
import org.fao.fenix.persistence.map.WMSMapProviderDao;
import org.fao.fenix.persistence.map.geoserver.GeoServerDao;
import org.fao.fenix.persistence.perspective.MapDao;
import org.fao.fenix.web.client.FenixResource;
import org.fao.fenix.web.client.services.MapService;
import org.fao.fenix.web.client.vo.map.ClientGeoView;
import org.fao.fenix.web.client.vo.map.ClientMapView;
import org.fao.fenix.web.utils.FenixResourceBuilder;
import org.fao.fenix.web.utils.map.ClientGeoViewLoader;
import org.fao.fenix.web.utils.map.ClientMapViewBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MapServiceImpl extends RemoteServiceServlet implements MapService {

    private DatasetDao datasetDao;

    private MapDao mapDao;

    private GeoViewDao geoViewDao;

    private GeoServerDao geoServerDao;

    private WMSMapProviderDao wmsMapProviderDao;

    private ProjectedDataMetadao projectedDataMetadao;

    @Override
    public ClientMapView getMap(long mapId) {
        MapView mapView = mapDao.findById(mapId);
        return ClientMapViewBuilder.build(mapView);
    }

    @Override
    public ClientMapView createEmptyMap() {
        MapView mapView = new MapView();
        mapView.setTitle("Empty map " + getNow());
        BoundingBox bb = new BoundingBox("EPSG:4326", -180d, -90d, 180d, 90d);
        mapView.setBoundingBox(bb);
        mapDao.save(mapView);
        return ClientMapViewBuilder.build(mapView);
    }

    private static String getNow() {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return now.get(Calendar.YEAR) + "-" + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH) + "|" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + "." + now.get(Calendar.MILLISECOND);
    }

    @Override
    public ClientGeoView addLayerToMap(long mapViewId, long layerId) {
        MapView mapView = mapDao.findById(mapViewId);
        WMSMapProvider wmsMapProvider = wmsMapProviderDao.findById(layerId);
        GeoView geoView = mapView.addMapProvider(wmsMapProvider);
        geoViewDao.save(geoView);
        mapDao.update(mapView);
        return ClientGeoViewLoader.buildClientResource(geoView);
    }

    @Override
    public void addGeoViewToMap(long mapViewId, long geoViewId) {
        MapView mapView = mapDao.findById(mapViewId);
        GeoView geoView = geoViewDao.findById(geoViewId);
        mapView.addGeoView(geoView);
        mapDao.update(mapView);
    }

    @Override
    public void removeGeoViewFromMap(long mapViewId, long geoViewId) {
        MapView mapView = mapDao.findById(mapViewId);
        mapView.removeGeoView(geoViewId);
        mapDao.update(mapView);
    }

    @Override
    public List getJoinableDataList(long geoViewId) {
        GeoView geoView = geoViewDao.findById(geoViewId);
        WMSMapProvider wmp = geoView.getWmsMapProvider();
        if (!(wmp instanceof DBFeatureLayer)) {
            throw new IllegalArgumentException("GeoView " + geoViewId + " references to a " + wmp.getClass().getSimpleName() + ". DBFeatureLayer was needed");
        }
        DBFeatureLayer dbFeatureLayer = (DBFeatureLayer) wmp;
        List<DataList> datalist = dbFeatureLayer.getJoinableDataList();
        List<FenixResource> ret = new ArrayList<FenixResource>();
        for (DataList dataList : datalist) {
            ret.add(FenixResourceBuilder.build(dataList));
        }
        return ret;
    }

    /**
	 * Creates a ProjectedData entry and a related GeoView associated to it
	 * 
	 * @param dataListId
	 * @param layerId
	 * @return
	 */
    @Override
    public ClientGeoView projectData(long dataListId, long layerId) {
        WMSMapProvider mp = wmsMapProviderDao.findById(layerId);
        if (!(mp instanceof DBFeatureLayer)) {
            throw new IllegalArgumentException("Layer '" + mp.getLayerName() + "' (id:" + layerId + ") cannot be projected.");
        }
        DBFeatureLayer dbfl = (DBFeatureLayer) mp;
        Dataset dl = datasetDao.findById(dataListId);
        ProjectedData pd = new ProjectedData(dl, dbfl);
        projectedDataMetadao.save(pd);
        GeoView geoView = new GeoView();
        geoView.setWmsMapProvider(pd);
        geoView.setTitle("PROJ '" + dl.getTitle() + "' ON '" + dbfl.getTitle() + "'");
        geoViewDao.save(geoView);
        return ClientGeoViewLoader.buildClientResource(geoView);
    }

    @Override
    public Map getProjectedDimensions(long geoViewId) {
        GeoView geoView = geoViewDao.findById(geoViewId);
        WMSMapProvider wmp = geoView.getWmsMapProvider();
        if (!(wmp instanceof ProjectedData)) {
            throw new IllegalArgumentException("GeoView " + geoViewId + " references to a " + wmp.getClass().getSimpleName() + ". ProjectedData was needed");
        }
        ProjectedData projectedData = (ProjectedData) wmp;
        Map<String, Dimension> mapDim = projectedData.getBoundDimensions();
        Map<String, String> ret = new HashMap<String, String>();
        for (String dimName : mapDim.keySet()) {
            Dimension dim = mapDim.get(dimName);
            ret.put(dim.getDimName(), dim.getDimValue());
        }
        return ret;
    }

    @Override
    public void setProjectedDimensions(long geoViewId, Map dimensions) {
        GeoView geoView = geoViewDao.findById(geoViewId);
        WMSMapProvider wmp = geoView.getWmsMapProvider();
        if (!(wmp instanceof ProjectedData)) throw new IllegalArgumentException("GeoView " + geoViewId + " references to a " + wmp.getClass().getSimpleName() + ". ProjectedData was needed");
        Map<String, String> dimss = (Map<String, String>) dimensions;
        ProjectedData projectedData = (ProjectedData) wmp;
        for (String dimName : dimss.keySet()) {
            String dimVal = dimss.get(dimName);
            projectedData.setDimValue(dimName, dimVal);
        }
        projectedDataMetadao.update(projectedData);
    }

    @Override
    public String[] getColumnNames(long wmsMapProviderId) {
        TabularLayer tabularLayer = getTabularLayer(wmsMapProviderId);
        List<String> fieldList = geoServerDao.getColumnNames(tabularLayer);
        List<String> filteredfieldList = new ArrayList<String>(fieldList.size());
        for (String fieldName : fieldList) {
            if (fieldName.equalsIgnoreCase(tabularLayer.getGeomColumn())) {
                continue;
            } else {
                filteredfieldList.add(fieldName);
            }
        }
        return filteredfieldList.toArray(new String[filteredfieldList.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getRecords(long wmsMapProviderId, List columnNames) {
        TabularLayer tabularLayer = getTabularLayer(wmsMapProviderId);
        List originalRowList = geoServerDao.getColumnValues(tabularLayer, columnNames);
        List<List<String>> rowList = new ArrayList<List<String>>();
        for (Object[] originalRow : (List<Object[]>) originalRowList) {
            List<String> row = new ArrayList<String>(originalRow.length);
            for (Object field : originalRow) {
                row.add(field.toString());
            }
            rowList.add(row);
        }
        return rowList;
    }

    private TabularLayer getTabularLayer(long wmsMapProviderId) {
        WMSMapProvider wmp = wmsMapProviderDao.findById(wmsMapProviderId);
        return checkTabularLayer(wmp);
    }

    public TabularLayer checkTabularLayer(WMSMapProvider wmp) throws IllegalArgumentException {
        TabularLayer tabularLayer;
        if (wmp instanceof DBFeatureLayer) {
            tabularLayer = (DBFeatureLayer) wmp;
        } else if (wmp instanceof ProjectedData) {
            tabularLayer = (ProjectedData) wmp;
        } else {
            throw new IllegalArgumentException("Given layer is not a TabularLayer (" + wmp.getClass().getSimpleName() + ")");
        }
        return tabularLayer;
    }

    public void setGeoViewStyle(long geoViewId, String styleName, String sldBody) {
        GeoView geoView = geoViewDao.findById(geoViewId);
        WMSMapProvider wmp = geoView.getWmsMapProvider();
        geoView.setStyleName(styleName);
        geoViewDao.update(geoView);
        GeoserverPublisher.createStyle(wmp.getGetMapUrl(), styleName, sldBody);
    }

    public void runWMSHarvester() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDatasetDao(DatasetDao datasetDao) {
        this.datasetDao = datasetDao;
    }

    public void setMapDao(MapDao mapDao) {
        this.mapDao = mapDao;
    }

    public void setGeoViewDao(GeoViewDao geoViewDao) {
        this.geoViewDao = geoViewDao;
    }

    public void setGeoServerDao(GeoServerDao geoServerDao) {
        this.geoServerDao = geoServerDao;
    }

    public void setWmsMapProviderDao(WMSMapProviderDao wmsMapProviderDao) {
        this.wmsMapProviderDao = wmsMapProviderDao;
    }

    public void setProjectedDataMetadao(ProjectedDataMetadao projectedDataMetadao) {
        this.projectedDataMetadao = projectedDataMetadao;
    }
}
