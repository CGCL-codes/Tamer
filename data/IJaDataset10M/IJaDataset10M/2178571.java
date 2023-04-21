package org.geogurus.data.webservices;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geogurus.data.ConnectionParams;
import org.geogurus.data.DataAccess;
import org.geogurus.data.DataAccessHelper;
import org.geogurus.data.DataAccessType;
import org.geogurus.data.Datasource;
import org.geogurus.data.Extent;
import org.geogurus.data.Geometry;
import org.geogurus.data.Operation;
import org.geogurus.data.Option;
import org.geogurus.data.cache.NoOpCacheable;
import org.geogurus.data.cache.ObjectCache;
import org.geogurus.gas.objects.GeometryClassFieldBean;
import org.geogurus.mapserver.objects.Layer;
import org.geogurus.mapserver.objects.MetaData;
import org.geogurus.mapserver.objects.MsLayer;
import org.geogurus.mapserver.objects.RGB;
import org.geotools.data.Query;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Accesses a WMS layer
 * 
 * @author jesse
 */
public class WmsDataAccess extends DataAccess {

    private static final long serialVersionUID = 1L;

    private String layerName;

    /** the DB major version for this datasource */
    protected int dbVersion;

    public WmsDataAccess(String host, String layer, Datasource owner) {
        super(layer, owner, DataAccessType.WMS);
        this.host = host;
        this.datasourceName = name;
        this.layerName = layer;
    }

    /**
     * Gets metadata information of a WMS source. WMS is treated as a Raster
     * data.
     */
    @SuppressWarnings("unchecked")
    public boolean loadMetadata() {
        numGeometries = 1;
        geometryAttributeName = "_wms_layer_";
        geomTypeCode = Geometry.RASTER;
        String epsg = "EPSG:4326";
        CRSEnvelope crsEnv = new CRSEnvelope("4326", -180, -90, 180, 90);
        Extent ex = null;
        if (metadata == null) {
            metadata = new Vector<GeometryClassFieldBean>();
        }
        org.geotools.data.ows.Layer wmsLayer = getWmsLayer();
        if (wmsLayer.getBoundingBoxes().size() > 0) {
            Iterator<Object> iteCrs = wmsLayer.getBoundingBoxes().keySet().iterator();
            if (iteCrs.hasNext()) {
                epsg = (String) iteCrs.next();
                crsEnv = (CRSEnvelope) wmsLayer.getBoundingBoxes().get(epsg);
            }
        } else if (wmsLayer.getLatLonBoundingBox() != null) {
            crsEnv = wmsLayer.getLatLonBoundingBox();
        }
        ex = new Extent(crsEnv.getMinX(), crsEnv.getMinY(), crsEnv.getMaxX(), crsEnv.getMaxY());
        extent = ex;
        SRID = Integer.parseInt(epsg.substring(epsg.lastIndexOf(":") + 1));
        return true;
    }

    private org.geotools.data.ows.Layer getWmsLayer() {
        try {
            WebMapServer wms = ObjectCache.getInstance().getCachedObject(new NoOpCacheable(owner.getHost()), new GasWmsDatastoreFactory(logger), GasWmsDatastoreFactory.createParams(new URL(owner.getHost())));
            for (org.geotools.data.ows.Layer layer : wms.getCapabilities().getLayer().getChildren()) {
                if (layer.getName().equals(this.layerName)) return layer;
            }
        } catch (MalformedURLException e) {
        }
        throw new IllegalStateException("Unable to find WMS Layer: " + layerName);
    }

    @Override
    public Vector<Vector<Object>> getSampleData(int from, int to) {
        return null;
    }

    @Override
    public String getConnectionURI() {
        return host + "#" + layerName;
    }

    @Override
    protected Layer createMSLayerInner(RGB color) {
        msLayer = new Layer();
        msLayer.setName(name);
        try {
            msLayer.setConnection(host);
            msLayer.setType(Layer.RASTER);
            msLayer.setConnectionType(MsLayer.WMS);
            WebMapServer wms = new WebMapServer(new URL(this.host));
            WMSCapabilities wmsCap = wms.getCapabilities();
            String format = "image/png";
            if (wmsCap.getRequest().getGetMap().getFormats().size() > 0) {
                format = wmsCap.getRequest().getGetMap().getFormats().get(0).toString();
            }
            String strMetadata = "\"wms_server_version\"    \"" + wmsCap.getVersion() + "\"";
            strMetadata += "\n\t\t\"wms_format\"    \"" + format + "\"";
            strMetadata += "\n\t\t\"wms_srs\"    \"EPSG:" + SRID + "\"";
            strMetadata += "\n\t\t\"wms_name\"    \"" + name + "\"";
            msLayer.setMetaData(new MetaData(strMetadata));
        } catch (IOException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        org.geogurus.mapserver.objects.Class c = new org.geogurus.mapserver.objects.Class();
        if (layerName.lastIndexOf(".") > -1) {
            c.setName(layerName.substring(0, layerName.lastIndexOf(".")));
        } else {
            c.setName(layerName);
        }
        DataAccessHelper.setMSLayerColorProperties(msLayer, geomTypeCode, color, c);
        return msLayer;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public <T> Option<Boolean> performOperateStep(Operation<SimpleFeature, T> op, T context, Query query) throws IOException {
        return Option.none();
    }

    @Override
    public <T> Option<Boolean> peformOperateStep(Operation<RenderedImage, T> op, T context) {
        return Option.none();
    }

    @Override
    public <T> Option<T> resource(Class<T> request) {
        return Option.none();
    }

    @Override
    public Option<SimpleFeatureType> featureType() {
        return Option.none();
    }

    @Override
    public ConnectionParams getConnectionParams() {
        ConnectionParams params = new ConnectionParams(owner);
        params.host = this.host;
        params.layer = this.layerName;
        params.name = this.name;
        params.type = datasourceType.name();
        return params;
    }
}
