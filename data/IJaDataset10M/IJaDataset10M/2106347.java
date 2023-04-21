package net.aa3sd.SMT;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import net.aa3sd.SMT.log.LogEvent;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.WmsLayerRendererFactory;
import com.vividsolutions.wms.WMService;

/**
 * Loads WMS map resources in a separate thread.   Currently loads hard coded Open Street Map WMS.
 * 
 * @author Paul J. Morris
 *
 */
public class WMS_Loader implements Runnable {

    public WMS_Loader() {
    }

    @Override
    public void run() {
        LayerViewPanel mapLayerPanel = SMTSingleton.getSingletonInstance().getMapLayerViewPanel();
        WMSLayer wmsLayer = null;
        WMSLayer wmsLayer1 = null;
        String layerName;
        try {
            boolean ok = false;
            layerName = "Open Street Map";
            boolean load = false;
            try {
                if (SMTSingleton.getSingletonInstance().getMapInternalFrame().getLayerManager().getLayer(layerName) == null) {
                    load = true;
                }
            } catch (NullPointerException e) {
                load = true;
            }
            if (load) {
                try {
                    URL requestUrl = new URL("http://irs.gis-lab.info/?layers=osm&");
                    InputStream inStream = requestUrl.openStream();
                    inStream.close();
                    ok = true;
                } catch (Exception e) {
                    System.out.println("Can't find server for Open Street Map WMS");
                    System.out.println(e.getMessage());
                }
                if (ok) {
                    WMService wms3 = new WMService("http://irs.gis-lab.info/?layers=osm&", WMService.WMS_1_1_1);
                    try {
                        wms3.initialize();
                        List<String> layerNames = new ArrayList<String>();
                        layerNames.add("osm");
                        wmsLayer = new WMSLayer("Open Street Map", mapLayerPanel.getLayerManager(), wms3, "EPSG:4326", layerNames, "image/jpeg");
                        SMTSingleton.getSingletonInstance().getActiveLog().logAnEvent(new LogEvent("MapLayers", LogEvent.EVENT_SYSTEM, "Found Daily Planet WMS layer"));
                    } catch (IOException e1) {
                        System.out.println("Can't initialize web mapping service for Open Street Map");
                        System.out.println(e1.getMessage());
                    }
                }
            }
            if (0 == 1) {
                URL requestUrl = new URL("http://openaerialmap.org/wms/?VERSION=1.0&request=GetMap&layers=world&styles=&srs=EPSG:4326&format=image/jpeg");
                InputStream inStream = requestUrl.openStream();
                WMService wms = new WMService("http://openaerialmap.org/wms/?VERSION=1.0&request=GetMap&layers=world&styles=&srs=EPSG:4326&format=image/jpeg", WMService.WMS_1_1_1);
                try {
                    wms.initialize();
                    List<String> layerNames = new ArrayList<String>();
                    layerNames.add("open_aerial_map");
                    wmsLayer = new WMSLayer("Open Aerial Map", mapLayerPanel.getLayerManager(), wms, "EPSG:4326", layerNames, "image/jpeg");
                    SMTSingleton.getSingletonInstance().getActiveLog().logAnEvent(new LogEvent("MapLayers", LogEvent.EVENT_SYSTEM, "Found Daily Planet WMS layer"));
                } catch (IOException e1) {
                    System.out.println("Can't initialize web mapping service for Open Aerial Map");
                    System.out.println(e1.getMessage());
                }
                WMService wms2 = new WMService("http://wms.jpl.nasa.gov/wms.cgi?layers=daily_planet&srs=EPSG:4326&", WMService.WMS_1_1_1);
                WMService wms1 = new WMService("http://wms.jpl.nasa.gov/wms.cgi?layers=BMNG&srs=EPSG:4326&style=default&", WMService.WMS_1_1_1);
                try {
                    wms2.initialize();
                    List<String> layerNames = new ArrayList<String>();
                    layerNames.add("daily_planet");
                    wmsLayer = new WMSLayer("Daily Planet", mapLayerPanel.getLayerManager(), wms2, "EPSG:4326", layerNames, "image/jpeg");
                    SMTSingleton.getSingletonInstance().getActiveLog().logAnEvent(new LogEvent("MapLayers", LogEvent.EVENT_SYSTEM, "Found Daily Planet WMS layer"));
                    wms1.initialize();
                    layerNames = new ArrayList<String>();
                    layerNames.add("BMNG");
                    wmsLayer1 = new WMSLayer("Blue Marble", mapLayerPanel.getLayerManager(), wms1, "EPSG:4326", layerNames, "image/jpeg");
                    SMTSingleton.getSingletonInstance().getActiveLog().logAnEvent(new LogEvent("MapLayers", LogEvent.EVENT_SYSTEM, "Found Daily Planet WMS layer"));
                } catch (IOException e1) {
                    System.out.println("Can't initialize web mapping service for JPL's daily planet");
                    System.out.println(e1.getMessage());
                }
            }
        } catch (UnknownHostException e2) {
            System.out.println("Can't initialize web mapping service for JPL's daily planet");
            System.out.println(e2.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't open web mapping service for JPL's daily planet");
            System.out.println(e.getMessage());
        }
        if (wmsLayer != null) {
            mapLayerPanel.getRenderingManager().setRendererFactory(wmsLayer.getClass(), new WmsLayerRendererFactory());
            mapLayerPanel.getLayerManager().addLayerable("BaseMap", wmsLayer);
            SMTSingleton.getSingletonInstance().getActiveLog().logAnEvent(new LogEvent("MapLayers", LogEvent.EVENT_SYSTEM, "Added Daily Planet WMS layer"));
        }
        if (wmsLayer1 != null) {
            mapLayerPanel.getRenderingManager().setRendererFactory(wmsLayer1.getClass(), new WmsLayerRendererFactory());
            mapLayerPanel.getLayerManager().addLayerable("BaseMap", wmsLayer1);
            SMTSingleton.getSingletonInstance().getActiveLog().logAnEvent(new LogEvent("MapLayers", LogEvent.EVENT_SYSTEM, "Added Blue Marble WMS layer"));
        }
    }
}
