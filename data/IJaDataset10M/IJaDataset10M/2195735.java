package jp.locky.locdisp;

import java.util.Date;
import java.util.HashMap;
import jp.locky.toolkit.PlaceEngineClient;
import jp.locky.util.WiFiData;

public class LocationDisplay {

    HashMap<String, WiFiLoc> wifiLocs;

    HashMap<String, LocationImage> locations;

    PlaceEngineClient peClient;

    public void start() {
        peClient = new PlaceEngineClient();
        wifiLocs = new HashMap<String, WiFiLoc>();
        locations = new HashMap<String, LocationImage>();
        LocDataLoader.loadData(wifiLocs, locations);
        WiFiData[] wifiData;
        while (true) {
            peClient.getMeasurement();
            int count = peClient.numberOfReadings();
            wifiData = peClient.getWiFiData();
            for (int i = 0; i < count; i++) {
                String idstr = wifiData[i].BSSID;
                if (wifiLocs.containsKey(idstr)) {
                    WiFiLoc wf = wifiLocs.get(idstr);
                    if (locations.containsKey(wf.IMGNAME)) {
                        LocationImage li = locations.get(wf.IMGNAME);
                        System.out.println(li.lineName + "|" + li.stationName + "|" + wf.IMGNAME + "|BSSID=" + idstr + "|RSSI=" + wifiData[i].RSSI + "|" + (new Date().toString()));
                    } else {
                        System.out.println("ID:" + idstr + ":" + "can't find:" + wf.IMGNAME);
                    }
                } else {
                    System.out.print(".");
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        LocationDisplay ld = new LocationDisplay();
        ld.start();
    }
}
