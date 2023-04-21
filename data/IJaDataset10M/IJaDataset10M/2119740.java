package org.fao.fenix.web.modules.core.server.utils.map;

import org.fao.fenix.domain.map.GeoView;
import org.fao.fenix.domain.map.ProjectedData;
import org.fao.fenix.domain.map.WMSMapProvider;
import org.fao.fenix.domain.map.geoserver.BoundingBox;
import org.fao.fenix.domain.map.layer.DBFeatureLayer;
import org.fao.fenix.web.modules.core.client.view.vo.ClientBBox;
import org.fao.fenix.web.modules.core.client.view.vo.ClientGeoView;

/**
 *
 * @author etj
 */
public class ClientGeoViewLoader {

    public static ClientGeoView buildClientResource(GeoView geoView) {
        ClientGeoView cgv = new ClientGeoView();
        cgv.setCurrFormat(geoView.getFormat());
        cgv.setFormat(geoView.getFormat());
        cgv.setGetMapUrl(geoView.getWmsMapProvider().getGetMapUrl());
        cgv.setHidden(geoView.isHidden());
        cgv.setId(geoView.getId());
        cgv.setLayerName(geoView.getWmsMapProvider().getLayerName());
        cgv.setPosition(geoView.getPosition());
        cgv.setQueryable(geoView.getWmsMapProvider().isQueryable());
        cgv.setSld(geoView.getStyleName());
        cgv.setTitle(geoView.getTitle());
        cgv.setBBox(buildClientBBox(geoView.getWmsMapProvider().getBoundingBox()));
        cgv.setJoinable(geoView.getWmsMapProvider() instanceof DBFeatureLayer);
        cgv.setLayerId(geoView.getWmsMapProvider().getResourceId());
        cgv.setJoined(geoView.getWmsMapProvider() instanceof ProjectedData);
        if (cgv.isJoined()) {
            ProjectedData pd = (ProjectedData) geoView.getWmsMapProvider();
            cgv.setRelatedDatasetId(pd.getDataset().getResourceId());
        }
        int type = ClientGeoView.TYPE_UNDEF;
        switch(geoView.getWmsMapProvider().getLayerType()) {
            case WMSMapProvider.TYPE_VECTOR_POINT:
                type = ClientGeoView.TYPE_VECTOR_POINT;
                break;
            case WMSMapProvider.TYPE_VECTOR_LINE:
                type = ClientGeoView.TYPE_VECTOR_LINE;
                break;
            case WMSMapProvider.TYPE_VECTOR_POLY:
                type = ClientGeoView.TYPE_VECTOR_POLY;
                break;
            case WMSMapProvider.TYPE_RASTER:
                type = ClientGeoView.TYPE_RASTER;
                break;
            default:
                type = ClientGeoView.TYPE_UNDEF;
        }
        cgv.setLayerType(type);
        return cgv;
    }

    public static ClientBBox buildClientBBox(BoundingBox bbox) {
        ClientBBox cbb = new ClientBBox();
        cbb.setSrs(bbox.getSrs());
        cbb.setMaxLat(bbox.getMaxY());
        cbb.setMaxLon(bbox.getMaxX());
        cbb.setMinLat(bbox.getMinY());
        cbb.setMinLon(bbox.getMinX());
        return cbb;
    }
}
