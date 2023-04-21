package com.road.geo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import org.testng.log4testng.Logger;

public class GeoInitializer {

    static Logger LOGGER = Logger.getLogger(GeoInitializer.class);

    static GeoInitializer INSTANCE = new GeoInitializer();

    GeoIndex geoIndex;

    GeoInitializer() {
        try {
            GeoReader gr = GeoReader.readFromPlainFile(new InputStreamReader(new FileInputStream("surrounds.txt"), "utf-8"));
            Collection col = gr.namedGeoObjs.values();
            geoIndex = new GeoIndex((GeoObject[]) col.toArray(new GeoObject[col.size()]));
        } catch (IOException e) {
            LOGGER.error("Fail to read the geo resource file!!!", e);
        }
    }

    public static GeoInitializer getInstance() {
        return INSTANCE;
    }

    public GeoIndex getGeoIndex() {
        return this.geoIndex;
    }

    public static void main(String[] args) throws GeoException {
        System.out.println(GeoInitializer.getInstance().getGeoIndex().getGeo("孙桥路"));
        System.out.println(GeoInitializer.getInstance().getGeoIndex().getGeo("孙桥路张江路"));
        System.out.println(GeoInitializer.getInstance().getGeoIndex().getGeo("张江路孙桥路"));
        String[] gos = GeoInitializer.getInstance().getGeoIndex().findSurrounds("张江路孙桥路", 10);
        for (String go : gos) {
            System.out.println(go);
        }
    }
}
