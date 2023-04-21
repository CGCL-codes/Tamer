package playground.scnadine.gpsProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class GPSCoordFactoryPlakanda extends GPSCoordFactory {

    public GPSCoordFactoryPlakanda() {
        super();
    }

    public GPSCoordPlakanda createGPSCoord(String lineFromInputFile) throws ParseException {
        String date, time;
        double xCoord, yCoord, zCoord;
        String[] entries = lineFromInputFile.split(",");
        xCoord = Double.parseDouble(entries[1]);
        yCoord = Double.parseDouble(entries[2]);
        zCoord = Double.parseDouble(entries[3]);
        date = entries[4];
        time = entries[5];
        GregorianCalendar timestamp = new GregorianCalendar();
        timestamp = createTimestamp(time, ":", date, ".");
        GPSCoordPlakanda currentCoord = new GPSCoordPlakanda(currentCoordID, personId, xCoord, yCoord, zCoord, timestamp);
        return currentCoord;
    }

    public void createGPSCoords(File inputFile) throws IOException, ParseException {
        String lineWithCoords;
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        for (int i = 0; i < 6; i++) {
            if (i == 1) {
                String headerline = in.readLine();
                setPersonID(Integer.parseInt(headerline.substring(10)));
                System.out.println("PersonenID: " + personId);
            } else {
                in.readLine();
            }
        }
        while ((lineWithCoords = in.readLine()) != null) {
            GPSCoordPlakanda currentCoord = createGPSCoord(lineWithCoords);
            addGPSCoord(currentCoord);
            currentCoordID++;
        }
        in.close();
    }

    public GregorianCalendar createTimestamp(String time, String timeDelim, String date, String dateFormat) throws ParseException {
        int hour = 0, minute = 0, second = 0;
        int day = 0, month = 0, year = 0;
        StringTokenizer stTime = new StringTokenizer(time, timeDelim);
        hour = Integer.parseInt(stTime.nextToken());
        minute = Integer.parseInt(stTime.nextToken());
        second = Integer.parseInt(stTime.nextToken());
        StringTokenizer stDate = new StringTokenizer(date, dateFormat);
        day = Integer.parseInt(stDate.nextToken());
        month = Integer.parseInt(stDate.nextToken());
        year = Integer.parseInt(stDate.nextToken());
        GregorianCalendar timestamp = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
        return timestamp;
    }
}
