package com.esri.gpt.catalog.search;

import com.esri.gpt.catalog.context.CatalogConfiguration;
import com.esri.gpt.catalog.search.SearchEngineCSW.Scheme;
import com.esri.gpt.framework.context.ApplicationContext;
import com.esri.gpt.framework.context.ConfigurationException;
import com.esri.gpt.framework.context.RequestContext;
import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.client.CswRecord;
import java.util.HashMap;
import java.util.List;

/**
 * Identifies resource and content types.
 */
public class ResourceIdentifier {

    /** Default constructor. */
    public ResourceIdentifier() {
    }

    /**
   * Guesses the ArcGIS Server SOAP endpoint for a MapServer server.
   * </br>Only MapServer services are considered.
   * @param serviceUrl the service URL
   * @return the ArcGIS MapServer SOAP endpoint (without "?wsdl", null if unknown) 
   */
    public String guessAgsMapServerSoapUrl(String serviceUrl) {
        serviceUrl = Val.chkStr(serviceUrl);
        String url = serviceUrl.toLowerCase();
        if ((url.indexOf("arcgis/services") >= 0) && (url.endsWith("/mapserver") || url.endsWith("/mapserver?wsdl"))) {
            int index = url.indexOf("/mapserver");
            return serviceUrl.substring(0, index + "/mapserver".length());
        }
        return null;
    }

    /**
   * Guesses the ArcGIS Server rest endpoint for a service.
   * @param serviceUrl the service URL
   * @return the service's ArcGIS Server rest endpoint (null if unknown)
   */
    public String guessAgsServiceRestUrl(String serviceUrl) {
        serviceUrl = Val.chkStr(serviceUrl);
        String url = serviceUrl.toLowerCase();
        if (url.contains("arcgis/rest") || url.contains("rest/services")) {
            String[] types = { "mapserver", "imageserver", "globeserver", "gpserver", "geocodeserver", "geometryserver", "networkserver", "geodataserver" };
            for (String type : types) {
                int index = url.indexOf("/" + type);
                if (index > 0) {
                    String serviceHomeUrl = serviceUrl.substring(0, index + ("/" + type).length());
                    if (serviceHomeUrl.contains("arcgis/rest") || serviceHomeUrl.contains("rest/services")) {
                        return serviceHomeUrl;
                    }
                }
            }
        }
        return null;
    }

    /**
   * Guesses the ArcIMS content type based upon a supplied resource type.
   * @param resourceType the resource type
   * @return the ArcIMS content type (empty String if none)
   */
    public String guessArcIMSContentTypeFromResourceType(String resourceType) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("livedata", "liveData");
        map.put("downloadabledata", "downloadableData");
        map.put("offlinedata", "offlineData");
        map.put("staticmapimage", "staticMapImage");
        map.put("document", "document");
        map.put("application", "application");
        map.put("geographicservice", "geographicService");
        map.put("clearinghouse", "clearinghouse");
        map.put("mapfiles", "mapFiles");
        map.put("geographicactivities", "geographicActivities");
        map.put("unknown", "unknown");
        map.put("download", "downloadableData");
        map.put("order", "downloadableData");
        map.put("offlineaccess", "offlineData");
        map.put("search", "application");
        map.put("information", "application");
        map.put("service", "liveData");
        map.put("dataset", "downloadableData");
        map.put("series", "offlineData");
        map.put("fieldSession", "geographicActivities");
        map.put("software", "application");
        map.put("model", "geographicService");
        map.put("tile", "staticMapImage");
        map.put("live data and maps", "liveData");
        map.put("downloadable data", "downloadableData");
        map.put("offline data", "offlineData");
        map.put("static map images", "staticMapImage");
        map.put("other documents", "document");
        map.put("applications", "application");
        map.put("geographic services", "geographicService");
        map.put("clearinghouses", "clearinghouse");
        map.put("map files", "mapFiles");
        map.put("geographic activities", "geographicActivities");
        map.put("live data", "liveData");
        map.put("static map image", "staticMapImage");
        map.put("other document", "document");
        map.put("documents", "document");
        map.put("geographic service", "geographicService");
        map.put("map file", "mapFiles");
        map.put("geographic activity", "geographicActivities");
        map.put("001", "liveData");
        map.put("002", "downloadableData");
        map.put("003", "offlineData");
        map.put("004", "staticMapImage");
        map.put("005", "document");
        map.put("006", "application");
        map.put("007", "geographicService");
        map.put("008", "clearinghouse");
        map.put("009", "mapFiles");
        map.put("010", "geographicActivities");
        resourceType = Val.chkStr(resourceType).toLowerCase();
        String imsContentType = Val.chkStr(map.get(resourceType));
        return imsContentType;
    }

    /**
   * Guesses the ArcIMS content type based upon a resultant CSW record.
   * @param cswRecord the CSW record resulting from a search
   * @return the ArcIMS content type ("unknown" is returned if the content type cannot be determined
   */
    public String guessArcIMSContentTypeFromResult(CswRecord cswRecord) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("livedata", "liveData");
        map.put("downloadabledata", "downloadableData");
        map.put("offlinedata", "offlineData");
        map.put("staticmapimage", "staticMapImage");
        map.put("document", "document");
        map.put("application", "application");
        map.put("geographicservice", "geographicService");
        map.put("clearinghouse", "clearinghouse");
        map.put("mapfiles", "mapFiles");
        map.put("geographicactivities", "geographicActivities");
        map.put("unknown", "unknown");
        String contentType = "";
        List<String> schemeVals = cswRecord.getTypes().get(Scheme.CONTENTTYPE_FGDC.getUrn());
        if (schemeVals.size() < 1) {
            schemeVals = cswRecord.getTypes().get(Scheme.CONTENTTYPE_ISO.getUrn());
        }
        if (schemeVals.size() < 1) {
            schemeVals = cswRecord.getTypes().get(null);
        }
        if (schemeVals.size() > 0) {
            contentType = schemeVals.get(0);
        }
        contentType = Val.chkStr(map.get(contentType));
        if (contentType.length() == 0) contentType = "unknown";
        return contentType;
    }

    /**
   * Guesses the ArcIMS content type based upon a given url.
   * @param url the resource url
   * @return the ArcIMS content type (empty String if none) 
   */
    public String guessArcIMSContentTypeFromUrl(String url) {
        String imsContentType = "";
        url = Val.chkStr(url).toLowerCase();
        if (url.contains("service=wms") || url.contains("wmsserver") || url.contains("com.esri.wms.esrimap")) {
            imsContentType = "liveData";
        } else if (url.contains("service=wfs") || url.contains("wfsserver")) {
            imsContentType = "liveData";
        } else if (url.contains("service=wcs") || url.contains("wcsserver")) {
            imsContentType = "liveData";
        } else if (url.contains("service=csw") || url.contains("cswserver")) {
        } else if (url.contains("service=sos")) {
            imsContentType = "liveData";
        } else if (url.contains("com.esri.esrimap.esrimap")) {
            imsContentType = "liveData";
        } else if (url.endsWith(".nmf") || (url.indexOf("f=nmf") != -1)) {
            imsContentType = "liveData";
        } else if (url.endsWith(".lyr") || (url.indexOf("f=lyr") != -1)) {
            imsContentType = "liveData";
        } else if (url.endsWith(".zip")) {
            imsContentType = "downloadableData";
        } else if (url.endsWith(".lpk") || url.endsWith(".pkinfo") || url.endsWith(".e00")) {
            imsContentType = "downloadableData";
        } else if (url.startsWith("ftp://") || url.startsWith("ftps://")) {
            imsContentType = "downloadableData";
        } else if (url.endsWith(".gz") || url.endsWith(".tgz") || url.endsWith(".tar") || url.endsWith(".rar") || url.endsWith(".dbf") || url.endsWith(".shp") || url.endsWith(".xls") || url.endsWith(".txt") || url.endsWith(".dwg") || url.endsWith(".dxf") || url.endsWith(".dgn")) {
            imsContentType = "downloadableData";
        } else if (url.endsWith(".mxd")) {
            imsContentType = "liveData";
        } else if (url.endsWith(".kml") || url.endsWith(".kmz") || (url.indexOf("f=kml") > -1)) {
            imsContentType = "geographicActivities";
        } else if (url.endsWith(".xml") && (url.contains("rss") || url.contains("georss"))) {
            imsContentType = "geographicActivities";
        } else if (url.contains("arcgis/rest") || url.contains("arcgis/services") || url.contains("rest/services")) {
            if (url.contains("/mapserver")) {
                imsContentType = "liveData";
            } else if (url.contains("/globeserver")) {
                imsContentType = "liveData";
            } else if (url.contains("/imageserver")) {
                imsContentType = "liveData";
            } else if (url.contains("/gpserver")) {
                imsContentType = "geographicService";
            } else if (url.contains("/geocodeserver")) {
                imsContentType = "geographicService";
            } else if (url.contains("/geometryserver")) {
                imsContentType = "geographicService";
            } else if (url.contains("/networkserver")) {
                imsContentType = "geographicService";
            } else if (url.contains("/geodataserver")) {
                imsContentType = "geographicService";
            } else {
                imsContentType = "geographicService";
            }
        }
        return imsContentType;
    }

    /**
   * Guesses a service type from a URL.
   * @param url the url
   * @return the service type
   */
    public String guessServiceTypeFromUrl(String url) {
        String serviceType = "";
        url = Val.chkStr(url).toLowerCase();
        if (url.contains("service=wms") || url.contains("wmsserver") || url.contains("com.esri.wms.esrimap")) {
            serviceType = "wms";
        } else if (url.contains("service=wfs") || url.contains("wfsserver")) {
            serviceType = "wfs";
        } else if (url.contains("service=wcs") || url.contains("wcsserver")) {
            serviceType = "wcs";
        } else if (url.contains("com.esri.esrimap.esrimap")) {
            serviceType = "aims";
        } else if (url.contains("arcgis/rest") || url.contains("arcgis/services") || url.contains("rest/services")) {
            serviceType = "ags";
        } else if (url.indexOf("service=csw") > 0) {
            serviceType = "csw";
        } else if (url.indexOf("service=sos") > 0) {
            serviceType = "sos";
        } else if (url.endsWith(".nmf")) {
            serviceType = "ArcGIS:nmf";
        } else if (url.endsWith(".lyr")) {
            serviceType = "ArcGIS:lyr";
        } else if (url.endsWith(".mxd")) {
            serviceType = "ArcGIS:mxd";
        } else if (url.endsWith(".kml")) {
            serviceType = "kml";
        }
        if (serviceType.equals("image") || serviceType.equals("feature")) {
            serviceType = "aims";
        }
        return serviceType;
    }

    /**
   * Instantiates a new resource identifier.
   * <p/>
   * By default, a new instance of 
   * com.esri.gpt.catalog.search.ResourceIdentifier is returned.
   * <p/>
   * This can be overridden by the configuration parameter:
   * /gptConfig/catalog/parameter@key="resourceLinkIdentifier"
   * @param context the active request context
   * @return the resource identifier
   */
    public static ResourceIdentifier newIdentifier(RequestContext context) {
        CatalogConfiguration catCfg = null;
        if (context != null) {
            catCfg = context.getCatalogConfiguration();
        } else {
            ApplicationContext appCtx = ApplicationContext.getInstance();
            catCfg = appCtx.getConfiguration().getCatalogConfiguration();
        }
        String className = Val.chkStr(catCfg.getParameters().getValue("resourceLinkIdentifier"));
        if (className.length() == 0) {
            className = com.esri.gpt.catalog.search.ResourceIdentifier.class.getName();
        }
        try {
            Class<?> cls = Class.forName(className);
            Object obj = cls.newInstance();
            if (obj instanceof ResourceIdentifier) {
                ResourceIdentifier linkIdentifier = (ResourceIdentifier) obj;
                return linkIdentifier;
            } else {
                String sMsg = "The configured resourceLinkIdentifier parameter is invalid: " + className;
                throw new ConfigurationException(sMsg);
            }
        } catch (ConfigurationException t) {
            throw t;
        } catch (Throwable t) {
            String sMsg = "Error instantiating resource link identifier: " + className;
            throw new ConfigurationException(sMsg, t);
        }
    }
}
