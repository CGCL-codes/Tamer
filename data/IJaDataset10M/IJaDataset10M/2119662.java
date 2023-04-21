package com.bkoenig.photo.toolkit.utils;

import java.io.File;
import java.io.FileOutputStream;
import org.jdom.Document;
import org.jdom.Element;

public class Export {

    public void export2GPX() {
        Element rootGPX = new Element("gpx");
        Document exportGPX = new Document(rootGPX);
        Element photoGPX = new Element("wpt");
        photoGPX.setAttribute("lat", "39.921055008");
        photoGPX.setAttribute("lon", "3.054223107");
        Element photoName = new Element("name");
        photoName.setText("fullpath/xxx.jpg");
        photoGPX.addContent(photoName);
        Element photoDate = new Element("time");
        photoDate.setText("2005-05-16T11:49:06Z");
        photoGPX.addContent(photoDate);
        Element photoType = new Element("sym");
        photoType.setText("Photo");
        photoGPX.addContent(photoType);
        rootGPX.addContent(photoGPX);
        exportGPX.toString();
    }

    public void export2KML() {
    }

    public static void exportStats2CSV() {
        StringBuffer buffer = new StringBuffer();
        exportStats2CSVHelper(buffer, PhotoDB.statsBrennweiteName, PhotoDB.statsBrennweiteCount, "Brennweite");
        exportStats2CSVHelper(buffer, PhotoDB.statsBlendeName, PhotoDB.statsBlendeCount, "Blende");
        exportStats2CSVHelper(buffer, PhotoDB.statsBelichtungszeitName, PhotoDB.statsBelichtungszeitCount, "Belichtungszeit");
        exportStats2CSVHelper(buffer, PhotoDB.statsJahrName, PhotoDB.statsJahrCount, "Jahr");
        exportStats2CSVHelper(buffer, PhotoDB.statsMonatNames, PhotoDB.statsMonatCount, "Monat");
        exportStats2CSVHelper(buffer, PhotoDB.statsWochentagNames, PhotoDB.statsWochentagCount, "Wochentag");
        exportStats2CSVHelper(buffer, PhotoDB.statsTagsName, PhotoDB.statsTagsCount, "Tags");
        exportStats2CSVHelper(buffer, PhotoDB.statsCameraName, PhotoDB.statsCameraCount, "Camera");
        try {
            FileOutputStream fos = new FileOutputStream("exportStats.csv");
            fos.write(buffer.toString().getBytes());
        } catch (Exception ex) {
            Logger.error("Fehler beim Schreiben in exportStats.csv.");
            Logger.debug(ex.getClass() + " " + ex.getMessage());
            for (int e = 0; e < ex.getStackTrace().length; e++) Logger.debug("     " + ex.getStackTrace()[e].toString());
            ex.printStackTrace();
        }
    }

    private static void exportStats2CSVHelper(StringBuffer buffer, String[] names, int[] values, String title) {
        for (int i = 0; i < names.length; i++) {
            buffer.append("\"" + title + "\";\"Anzahl\";\n");
            buffer.append("\"" + names[i] + "\";\"" + values[i] + "\";\n");
        }
        buffer.append("\"\";\"\";\n");
    }
}
